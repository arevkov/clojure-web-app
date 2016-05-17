(ns user
  (:require [ring.middleware.reload :refer [wrap-reload]]
            [figwheel-sidecar.repl-api :as figwheel]))

;; Let Clojure warn you when it needs to reflect on types, or when it does math
;; on unboxed numbers. In both cases you should add type annotations to prevent
;; degraded performance.
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

;uncomment if you want to run the server with the client
;(def http-handler
;  (wrap-reload #'clojure-web-app.server/app))

(defn run []
  (figwheel/start-figwheel!))

(defn stop []
  (figwheel/stop-figwheel!))

(def browser-repl figwheel/cljs-repl)
