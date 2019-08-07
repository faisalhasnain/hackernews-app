(ns hackernews-app.views
  (:require
   [re-frame.core :as re-frame]
   [hackernews-app.icons :as icons]))

(def tabs {:top "Top" :new "New" :ask "Ask" :show "Show" :jobs "Jobs"})

(def page-counts {:top 10 :new 12 :ask 2 :show 2 :jobs 1})

(defn navigation [params]
  (let [tab (-> params :parameters :path :tab)
        show-navbar-menu @(re-frame/subscribe [:get-db :show-navbar-menu])]
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
        :role "button"
        :class (if show-navbar-menu "is-active")
        :on-click #(re-frame/dispatch [:toggle-navbar-menu])}
       [:span {:aria-hidden "true"}]
       [:span {:aria-hidden "true"}]
       [:span {:aria-hidden "true"}]]]
     [:div#navbar.navbar-menu {:class (if show-navbar-menu "is-active")}
      [:div.navbar-start
       (for [[key val] tabs]
         ^{:key key} [:a.navbar-item {:class (if (= tab key) "is-active") :on-click #(re-frame/dispatch [:navigate :posts {:tab key}])} val])]]]))

(defn pagination [tab page]
  (if (> (tab page-counts) 1)
    [:nav.pagination.is-rounded
     {:aria-label "pagination", :role "navigation"}
     [:ul.pagination-list
      (for [page-num (range 1 (inc (tab page-counts)))]
        ^{:key page-num} [:li [:a.pagination-link {:class (if (= page-num page) "is-current") :on-click #(re-frame/dispatch [:navigate :posts {:tab tab} {:page page-num}])} page-num]])]]))

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
  (let [posts @(re-frame/subscribe [:get-db :posts])
        tab (get-in params [:parameters :path :tab])
        page (get-in params [:parameters :query :page] 1)]
    [:section.section
     [:div.container
      (for [post posts]
        ^{:key (:id post)} [render-post post])
      [pagination tab page]]]))

(defn render-comments [params]
  (let [item @(re-frame/subscribe [:get-db :comments])
        comments (:comments item)]
    [:section.section
     [:div.container
      ^{:key (:id item)} [render-post (-> item
                                          (dissoc item :comments)
                                          (assoc item :comments_count (count comments)))]
      (for [comment comments]
        ^{:key (:id comment)} [render-comment comment])]]))

(defn footer []
  [:footer.section
   [:div.container
    [:hr]
    [:p.has-text-centered.is-size-7 "Made with ❤️ by " [:a {:href "https://faisalhasnain.com" :target "_blank"} "Faisal Hasnain"] " using " [:a {:href "https://clojurescript.org" :target "_blank"} "ClojureScript"] ", " [:a {:href "https://reagent-project.github.io" :target "_blank"} "Reagent"] ", " [:a {:href "https://github.com/Day8/re-frame" :target "_blank"} "Re-frame"] " and " [:a {:href "https://bulma.io" :target "_blank"} "Bulma"]]]])

(defn main-panel []
  (let [loading @(re-frame/subscribe [:get-db :loading])
        current-route @(re-frame/subscribe [:get-db :current-route])
        view (-> current-route :data :view)]
    [:div.page
     [navigation current-route]
     (cond
       loading [:div.loading-container
                [:button.button.is-large.is-loading.loading-indicator]]
       view [view current-route])
     [footer]]))

