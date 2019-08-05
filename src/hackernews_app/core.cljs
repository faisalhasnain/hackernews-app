(ns hackernews-app.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [hackernews-app.events :as events]
   [hackernews-app.subs :as subs]
   [hackernews-app.routing :as routing]
   [hackernews-app.views :as views]
   [hackernews-app.config :as config]))


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
  (dev-setup)
  (mount-root))
