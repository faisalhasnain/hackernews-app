(ns hackernews-app.views
  (:require
   [re-frame.core :as re-frame]
   [hackernews-app.icons :as icons]))

(def tabs {:top "Top" :new "New" :ask "Ask" :show "Show" :jobs "Jobs"})

(defn navigation [params]
  (let [tab (-> params :parameters :path :tab)]
    [:nav.navbar.is-info.is-fixed-top
     {:aria-label "main navigation", :role "navigation"}
     [:div.navbar-brand
      [:a.navbar-item
       {:on-click #(re-frame/dispatch [:navigate :posts {:tab :top}])}
       [:span.title.has-text-white "Hacker News"]]
      [:a.navbar-burger.burger
       {:data-target "navbar"
        :aria-expanded "false"
        :aria-label "menu"
        :role "button"}
       [:span {:aria-hidden "true"}]
       [:span {:aria-hidden "true"}]
       [:span {:aria-hidden "true"}]]]
     [:div#navbar.navbar-menu
      [:div.navbar-start
       (for [[key val] tabs]
         ^{:key key} [:a.navbar-item {:class (if (= tab key) "is-active") :on-click #(re-frame/dispatch [:navigate :posts {:tab key}])} val])]]]))

(defn render-post [{:keys [id title url domain points user time_ago domain comments_count]}]
  (let [current-route @(re-frame/subscribe [:get-db :current-route])
        tab (-> current-route :parameters :path :tab)]
    [:div.box.post-item
     [:div.post-link
      (if (not= tab :ask)
        [:a {:href url :target "_blank"}
         icons/external-link [:span.subtitle title] (if domain [:span.domain (str " (" domain ")")])]
        [:a {:on-click #(re-frame/dispatch [:navigate :comments {:tab tab :id id}])}
         [:span.subtitle title] (if domain [:span.domain (str " (" domain ")")])])]
     [:div.post-stats
      (if points [:span {:title "Points"} icons/thumbs-up points]) [:a {:title "Comments" :on-click #(re-frame/dispatch [:navigate :comments {:tab tab :id id}])} icons/message-square comments_count] [:span icons/clock time_ago] (if user [:span {:title "User"} icons/user user])]]))


(defn render-comment [{:keys [id content user time_ago comments depth]}]
  [:<> [:div.box.post-item {:style {:margin-left (str (* (or depth 0) 1.5) "rem")}}
        [:div.post-contents {:dangerouslySetInnerHTML {"__html" content}}]
        [:div.post-stats
         [:span icons/user user] [:span icons/clock time_ago]]]
   (if comments
     (for [comm comments]
       ^{:key (:id comm)} [render-comment (assoc comm :depth (inc depth))]))])

(defn render-posts [params]
  (let [posts @(re-frame/subscribe [:get-db :posts])]
    [:div.container
     (for [post posts]
       ^{:key (:id post)} [render-post post])]))

(defn render-comments [params]
  (let [item @(re-frame/subscribe [:get-db :comments])
        comments (:comments item)]
    [:div.container
     ^{:key (:id item)} [render-post (-> item
                                         (dissoc item :comments)
                                         (assoc item :comments_count (count comments)))]
     (for [comment comments]
       ^{:key (:id comment)} [render-comment comment])]))



(defn main-panel []
  (let [loading @(re-frame/subscribe [:get-db :loading])
        current-route @(re-frame/subscribe [:get-db :current-route])
        view (-> current-route :data :view)]
    [:div.page
     [navigation current-route]
     [:section.section
      (cond
        loading [:div.loading-container
                 [:button.button.is-large.is-loading.loading-indicator]]
        view [view current-route])]]))

