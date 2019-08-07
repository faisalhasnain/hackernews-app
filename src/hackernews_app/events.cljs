(ns hackernews-app.events
  (:require
   [re-frame.core :as re-frame]
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]
   [hackernews-app.db :as db]))

(def urls {:top "https://api.hnpwa.com/v0/news/1.json"
           :new "https://api.hnpwa.com/v0/newest/1.json"
           :ask "https://api.hnpwa.com/v0/ask/1.json"
           :show "https://api.hnpwa.com/v0/show/1.json"
           :jobs "https://api.hnpwa.com/v0/jobs/1.json"})

(re-frame/reg-event-fx
 :fetch-posts
 (fn [{db :db} [_ tab]]
   {:http-xhrio {:method          :get
                 :uri             (tab urls)
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:show-resp :posts]
                 :on-failure      [:show-resp :error]}
    :db (assoc db :loading true :show-navbar-menu false)}))


(re-frame/reg-event-fx
 :fetch-comments
 (fn [{db :db} [_ id]]
   {:http-xhrio {:method          :get
                 :uri             (str "https://api.hnpwa.com/v0/item/" id ".json")
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:show-resp :comments]
                 :on-failure      [:show-resp :error]}
    :db (assoc db :loading true)}))

(re-frame/reg-event-db
 :initialize-db
 (fn [_ _]
   db/default))

(re-frame/reg-event-fx
 :load-top-posts
 (fn [{db :db} _]
   {:navigate! [:posts {:tab :top}]}))

(re-frame/reg-event-db
 :toggle-navbar-menu
 (fn [db _]
   (update db :show-navbar-menu not)))

(re-frame/reg-event-db
 :show-resp
 (fn [db [_ name val]]
   (assoc db name val :loading false)))
