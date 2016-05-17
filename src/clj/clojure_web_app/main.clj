(ns clojure-web-app.main
  (:gen-class)
  (:require [mount.core]
            [clojure.tools.logging :as log]
            [clojure-web-app.server :refer [app-server]]))

(defn set-global-exception-hook
  "set global exception hook.
  here we catch any uncaught exceptions and log it."
  []
  (Thread/setDefaultUncaughtExceptionHandler
    (reify Thread$UncaughtExceptionHandler
      (uncaughtException [_ thread ex]
        (log/error {:msg "Uncaught exception" :thread (.getName thread) :descr ex})))))

;;;; mount's state control

(defn start-app
  "start application"
  []
  (do
    (log/info {:msg "start service.."})
    (set-global-exception-hook)
    (mount.core/start)))

(defn stop-app
  "stop application"
  []
  (do
    (log/info {:msg "stop service.."})
    (mount.core/stop)))

;;;; entry point

(defn -main
  "entry point to application"
  [& args]
  (start-app))
