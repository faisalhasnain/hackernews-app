(ns hackernews-pwa.icons)

(def external-link [:svg.feather.feather-external-link
                    {:stroke-linejoin "round"
                     :stroke-linecap "round"
                     :stroke-width "2"
                     :stroke "currentColor"
                     :fill "none"
                     :viewBox "0 0 24 24"
                     :height "24"
                     :width "24"}
                    [:path
                     {:d "M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"}]
                    [:polyline {:points "15 3 21 3 21 9"}]
                    [:line {:y2 "3", :x2 "21", :y1 "14", :x1 "10"}]])

(def message-square [:svg.feather.feather-message-square
                     {:stroke-linejoin "round"
                      :stroke-linecap "round"
                      :stroke-width "2"
                      :stroke "currentColor"
                      :fill "none"
                      :viewBox "0 0 24 24"
                      :height "24"
                      :width "24"}
                     [:path
                      {:d
                       "M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"}]])

(def thumbs-up [:svg.feather.feather-thumbs-up
                {:stroke-linejoin "round"
                 :stroke-linecap "round"
                 :stroke-width "2"
                 :stroke "currentColor"
                 :fill "none"
                 :viewBox "0 0 24 24"
                 :height "24"
                 :width "24"}
                [:path
                 {:d
                  "M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3"}]])

(def user [:svg.feather.feather-user
           {:stroke-linejoin "round"
            :stroke-linecap "round"
            :stroke-width "2"
            :stroke "currentColor"
            :fill "none"
            :viewBox "0 0 24 24"
            :height "24"
            :width "24"}
           [:path {:d "M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"}]
           [:circle {:r "4", :cy "7", :cx "12"}]])

(def clock [:svg.feather.feather-clock
            {:stroke-linejoin "round"
             :stroke-linecap "round"
             :stroke-width "2"
             :stroke "currentColor"
             :fill "none"
             :viewBox "0 0 24 24"
             :height "24"
             :width "24"}
            [:circle {:r "10", :cy "12", :cx "12"}]
            [:polyline {:points "12 6 12 12 16 14"}]])