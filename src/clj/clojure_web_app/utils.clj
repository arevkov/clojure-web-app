(ns clojure-web-app.utils
  (:require [clojure.edn :as edn])
  (:import (java.io PushbackReader)))

(defn read-edn [file]
  (edn/read (PushbackReader. (clojure.java.io/reader file))))