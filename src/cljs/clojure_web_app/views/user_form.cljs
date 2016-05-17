(ns clojure-web-app.views.user-form
  (:require [reagent-forms.core :refer [bind-fields]]
            [reagent.core :as r]
            [clojure-web-app.utils :refer [show-radio-group]]))

(def ^:private form-template
  [:div
   [:div.form-group [:label "First Name"] [:input.form-control {:field :text :id :first_name}]]
   [:div.form-group [:label "Last Name"] [:input.form-control {:field :text :id :last_name}]]
   [:div.form-group [:label "Email"] [:input.form-control {:field :text :id :email}]]
   [:div.form-group [:label "Phone"] [:input.form-control {:field :text :id :phone :placeholder "optional"}]]
   (show-radio-group :gender "Gender" '({:value "m" :label "Male"}
                                         {:value "f" :label "Female"}))])

(defn show-form [title data on-save on-cancel]
  (let [entity (r/atom data)]
    [:div
     [:div.row.col-lg-12 [:h1.page-header title]]
     [:div.col-lg-8
      [:form {:role "form"}
       [bind-fields form-template entity]
       [:button.btn.btn-primary {:type "button" :on-click (fn [] (on-save @entity))} "Save"]
       [:button.btn.btn-primary {:type "button" :on-click on-cancel} "Cancel"]]]]))