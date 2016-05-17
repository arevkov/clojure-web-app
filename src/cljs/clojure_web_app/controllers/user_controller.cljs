(ns clojure-web-app.controllers.user-controller
  (:require [clojure-web-app.controllers.common :refer [add-item del-item upd-item]]
            [clojure-web-app.views.user-form :refer [show-form]]
            [clojure-web-app.views.user-table :refer [show-panel]]))

(declare show-user-table)

(defonce api-prefix "/api/user/")

(defn show-name [item]
  (str (:first_name item) " " (:last_name item)))

(defn refresh-model
  [chsk-send!]
  (chsk-send! [:srv/api {:method :get :path (str api-prefix "m")}]))

(defn show-user-form
  [render! set-loc! title item on-save]
  (let [on-cancel (partial set-loc! show-user-table)]
    (render! (partial show-form title item on-save on-cancel))))

(defn show-user-add
  [{:keys [query-params render! set-loc! chsk-send!]}]
  (let [on-success (fn [resp] (set-loc! show-user-table))
        on-ok      (partial add-item chsk-send! on-success api-prefix)]
    (show-user-form render! set-loc! "New Entity" {} on-ok)))

(defn show-user-table
  [{:keys [render! set-loc! chsk-send!]}]
  (let [on-success-delete (partial refresh-model chsk-send!)
        on-delete         (partial del-item chsk-send! on-success-delete show-name api-prefix :id)
        on-add            (partial set-loc! show-user-add)]
    (render! (partial show-panel on-delete on-add)))
  (refresh-model chsk-send!))

(defn show-user-update
  [{:keys [query-params chsk-send! render! set-loc!]}]
  (let [{:keys [id]} query-params]
    (chsk-send!
      [:srv/api {:method :get :path (str api-prefix id)}]   ; Event
      2000                                                  ; Timeout
      (fn [reply] (let [on-success (fn [resp] (set-loc! show-user-table))
                        on-ok      (partial upd-item chsk-send! on-success api-prefix :id)]
                    (show-user-form render! set-loc! (str "Edit '" (show-name reply) "'") reply on-ok))))))
