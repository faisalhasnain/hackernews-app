(ns hackernews-pwa.views
  (:require
   [re-frame.core :as re-frame]
   [hackernews-pwa.subs :as subs]))

(defn main-panel []
  (let [loading (re-frame/subscribe [::subs/loading])]
    [:div.page
     [:div.app-title
      [:div.title "Hacker News"]]
     [:div.tabs
      [:ul
       [:li.is-active [:a "Top"]]
       [:li [:a "New"]]
       [:li [:a "Ask"]]
       [:li [:a "Show"]]
       [:li [:a "Jobs"]]]]
     (if loading
       [:div.loading-container
        [:button {:class "button is-large is-loading loading-indicator"}]]
       [:div
        [:h1 "Hello World"]])]))
