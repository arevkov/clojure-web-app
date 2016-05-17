(ns clojure-web-app.ws.sente_factory
  (:require [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.immutant :refer (sente-web-server-adapter)]
            [clojure.tools.logging :as log]
            [clojure-web-app.ws.sente-event-handler :refer [event-msg-handler]]))

;;;; Define Sente channel socket (chsk) server

(defn user-id-fn [req]
  "generates unique ID for request"
  (let [uid (get-in req [:cookies "JSESSIONID" :value])]
    (log/info {:msg "connected" :addr (:remote-addr req) :uid uid})
    uid))

(let [{:keys [ch-recv send-fn ajax-post-fn ajax-get-or-ws-handshake-fn
              connected-uids]}
      (sente/make-channel-socket! sente-web-server-adapter {:packer     :edn
                                                            :user-id-fn user-id-fn})]
  (def ring-ajax-post ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk ch-recv)                                     ; ChannelSocket's receive channel
  (def chsk-send! send-fn)                                  ; ChannelSocket's send API fn
  (def connected-uids connected-uids))                      ; Watchable, read-only atom

;;;; Sente event router

(defonce router_ (atom nil))
(defn stop-router! [] (when-let [stop-f @router_] (stop-f)))
(defn start-router! []
  (stop-router!)
  (reset! router_
          (sente/start-server-chsk-router!
            ch-chsk event-msg-handler)))
