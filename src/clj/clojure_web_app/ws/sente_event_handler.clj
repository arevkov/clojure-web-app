(ns clojure-web-app.ws.sente-event-handler
  (:use [slingshot.slingshot :only [throw+ try+]])
  (:require [clojure.tools.logging :as log]
            [bidi.bidi :as bidi]
            [clojure-web-app.ws.ws-routes :refer [routes]])
  (:import (clojure.lang ExceptionInfo)))

(defn- async-log-wrapper [send-fn uid resp]
  (let [[_ {:keys [data]}] resp]
    (log/info {:msg "ws/sent" :data data :uid uid})
    (send-fn uid resp)))

(defn- sync-log-wrapper [reply-fn uid data]
  (log/info {:msg "ws/sent" :data data :uid uid})
  (when (-> reply-fn nil? not) (reply-fn data)))

;;;; Sente event handlers

(defmulti -event-msg-handler
          "Multimethod to handle Sente `event-msg`s"
          :id)                                              ; Dispatch on event-id

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg}]
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler
  :srv/api
  [{:as msg :keys [event ring-req ?reply-fn send-fn]}]
  (let [req     (event 1)
        uid     (get-in ring-req [:cookies "JSESSIONID" :value])
        route   (bidi/match-route* routes (:path req) {:request-method (:method req)})
        handler (:handler route)
        params  (:route-params route)]
    (log/info {:msg "ws/rcvd" :req req :uid uid})
    (let [callback (partial sync-log-wrapper ?reply-fn uid)]
      (try+
        (handler {:uid uid :req req :params params :cbk callback :chsk-send! (partial async-log-wrapper send-fn)})
        (catch Throwable t
          (let [err &throw-context]
            (log/error :err err)
            (callback {:err (-> err :object .toString)})))
        (catch Object o
          (let [err &throw-context]
            (log/error :err err)
            (callback {:err (:message err)})))))
    ))

(defmethod -event-msg-handler
  :chsk/ws-ping
  [ev-msg] ())

(defmethod -event-msg-handler
  :chsk/uidport-open
  [ev-msg] ())

(defmethod -event-msg-handler
  :default                                                  ; Default/fallback case (no other matching handler)
  [{:keys [event]}]
  (log/warn {:msg "unhandled event" :type event}))
