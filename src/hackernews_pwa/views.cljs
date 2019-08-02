(ns hackernews-pwa.views
  (:require
   [re-frame.core :as re-frame]
   [reitit.core :as reitit-core]
   [reitit.frontend :as reitit-frontend]
   [reitit.frontend.easy :as reitit-frontend-easy]
   [reitit.frontend.controllers :as reitit-frontend-controllers]
   [reitit.coercion.spec :as rss]
   [hackernews-pwa.icons :as icons]))

(def tabs {:top "Top" :new "New" :ask "Ask" :show "Show" :jobs "Jobs"})

; routing utils

(re-frame/reg-fx
 :navigate!
 (fn [[k params query]]
   (reitit-frontend-easy/push-state k params query)))

(re-frame/reg-event-fx
 :navigate
 (fn [db [_ route params query]]
   {:navigate! [route params query]}))

(re-frame/reg-event-db
 :navigated
 (fn [db [_ new-match]]
   (let [old-match   (:current-route db)
         controllers (reitit-frontend-controllers/apply-controllers (:controllers old-match) new-match)]
     (assoc db :current-route (assoc new-match :controllers controllers)))))

(defn on-navigate [new-match]
  (when new-match
    (re-frame/dispatch [:navigated new-match])))

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
       (doall (map (fn [[key val]] ^{:key key} [:a.navbar-item {:class (if (= tab key) "is-active") :on-click #(re-frame/dispatch [:navigate :posts {:tab key}])} val]) tabs))]]]))

(defn render-post [{:keys [id title url domain points user time_ago domain comments_count]}]
  (let [current-route @(re-frame/subscribe [:get-db :current-route])
        tab (-> current-route :parameters :path :tab)]
    ^{:key id} [:div.box.post-item
                [:div.post-link
                 (if (not= tab :ask) icons/external-link)
                 [:a {:href url :target "_blank"}
                  [:div [:span.subtitle title] (if domain [:span.domain (str " (" domain ")")])]]]
                [:div.post-stats
                 (if points [:span icons/thumbs-up points]) [:a {:on-click #(re-frame/dispatch [:navigate :comments {:tab tab :id id}])} icons/message-square comments_count] [:span icons/clock time_ago] (if user [:span icons/user user])]]))


(defn render-comment [{:keys [id content user time_ago comments depth]}]
  [:<> ^{:key id} [:div.box.post-item {:style {:margin-left (str (* (or depth 0) 1.5) "rem")}}
                   [:div.post-contents {:dangerouslySetInnerHTML {"__html" content}}]
                   [:div.post-stats
                    [:span icons/user user] [:span icons/clock time_ago]]]
   (if comments (->> comments (map #(assoc % :depth (inc depth))) (map render-comment)))])

(defn render-posts [params]
  (let [posts @(re-frame/subscribe [:get-db :posts])]
    [:div.container.is-fluid
     (doall (map render-post posts))]))

(defn render-comments [params]
  (let [item @(re-frame/subscribe [:get-db :comments])
        comments (:comments item)]
    [:div.container.is-fluid
     (render-post (-> item
                      (dissoc item :comments)
                      (assoc item :comments_count (count comments))))
     (doall (map render-comment comments))]))


(def routes
  [["/:tab"
    {:name      :posts
     :view      render-posts
     :parameters {:path {:tab keyword?}}
     :controllers
     [{:parameters {:path [:tab]} :start (fn [{{tab :tab} :path}] (re-frame/dispatch [:fetch-posts tab]))}]}]
   ["/:tab/:id"
    {:name      :comments
     :view      render-comments
     :parameters {:path {:tab keyword? :id string?}}
     :controllers
     [{:parameters {:path [:tab :id]} :start (fn [{{tab :tab id :id} :path}] (re-frame/dispatch [:fetch-comments id]))}]}]])

(def router (reitit-frontend/router routes {:data {:coercion rss/coercion}}))

(defn init-routes! []
  (reitit-frontend-easy/start!
   router
   on-navigate
   {:use-fragment true}))

(defn main-panel []
  (let [loading @(re-frame/subscribe [:get-db :loading])
        current-route @(re-frame/subscribe [:get-db :current-route])]
    [:div.page
     [navigation current-route]
     (cond
       loading [:div.loading-container
                [:button.button.is-large.is-loading.loading-indicator]]
       current-route [(-> current-route :data :view) current-route])]))

