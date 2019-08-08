(defproject hackernews-app "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.8"]
                 [cljs-ajax "0.7.5"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [metosin/reitit-core "0.3.9"]
                 [metosin/reitit-frontend "0.3.9"]
                 [metosin/reitit-spec "0.3.9"]
                 [ring "1.7.1"]]

  :plugins [[lein-cljsbuild "1.1.7"]]

  :min-lein-version "2.5.3"

  :source-paths ["src"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler hackernews-app.server/handler}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]]

    :plugins      [[lein-figwheel "0.5.18"]]}
   :prod {}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src"]
     :figwheel     {:on-jsload "hackernews-app.core/mount-root"}
     :compiler     {:main                 hackernews-app.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}}}

    {:id           "min"
     :source-paths ["src"]
     :compiler     {:main            hackernews-app.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}]})
