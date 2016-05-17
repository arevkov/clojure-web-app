(ns clojure-web-app.ws.ws-routes
  (:require [clojure-web-app.handlers.user-handler :refer :all]))

(def routes ["/api/" {"user/" {""    {:post add-user}
                               "m"   list-users
                               [:id] {:get    get-user
                                      :put    upd-user
                                      :delete del-user}}}
             ;add new handler here
             ;it will be available both over Websockets and REST api
             ])