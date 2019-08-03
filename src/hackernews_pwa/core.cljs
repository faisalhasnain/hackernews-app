(ns hackernews-pwa.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [hackernews-pwa.events :as events]
   [hackernews-pwa.subs :as subs]
   [hackernews-pwa.routing :as routing]
   [hackernews-pwa.views :as views]
   [hackernews-pwa.config :as config]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (routing/init-routes!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (re-frame/dispatch [:load-top-posts])
  (dev-setup)
  (mount-root))
