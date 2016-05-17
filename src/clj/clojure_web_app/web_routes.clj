(ns clojure-web-app.web-routes
  (:use [slingshot.slingshot :only [throw+ try+]])
  (:require [bidi.bidi :as bidi]
            [bidi.ring]
            [ring.util.response :refer [resource-response redirect]]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [clojure-web-app.ws.ws-routes :refer [routes]]
            [clojure.core :refer [deliver promise]]))

;;;; Ring handlers

;clojure.data.json couldn't convert java.sql.Timestamp out-of-the-box
(extend-type java.sql.Timestamp
  json/JSONWriter
  (-write [date out]
    (json/-write (str date) out)))

(defn show-index-page
  "return map with index page body"
  [req]
  (resource-response "index.html" {:root "public"}))

(defn show-page-notfound
  "return map with 404 message"
  [req]
  {:status 404
   :body   "page not found"})

(defn ^:private read-body-to-json [req]
  (let [text (-> req :body slurp)]
    (if (not-empty text)
      (json/read-json text)
      nil)))

(defn api-handler
  "This is the adapter to WebSocket's API"
  [req]
  (let [req-path     (-> req :route-params :path)
        req-method   (:request-method req)
        match        (bidi/match-route* routes req-path {:request-method req-method})
        ws-handler   (:handler match)
        route-params (:route-params match)
        body         (read-body-to-json req)
        resp         (promise)]                             ;TODO: timeout
    (log/info {:msg "web/req" :method req-method :path req-path :body body})
    (try+
      (ws-handler {:uid        nil
                   :req        {:data body}
                   :params     route-params
                   :cbk        (fn [r] (deliver resp r))    ;adapter to sync response
                   :chsk-send! (fn [uid r] (deliver resp (:data (r 1)))) ;adapter to async response
                   })                                       ;supposed to be in format [:srv/api {.. :data <payload> ..}]
      (let [json (json/write-str @resp)]
        (log/info {:msg "web/rsp" :body json})
        {:status 200
         :body   json})
      (catch Throwable t
        (log/error {:err t})
        {:status 500
         :err    (.getMessage t)}))))

(defn web-routes [ring-ajax-get-or-ws-handshake ring-ajax-post]
  ["/" {(bidi/alts
          ""
          "index"
          "index.html")            show-index-page
        "notfound"                 show-page-notfound

        ;Sente paths to service Ajax or WebSocket requests
        :get                       {"chsk" ring-ajax-get-or-ws-handshake}
        :post                      {"chsk" ring-ajax-post}

        ;here we inject the same handlers as we use over WebSockets to provide REST api
        ["v1" [#"[\w%\/]+" :path]] api-handler

        ;redirect to /notfound by default
        true                       (bidi.ring/->Redirect 302 "/notfound")}])