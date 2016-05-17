(ns clojure-web-app.views.menu
  (:require [reagent-modals.modals :as modals]))

(defn show-about []
  [:div.dialog [:h2 "Clojure-web-app ver 0.0.1"] [:hr] [:div "TODO: write something meaningful here!"]])

(defn show-brand-nav []                                     ; Brand and toggle get grouped for better mobile display
  [:div.navbar-header
   [:button.navbar-toggle
    {:type "button" :data-toggle "collapse" :data-target ".navbar-ex1-collapse"}
    [:span.sr-only "Toggle navigation"]
    [:span.icon-bar]
    [:span.icon-bar]
    [:span.icon-bar]]
   [:a.navbar-brand {:href "/"} "Clojure-Web-App"]])

(defn show-nav-links []                                     ; Collect the nav links, forms, and other content for toggling
  [:ul.nav.navbar-nav.top-nav
   [:li [:a {:href "#/user/m"} "Users"]]
   [:li [:a {:on-click #(modals/modal! (show-about) {:size :lg})} "About"]]])

(defn show-top-menu []                                      ; Top Menu Items
  [:ul.nav.navbar-top-links.navbar-right
   [:li.dropdown
    [:a.dropdown-toggle
     {:data-toggle "dropdown", :href "#"}
     [:i.fa.fa-user]
     [:b.caret]]
    [:ul.dropdown-menu
     [:li
      [:a
       {:href "#"}
       [:i.fa.fa-fw.fa-power-off]
       " Log Out"]]]]])

(defn show-menu []
  [:nav.navbar.navbar-default.navbar-static-top
   {:role "navigation"} (show-brand-nav) (show-nav-links) #_(show-top-menu)])