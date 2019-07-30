(ns hackernews-pwa.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::loading
 (fn [db v]
   (:loading db)))

(re-frame/reg-sub
 ::tab
 (fn [db v]
   (:tab db)))
