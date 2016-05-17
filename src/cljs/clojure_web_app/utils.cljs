(ns clojure-web-app.utils
  (:require [reagent-forms.core :refer [bind-fields]]
            [reagent-modals.modals :as modals]
            [reagent.core :as r]))

(defn find-one [coll filter]
  (some #(when (filter %) %) coll))

(defn find-byid [coll id]
  (find-one coll #(= (:id %) id)))

(defn map-keys [m f]
  (into {} (for [[k v] m] [(f k) v])))

(defn diff-by-value [map1 map2]
  (into {} (filter (fn [[k v]] (not= v (k map2))) map1)))

;;;; common UI components

(defn show-radio-group
  "Radio buttons do not use the :id key since it must be unique and are instead grouped using the :name attribute.
  The :value attribute is used to indicate the value that is saved to the document."
  [name label items]
  [:div.form-group [:label label]
   (map (fn [i] [:div.radio [:label [:input {:name name :field :radio :value (:value i)} (:label i)]]]) items)])

(defn show-table [data fields headers show-actions]
  [:table.table.table-bordered
   (when (not-empty headers) [:thead [:tr (map (fn [h] [:th h]) headers) [:th "Actions"]]])
   [:tbody (for [i @data] [:tr (map (fn [fld-fn] [:td (str (fld-fn i))]) fields) [:td (show-actions i)]])]])

(defn show-edit-delete [build-edit-url on-delete item]
  [:div [:a {:href (build-edit-url item)} [:span.glyphicon.glyphicon-wrench {:title "edit"}]] "  "
   [:a {:on-click (partial on-delete item)} [:span.glyphicon.glyphicon-trash {:title "delete"}]]])

(defn show-alert [msg]
  (modals/modal! [:div.dialog
                  [:div [:h3 (str msg)]] [:hr]
                  [:button.btn.btn-primary {:on-click #(modals/close-modal!)} "Ok"]]))

(defn show-confirm-dialog [msg on-ok]
  (let [res (atom false)]
    (modals/modal! [:div.dialog [:h3 msg] [:hr]
                    [:div
                     [:button.btn.btn-primary {:on-click (fn [] (modals/close-modal!) (reset! res true))} "OK"]
                     [:button.btn.btn-primary {:on-click #(modals/close-modal!)} "Cancel"]]]
                   {:size   :lg
                    ;call on-ok in :hidden section cz you might want to show another dialog here
                    ;it may dissapear due to: http://stackoverflow.com/questions/13648979/bootstrap-modal-immediately-disappearing
                    :hidden #(when (true? @res) (on-ok))
                    })))

;;;; date-time formatting

(enable-console-print!)

(defn moment [arg] (.moment js/window arg))

(defn fmt-date-time [dt]
  (if dt (-> dt .getTime moment (.format "YYYY-MM-DD HH:mm")) "-"))