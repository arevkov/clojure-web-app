(ns clojure-web-app.db
  (:require [clojure.edn :as edn]
            [clojure.java.jdbc :as jdbc]
            [jdbc.pool.c3p0 :as pool]
            [hugsql.core :as hugsql]
            [mount.core :refer [defstate]]
            [clojure-web-app.utils :refer [read-edn]]))

(def config-file (str (System/getProperty "user.dir") "/conf/db-conf.edn"))

(def dev-spec (read-edn config-file))

(defn- connect! [conf] (pool/make-datasource-spec conf))

(defstate ^:dynamic *pool*
          :start (connect! dev-spec)
          :stop (.close (:datasource *pool*)))

(hugsql/def-db-fns "hugsql_crud.sql")