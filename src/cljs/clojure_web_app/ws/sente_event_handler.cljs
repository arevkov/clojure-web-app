(ns clojure-web-app.ws.sente-event-handler
  (:require [taoensso.sente :as sente]
            [clojure-web-app.ws.client-ws-routes :refer [handle-ws-msg]]
            [clojure-web-app.controllers.browser-routes :refer [set-loc!]]))

;;;; Sente event handlers

(defmulti -event-msg-handler
          "Multimethod to handle Sente `event-msg`s"
          :id)                                              ; Dispatch on event-id

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg}]
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler :chsk/recv
  [{:as ev-msg :keys [?data]}]
  (let [payload (?data 1)]
    (.log js/console (str "rsvd: " payload))
    (handle-ws-msg payload)))

(defmethod -event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (.log js/console "Channel socket successfully established!")
  (.log js/console (str "Channel socket state change: " ?data))
  (when (:first-open? ?data) (set-loc! "none")))            ; Sets location to nonexisting handler to make request to backend

(defmethod -event-msg-handler
  :default                                                  ; Default/fallback case (no other matching handler)
  [{:as ev-msg :keys [event]}]
  (.log js/console (str "Unhandled event: " event)))

;;;; Sente event router (our `event-msg-handler` loop)

(defonce router_ (atom nil))
(defn stop-ws-router! [] (when-let [stop-f @router_] (stop-f)))
(defn start-ws-router! [ch-chsk]
  (stop-ws-router!)
  (.log js/console "init ws-sente-ctrl..")
  (reset! router_
          (sente/start-client-chsk-router!
            ch-chsk event-msg-handler)))