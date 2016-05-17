(ns clojure-web-app.models.global
  (:require [reagent.core :as r]))

;;;; Global Models

(defonce user-list (r/atom (list)))