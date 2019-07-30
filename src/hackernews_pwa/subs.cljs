(ns hackernews-pwa.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::loading
 (fn [db]
   (:loading db)))
