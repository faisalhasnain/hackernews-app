(ns hackernews-pwa.events
  (:require
   [ajax.core :refer [GET]]
   [re-frame.core :as re-frame]
   [hackernews-pwa.db :as db]))

(def urls {:top "https://api.hnpwa.com/v0/news/1.json"
           :new "https://api.hnpwa.com/v0/newest/1.json"
           :ask "https://api.hnpwa.com/v0/ask/1.json"
           :show "https://api.hnpwa.com/v0/show/1.json"
           :jobs "https://api.hnpwa.com/v0/jobs/1.json"})

(re-frame/reg-event-db
 :fetch-posts
 (fn [db [_ tab]]
   (GET (tab urls)
     {:keywords? true
      :response-format :json
      :handler       #(re-frame/dispatch [:show-resp :posts %1])
      :error-handler #(re-frame/dispatch [:show-error :error %1])})
   (assoc db :tab tab :loading true :type :posts)))

(re-frame/reg-event-db
 :fetch-comments
 (fn [db [_ id]]
   (GET (str "https://api.hnpwa.com/v0/item/" id ".json")
     {:keywords? true
      :response-format :json
      :handler       #(re-frame/dispatch [:show-resp :comments %1])
      :error-handler #(re-frame/dispatch [:show-error :error %1])})
   (assoc db :loading true :type :comments)))

(re-frame/reg-event-db
 :initialize-db
 (fn [_ _]
   (re-frame/dispatch [:fetch-posts (:tab db/default)])
   db/default))

(re-frame/reg-event-db
 :show-resp
 (fn [db [_ name val]]
   (assoc db name val :loading false)))
