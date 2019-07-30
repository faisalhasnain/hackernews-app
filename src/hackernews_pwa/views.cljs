(ns hackernews-pwa.views
  (:require
   [re-frame.core :as re-frame]
   [hackernews-pwa.icons :as icons]))

(def tabs {:top "Top" :new "New" :ask "Ask" :show "Show" :jobs "Jobs"})

(defn post-item [{:keys [id title url points user time_ago domain comments_count]}]
  ^{:key id} [:div.box.post-item
              [:div.post-link
               icons/external-link
               [:a {:href url}
                [:div.subtitle title]]]
              [:div.post-stats
               [:span icons/thumbs-up points] [:span icons/message-square comments_count] [:span icons/clock time_ago] [:span icons/user user]]])

(defn main-panel []
  (let [loading @(re-frame/subscribe [:get-db :loading])
        tab @(re-frame/subscribe [:get-db :tab])]
    [:div.pageÍ
     [:div.app-title
      [:div.title "Hacker News"]]
     [:div.tabs
      [:ul
       (map (fn [[key val]] ^{:key key} [:li {:class (if (= tab key) "is-active") :on-click #(re-frame/dispatch [:fetch-posts key])} [:a val]]) tabs)]]
     (if loading
       [:div.loading-container
        [:button {:class "button is-large is-loading loading-indicator"}]]
       (let [posts @(re-frame/subscribe [:get-db :posts])]
         [:div.container.is-fluid
          (map post-item posts)]))]))
