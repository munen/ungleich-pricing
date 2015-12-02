(ns ungleich-pricing.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(defonce resources (atom {:cores 1
                          :memory 5
                          :hd 1}))

(defonce selected-hosting (atom :hetzner))


(defn log [type]
  [:input {:type "input" :value (type @resources) :size 2}])

(defn slider [type]
  [:div
   [:span (str type)]
   [log type]
   [:input {:type "range" 
            :value (type @resources)
            :min 1
            :max 100
            :on-change #(swap! resources assoc type (-> % .-target .-value))
            }]])

(defn calculate-price []
  (let [hetzner_base 10
        hetzner_core 10
        hetzner_memory 5
        hetzner_hd 1
        factor {:hetzner 1
                :hetzner-raid6 1.2
                :hetzner-glusterfs 1.4}]
    (* (@selected-hosting factor)
       (+ hetzner_base
          (* (:hd @resources) hetzner_hd)
          (* (:memory @resources) hetzner_memory)
          (* (:cores @resources) hetzner_core)))))

(defn price []
  [:div
   [:div "Price: " (int (calculate-price))]
   [:div "Price (incl. vat): " (int (* 1.08 (calculate-price)))]
   ])

(defn switch-hosting [e]
  (reset! selected-hosting (keyword (-> e .-target .-value))))

(defn hosting-option [short_name name]
  [:p
   [:input {:type "radio"
            :name "options"
            :value short_name
            :checked (= short_name @selected-hosting)
            :on-click #(switch-hosting %)}] name])
            
(defn main []
  [:div{:class "container"}
   [:div{:class "jumbotron"}
    [:h2 "Ungleich Pricing"]
    [:div
     [hosting-option :hetzner "Hetzner"]
     [hosting-option :hetzner-raid6 "Hetzner Raid6"]
     [hosting-option :hetzner-glusterfs "Hetzner GlusterFS"]]
    [slider :cores]
    [slider :memory]
    [slider :hd]
    [price]
    ]])


(reagent/render-component [main]
                          (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your resources to force rerendering depending on
  ;; your application
  ;; (swap! resources update-in [:__figwheel_countser] inc)
)
