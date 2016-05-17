(ns clojure-web-app.ws.client-ws-routes
  (:require [bidi.bidi :as bidi]
            [clojure-web-app.models.global :refer [user-list]]))

;;;; WS controllers

(def ^:private ws-api-routes ["/api/" {"user/m" #(reset! user-list (sort-by :id %))
                                       ;put here new handlers
                                       }])

(defn handle-ws-msg
  [{:keys [method path data]}]
  (let [handler (:handler (bidi/match-route* ws-api-routes path {:request-method method}))]
    (handler data)))