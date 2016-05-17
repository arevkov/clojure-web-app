(ns clojure-web-app.handlers.user-handler
  (:require [clojure.tools.logging :as log]
            [clojure-web-app.db :as db]))

(defn ^:private validate
  [item]
  ;hugsql requires nil values for optional fields
  ;but reagent-forms doesn't provide value if form field is empty
  (merge {:phone nil :gender nil} item))

(defn list-users [{:keys [uid req chsk-send!]}]
  (let [data (db/list-users db/*pool*)]
    (chsk-send! uid [:srv/api (assoc req :data data)])))

(defn add-user [{:keys [req cbk]}]
  (let [id (db/add-user db/*pool* (-> req :data validate))]
    (cbk id)))

(defn get-user [{:keys [params cbk]}]
  (let [id (read-string (:id params))]
    (cbk (db/get-user db/*pool* {:id id}))))

(defn upd-user [{:keys [params cbk req]}]
  (assert (= (get-in req [:data :id]) (read-string (:id params))) "ID from payload doesn't match to ID of query string")
  (cbk (db/update-user db/*pool* (-> req :data validate))))

(defn del-user [{:keys [params cbk]}]
  (let [id (read-string (:id params))]
    (cbk (db/delete-user db/*pool* {:id id}))))
