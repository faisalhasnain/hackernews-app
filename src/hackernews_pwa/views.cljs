(ns hackernews-pwa.views
  (:require
   [re-frame.core :as re-frame]
   [hackernews-pwa.events :as events]
   [hackernews-pwa.subs :as subs]))

(def tabs {:top "Top" :new "New" :ask "Ask" :show "Show" :jobs "Jobs"})

(defn main-panel []
  (let [loading @(re-frame/subscribe [::subs/get-db :loading])
        tab @(re-frame/subscribe [::subs/get-db :tab])]
    [:div.pageÍ
     [:div.app-title
      [:div.title "Hacker News"]]
     [:div.tabs
      [:ul
       (map (fn [[key val]] ^{:key key} [:li {:class (if (= tab key) "is-active") :on-click #(re-frame/dispatch [::events/change-db :tab key])} [:a val]]) tabs)]]
     (if loading
       [:div.loading-container
        [:button {:class "button is-large is-loading loading-indicator"}]]
       [:div
        [:h1 "Hello World"]])]))
