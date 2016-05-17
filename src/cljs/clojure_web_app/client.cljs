(ns clojure_web_app.client
  (:require-macros
    [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require [reagent.core :as r]
            [cljs.core.async :as async :refer (<! >! put! chan)]
            [taoensso.timbre :refer-macros (tracef debugf infof warnf errorf)]
            [taoensso.sente :as sente :refer (cb-success?)]
            [clojure-web-app.ws.sente-event-handler :refer [start-ws-router!]]
            [clojure-web-app.controllers.browser-routes :refer [init-browser-routes]]))

;;;; Define Sente channel socket (chsk) client

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket! "/chsk"                   ; Note the same path as before
                                  {:type   :auto            ; e/o #{:auto :ajax :ws}
                                   :packer :edn})]
  (def chsk chsk)
  (def ch-chsk ch-recv)                                     ; ChannelSocket's receive channel
  (def chsk-send! send-fn)                                  ; ChannelSocket's send API fn
  (def chsk-state state))                                   ; Watchable, read-only atom

;;;; Init stuff

(.log js/console "Starting..")

(enable-console-print!)

(init-browser-routes chsk-send!)

(start-ws-router! ch-chsk)