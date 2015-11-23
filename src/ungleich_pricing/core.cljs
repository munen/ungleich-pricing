(ns ungleich-pricing.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(defonce app-state (atom {:count 1 }))

(defn simple-slider [type]
  [:div
   [:span (str type)]
   [:input {:type "range" 
            :value (:count @app-state)
            :min 1
            :max 100
            :on-change #(swap! app-state assoc :count (-> % .-target .-value))
            }]])

(defn log []
  [:span "Logging: " (:count @app-state)])

(defn calculate-price []
  (let [hetzner_base 10
        hetzner_core 10
        hetzner_memory 5
        hetzner_hd 1]
    (+ hetzner_base
       hetzner_hd
       hetzner_memory
       (* (:count @app-state) hetzner_core))))

(defn price []
  [:div
    [:span "Price: " (calculate-price)]])

(defn main []
  [:div
   [:h2 "Ungleich Pricing"]
   [log]
   [simple-slider :cores]
   [price]
   ])


(reagent/render-component [main]
                          (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
