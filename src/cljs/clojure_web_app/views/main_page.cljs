(ns clojure-web-app.views.main-page
  (:require [clojure-web-app.views.menu :as menu]
            [reagent-modals.modals :as modals]))

(defn show-main-page [show-body]
  [:div
   [modals/modal-window]
   (menu/show-menu)
   [:div.container [:div.row [:div.col-lg-12 (show-body)]]]])