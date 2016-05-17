(ns clojure-web-app.controllers.browser-routes
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [bidi.bidi :as bidi]
            [bidi.router :as br]
            [reagent.core :as r]
            [reagent-modals.modals :as modals]
            [cljs.core.async :refer [<! timeout]]
            [clojure-web-app.utils :refer [show-alert show-confirm-dialog]]
            [clojure-web-app.views.main-page :refer [show-main-page]]
            [clojure-web-app.controllers.user-controller :as users]))

(declare chsk-send!)
(declare set-loc!)
(declare show-wf-pnl)
(declare show-entity-pnl)
(declare show-param-pnl)
(declare show-stat-pnl)
(declare refresh-stat)

(defonce app (.getElementById js/document "app"))

(defn render [show-body]
  (r/render (partial show-main-page show-body) app))

;;;; browser history router

(defn init-browser-routes [_chsk-send!]
  (.log js/console "init browser-ctrl..")

  (def chsk-send! _chsk-send!)

  (defn on-navigate [req]
    (let [handler (:handler req)
          params  (:route-params req)]
      (when (fn? handler) (handler {:query-params params
                                    :set-loc!     set-loc!  ;browser location
                                    :chsk-send!   chsk-send! ;communication w/ server
                                    :render!      render    ;DOM
                                    }))))

  (defonce _hist-router
           (br/start-router!
             ["/" {"user/" {""    users/show-user-add
                            "m"   users/show-user-table
                            [:id] users/show-user-update}
                   ;add new handlers here
                   }]
             {:on-navigate      on-navigate
              :default-location {:handler users/show-user-table}}))

  (defn set-loc! [path]
    (br/set-location! _hist-router {:handler path})))