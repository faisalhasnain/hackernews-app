(ns hackernews-app.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :get-db
 (fn [db [_ name]]
   (name db)))
