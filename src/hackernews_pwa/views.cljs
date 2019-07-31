(ns hackernews-pwa.views
  (:require
   [re-frame.core :as re-frame]
   [hackernews-pwa.icons :as icons]))

(def tabs {:top "Top" :new "New" :ask "Ask" :show "Show" :jobs "Jobs"})

(defn post-item [{:keys [id title url domain points user time_ago domain comments_count]}]
  (let [tab @(re-frame/subscribe [:get-db :tab])]
    ^{:key id} [:div.box.post-item
                [:div.post-link
                 (if (not= tab :ask) icons/external-link)
                 [:a {:href url :target "_blank"}
                  [:div [:span.subtitle title] (if domain [:span.domain (str " (" domain ")")])]]]
                [:div.post-stats
                 (if points [:span icons/thumbs-up points]) [:span icons/message-square comments_count] [:span icons/clock time_ago] (if user [:span icons/user user])]]))

(defn navigation []
  (let [tab @(re-frame/subscribe [:get-db :tab])]
    [:nav.navbar.is-info.is-fixed-top
     {:aria-label "main navigation", :role "navigation"}
     [:div.navbar-brand
      [:a.navbar-item
       {:href "/"}
       [:span.title.has-text-white "Hacker News"]]
      [:a.navbar-burger.burger
       {:data-target "navbarBasicExample"
        :aria-expanded "false"
        :aria-label "menu"
        :role "button"}
       [:span {:aria-hidden "true"}]
       [:span {:aria-hidden "true"}]
       [:span {:aria-hidden "true"}]]]
     [:div#navbarBasicExample.navbar-menu
      [:div.navbar-start
       (doall (map (fn [[key val]] ^{:key key} [:a.navbar-item {:class (if (= tab key) "is-active") :on-click #(re-frame/dispatch [:fetch-posts key])} val]) tabs))]]]))

(defn main-panel []
  (let [loading @(re-frame/subscribe [:get-db :loading])]
    [:div.page
     (navigation)
     (if loading
       [:div.loading-container
        [:button {:class "button is-large is-loading loading-indicator"}]]
       (let [posts @(re-frame/subscribe [:get-db :posts])]
         [:div.container.is-fluid
          (doall (map post-item posts))]))]))
