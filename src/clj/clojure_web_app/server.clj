(ns clojure-web-app.server
  (:use [slingshot.slingshot])
  (:require [clojure.tools.logging :as log]
            [clojure.edn :as edn]
            [bidi.bidi :as bidi]
            [bidi.ring]
            [hiccup.core]
            [hiccup.page]
            [mount.core :refer [defstate]]
            [immutant.web]
            [ring.util.response :refer [redirect]]
            [ring.middleware.params]
            [ring.middleware.cookies]
            [ring.middleware.resource]
            [ring.middleware.keyword-params]
            [clojure-web-app.db]
            [clojure-web-app.web-routes :refer [web-routes]]
            [clojure-web-app.ws.sente_factory :as ws]
            [clojure-web-app.utils :refer [read-edn]]))

;;;; Init stuff

(def config-file (str (System/getProperty "user.dir") "/conf/web-conf.edn"))

(def config (read-edn config-file))

(defn start! []
  (ws/start-router!)
  (def app (-> (bidi.ring/make-handler (web-routes ws/ring-ajax-get-or-ws-handshake ws/ring-ajax-post))
               (ring.middleware.resource/wrap-resource "public")
               (ring.middleware.cookies/wrap-cookies)
               (ring.middleware.keyword-params/wrap-keyword-params)
               (ring.middleware.params/wrap-params)
               (immutant.web.middleware/wrap-session {:timeout 300 :secure true})))
  (try
    (immutant.web/run (immutant.web.middleware/wrap-development #'app) config)
    (catch Exception e
      (throw+ {:msg "failed to start the server" :desc (.getMessage e)}))))

(defstate app-server
          :start (start!)
          :stop (immutant.web/stop app-server))