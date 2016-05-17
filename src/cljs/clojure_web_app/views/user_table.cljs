(ns clojure-web-app.views.user-table
  (:require [reagent-forms.core :refer [bind-fields]]
            [clojure-web-app.models.global :refer [user-list]]
            [clojure-web-app.utils :refer [show-edit-delete show-table fmt-date-time]]))

(defn- show-table_ [on-delete]
  (when @user-list
    (show-table user-list
                      [:id :first_name :last_name :gender :email :phone #(-> % :modify_date fmt-date-time)]
                      '("ID" "FirstName" "LastName" "Gender" "Email" "Phone" "ModifyDate")
                      (partial show-edit-delete #(str "#/user/" (:id %)) on-delete))))

(defn show-panel [on-delete on-add-entity]
  [:div [:div [(partial show-table_ on-delete)]] [:button.btn.btn-primary {:type "button" :on-click on-add-entity} "Add User"]])