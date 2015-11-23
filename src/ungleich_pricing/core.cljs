(ns ungleich-pricing.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(defonce app-state (atom {:cores 1
                          :memory 5
                          :hd 1
                          :selected :hetzner}))

(defn log [type]
  [:input {:type "input" :value (type @app-state) :size 2}])

(defn slider [type]
  [:div
   [:span (str type)]
   [log type]
   [:input {:type "range" 
            :value (type @app-state)
            :min 1
            :max 100
            :on-change #(swap! app-state assoc type (.-target.value %))
            }]])

(defn calculate-price []
  (let [hetzner_base 10
        hetzner_core 10
        hetzner_memory 5
        hetzner_hd 1
        factor {:hetzner 1
                :hetzner-raid6 1.2
                :hetzner-glusterfs 1.4}]
    (* ((:selected @app-state) factor)
       (+ hetzner_base
          (* (:hd @app-state) hetzner_hd)
          (* (:memory @app-state) hetzner_memory)
          (* (:cores @app-state) hetzner_core)))))

(defn price []
  [:div
   [:div "Price: " (int (calculate-price))]
   [:div "Price (incl. vat): " (int (* 1.08 (calculate-price)))]
   ])

(defn switch-hosting [e]
  (swap! app-state assoc :selected (keyword (-> e .-target.value))))

(defn main []
  [:div{:class "container"}
   [:div{:class "jumbotron"}
    [:h2 "Ungleich Pricing"]
    [:div
     [:p [:input{:type "radio"
                 :name "options"
                 :value "hetzner"
                 :on-click #(switch-hosting %)}] " Hetzner"]
     [:p [:input{:type "radio"
                 :name "options"
                 :value "hetzner-raid6"
                 :on-click #(switch-hosting %)}] "Hetzner Raid6"]
     [:p [:input{:type "radio"
                 :name "options"
                 :value "hetzner-glusterfs"
                 :on-click #(switch-hosting %)}] "Hetzner GlusterFS" ]]
    [slider :cores]
    [slider :memory]
    [slider :hd]
    [price]
    ]])


(reagent/render-component [main]
                          (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_countser] inc)
)
