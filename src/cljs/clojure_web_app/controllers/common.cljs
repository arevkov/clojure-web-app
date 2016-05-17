(ns clojure-web-app.controllers.common
  (:require [clojure-web-app.utils :refer [show-alert show-confirm-dialog]]))

(defn handle-response [on-success resp]
  (if (contains? resp :err)
    (show-alert (:err resp))
    (on-success resp)))

(defn add-item [chsk-send! on-success api-path item]
  (chsk-send! [:srv/api {:method :post :path api-path :data item}]
              2000
              (partial handle-response on-success)))

(defn del-item [chsk-send! on-success get-name api-path get-id item]
  (show-confirm-dialog
    (str "Are you sure you want to delete '" (get-name item) "'?")
    #(chsk-send! [:srv/api {:method :delete :path (str api-path (get-id item))}]
                 2000
                 (partial handle-response on-success))))

(defn upd-item [chsk-send! on-success api-path get-id item]
  (chsk-send! [:srv/api {:method :put :path (str api-path (get-id item)) :data item}]
              2000
              (partial handle-response on-success)))
