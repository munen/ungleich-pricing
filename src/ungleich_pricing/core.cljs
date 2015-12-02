(ns ungleich-pricing.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :refer [capitalize]]))

(enable-console-print!)

(defonce resources (atom {:cores 1
                          :memory 5
                          :hd 1}))

(defonce selected-hosting (atom :hetzner))


(defn log [type]
  [:input {:type "input"
           :value (type @resources)
           :size 2
           :on-change #(swap! resources assoc type (-> % .-target .-value))}])

(defn slider [type]
  [:div
   [:span.label.label-default (capitalize (name type))]
   [log type]
   [:input {:type "range" 
            :value (type @resources)
            :min 1
            :max 100
            :on-change #(swap! resources assoc type (-> % .-target .-value))}]])

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

(defn euro []
  [:span.glyphicon.glyphicon-eur])

(defn price []
  [:div.well
   [:h5 "Price: " (int (calculate-price))
    [euro]]

   [:h5 "Price (incl. vat): " (int (* 1.08 (calculate-price)))
    [euro]]])

(defn switch-hosting [e]
  (reset! selected-hosting (keyword (-> e .-target .-value))))

(defn hosting-option [short_name name]
  [:div.col-xs-4
   [:h4
    [:input {:type "radio"
             :name "options"
             :value short_name
             :checked (= short_name @selected-hosting)
             :on-click #(switch-hosting %)}] name]])
            
(defn main []
  [:div{:class "container"}
   [:div{:class "jumbotron"}
    [:h2 "Ungleich Pricing"]
    [:div.row
     [hosting-option :hetzner "Hetzner"]
     [hosting-option :hetzner-raid6 "Hetzner Raid6"]
     [hosting-option :hetzner-glusterfs "Hetzner GlusterFS"]]
    [:div.row
     [slider :cores]
     [slider :memory]
     [slider :hd]]
    [:div.row
     [price]]
    ]])

(reagent/render-component [main]
                          (. js/document (getElementById "app")))
