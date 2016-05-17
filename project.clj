(defproject clojure-web-app "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/core.async "0.2.374"]
                 [slingshot "0.12.2"]                       ;enchanced try-catch
                 [mount "0.1.8"]                            ;state management(allows the app to be reloadable)
                 [hiccup "1.0.5"]                           ;template engine

                 ;WEB
                 [bidi "2.0.4"]                             ;routing
                 [ring "1.4.0" :exclusions [ring/ring-jetty-adapter]] ;web abstraction layer
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-codec "1.0.0"]
                 [bk/ring-gzip "0.1.1"]
                 [org.immutant/web "2.1.3"]                 ;high performance app server

                 ;DAO
                 [com.layerware/hugsql "0.4.3"]             ;DSL to iteract with RDBMS
                 [org.postgresql/postgresql "9.4.1208.jre7"] ;jdbc driver to PostgreSQL
                 [org.clojure/java.jdbc "0.5.0"]            ;jdbc abstraction layer for Clojure
                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.1"]   ;connection pool

                 ;LOGGING
                 [org.clojure/tools.logging "0.3.1"]
                 [ch.qos.logback/logback-classic "1.1.3"]

                 ;UI
                 [org.clojure/clojurescript "1.7.228"]
                 [reagent "0.5.1"]                          ;wrapper upon React: V part of MVC
                 [reagent-forms "0.5.13"]                   ;data binding: reagent atom and html components
                 [reagent-utils "0.1.5"]                    ;sessions, cookies, and validation in reagent
                 [org.clojars.frozenlock/reagent-modals "0.2.5"]
                 [com.taoensso/sente "1.8.1"]               ;layer over WebSockets & AJAX
                 ]

  :plugins [[lein-cljsbuild "1.1.1"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj", "src/cljs"]

  :test-paths ["test/clj"]

  :clean-targets ^{:protect false} [:target-path :compile-path "resources/public/js/compiled"]

  :uberjar-name "clojure_web_app.jar"

  :main ^:skip-aot clojure-web-app.main

  ;; nREPL by default starts in the :main namespace, we want to start in `user`
  ;; because that's where our development helper functions like (run) and
  ;; (browser-repl) live.
  :repl-options {:init-ns user}

  :cljsbuild {:builds [
                       ;lein cljsbuild once prod
                       {:id           "prod"
                        :source-paths ["src/cljs"]
                        :compiler     {:main          clojure-web-app.client
                                       :asset-path    "js/compiled/prod"
                                       :output-to     "resources/public/js/compiled/clojure-web-app.js"
                                       :output-dir    "resources/public/js/compiled/prod"
                                       :optimizations :advanced
                                       :externs       ["externs/extern-moment.js"]
                                       :pretty-print  false}}

                       ;lein cljsbuild once dev
                       {:id           "dev"
                        :source-paths ["src/cljs"]
                        :figwheel     true
                        :compiler     {:main                 clojure-web-app.client
                                       :asset-path           "js/compiled/dev"
                                       :output-to            "resources/public/js/compiled/clojure-web-app.js"
                                       :output-dir           "resources/public/js/compiled/dev"
                                       :source-map-timestamp true
                                       :optimizations        :none
                                       :pretty-print         true}}
                       ]}

  :figwheel {;; :http-server-root "public"       ;; serve static assets from resources/public/
             ;; :server-port 3449                ;; default
             ;; :server-ip "127.0.0.1"           ;; default
             :css-dirs       ["resources/public/css"]       ;; watch and update CSS
             ;; :ring-handler user/http-handler
             ;; :nrepl-port 7888
             ;; :open-file-command "myfile-opener"
             :server-logfile "log/figwheel.log"}

  :profiles {;; activated by default
             :dev     {:source-paths ["src/clj", "src/cljs", "dev"]
                       :dependencies [[figwheel "0.5.1"]
                                      [figwheel-sidecar "0.5.1"]
                                      [com.cemerick/piggieback "0.2.1"]
                                      [org.clojure/tools.nrepl "0.2.12"]]
                       :plugins      [[lein-figwheel "0.5.1"]]}
             ;; activated automatically during uberjar
             :uberjar {:env         {:production true}
                       :omit-source true
                       :aot         :all
                       :prep-tasks  ["javac" "compile" ["cljsbuild" "once" "prod"]]}
             ;; activated automatically in repl task
             :repl    {:prep-tasks ["javac" "compile"]}
             })
