(ns hackernews-pwa.routing
  (:require
   [re-frame.core :as re-frame]
   [reitit.core :as reitit-core]
   [reitit.frontend :as reitit-frontend]
   [reitit.frontend.easy :as reitit-frontend-easy]
   [reitit.frontend.controllers :as reitit-frontend-controllers]
   [reitit.coercion.spec :as reitit-coercion-spec]
   [hackernews-pwa.views :as views]))

; routing utils

(re-frame/reg-event-fx
 :navigate
 (fn [db [_ route params query]]
   {:navigate! [route params query]}))

(re-frame/reg-fx
 :navigate!
 (fn [[k params query]]
   (reitit-frontend-easy/push-state k params query)))

(re-frame/reg-event-db
 :navigated
 (fn [db [_ new-match]]
   (let [old-match   (:current-route db)
         controllers (reitit-frontend-controllers/apply-controllers (:controllers old-match) new-match)]
     (assoc db :current-route (assoc new-match :controllers controllers)))))

(defn on-navigate [new-match]
  (when new-match
    (re-frame/dispatch [:navigated new-match])))

(def routes
  [["/"
    {:name :root
     :controllers
     [{:start (fn [] (re-frame/dispatch [:load-top-posts]))}]}]
   ["/:tab"
    {:name      :posts
     :view      views/render-posts
     :parameters {:path {:tab keyword?}}
     :controllers
     [{:parameters {:path [:tab]} :start (fn [{{tab :tab} :path}] (re-frame/dispatch [:fetch-posts tab]))}]}]
   ["/:tab/:id"
    {:name      :comments
     :view      views/render-comments
     :parameters {:path {:tab keyword? :id string?}}
     :controllers
     [{:parameters {:path [:tab :id]} :start (fn [{{tab :tab id :id} :path}] (re-frame/dispatch [:fetch-comments id]))}]}]])

(def router (reitit-frontend/router routes {:data {:coercion reitit-coercion-spec/coercion}}))

(defn init-routes! []
  (reitit-frontend-easy/start!
   router
   on-navigate
   {:use-fragment true}))