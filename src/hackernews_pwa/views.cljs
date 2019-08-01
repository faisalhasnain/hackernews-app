(ns hackernews-pwa.views
  (:require
   [re-frame.core :as re-frame]
   [hackernews-pwa.icons :as icons]))

(def tabs {:top "Top" :new "New" :ask "Ask" :show "Show" :jobs "Jobs"})

(defn navigation []
  (let [tab @(re-frame/subscribe [:get-db :tab])]
    [:nav.navbar.is-info.is-fixed-top
     {:aria-label "main navigation", :role "navigation"}
     [:div.navbar-brand
      [:a.navbar-item
       {:on-click #(re-frame/dispatch [:fetch-posts :top])}
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
       (doall (map (fn [[key val]] ^{:key key} [:a.navbar-item {:class (if (= tab key) "is-active") :on-click #(re-frame/dispatch [:fetch-posts key])} val]) tabs))]]]))

(defn render-post [{:keys [id title url domain points user time_ago domain comments_count]}]
  (let [tab @(re-frame/subscribe [:get-db :tab])]
    ^{:key id} [:div.box.post-item
                [:div.post-link
                 (if (not= tab :ask) icons/external-link)
                 [:a {:href url :target "_blank"}
                  [:div [:span.subtitle title] (if domain [:span.domain (str " (" domain ")")])]]]
                [:div.post-stats
                 (if points [:span icons/thumbs-up points]) [:a {:on-click #(re-frame/dispatch [:fetch-comments id])} icons/message-square comments_count] [:span icons/clock time_ago] (if user [:span icons/user user])]]))


(defn render-comments [{:keys [id content user time_ago comments depth]}]
  [:<> ^{:key id} [:div.box.post-item {:style {:margin-left (str (* (or depth 0) 1.5) "rem")}}
                   [:div.post-contents {:dangerouslySetInnerHTML {"__html" content}}]
                   [:div.post-stats
                    [:span icons/user user] [:span icons/clock time_ago]]]
   (if comments (->> comments (map #(assoc % :depth (inc depth))) (map render-comments)))])

(defn main-panel []
  (let [loading @(re-frame/subscribe [:get-db :loading])
        type @(re-frame/subscribe [:get-db :type])]
    [:div.page
     (navigation)
     (cond
       loading [:div.loading-container
                [:button {:class "button is-large is-loading loading-indicator"}]]
       (= type :posts) (let [posts @(re-frame/subscribe [:get-db :posts])]
                         [:div.container.is-fluid
                          (doall (map render-post posts))])
       (= type :comments) (let [item @(re-frame/subscribe [:get-db :comments])
                                comments (:comments item)]
                            [:div.container.is-fluid
                             (render-post (-> item
                                              (dissoc item :comments)
                                              (assoc item :comments_count (count comments))))
                             (doall (map render-comments comments))]))]))

