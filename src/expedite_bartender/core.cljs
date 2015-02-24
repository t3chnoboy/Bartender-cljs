(ns ^:figwheel-always expedite-bartender.core
    (:require-macros [cljs.core.async.macros :refer [go]])
    (:require
     [cljs.core.async :as async :refer [<!]]
     [reagent.core :as reagent :refer [atom]]
     [secretary.core :as secretary :refer-macros [defroute]]
     [expedite-bartender.utils :refer [jsonp random-beer-pic]]
     [expedite-bartender.components :refer [navbar content]]))

(defonce app-state (atom {:selected-brewery-id 1
                          :breweries []}))

(defroute "/brewery/:id" {:as params}
  (swap! app-state assoc :selected-brewery-id (js/parseInt (:id params))))

(defn app []
  [:div
   [navbar]
   [content (:breweries @app-state) (:selected-brewery-id @app-state)]])

(reagent/render-component [app]
                          (. js/document (getElementById "app")))

(go (let [breweries (:breweries (<! (jsonp "http://api.openbeerdatabase.com/v1/breweries.json")))
          beers-1 (:beers (<! (jsonp "http://api.openbeerdatabase.com/v1/beers.json?page=1")))
          beers-2 (:beers (<! (jsonp "http://api.openbeerdatabase.com/v1/beers.json?page=2")))]
      (let [beers (map #(assoc % :image (random-beer-pic)) (concat beers-1 beers-2))]
        (swap! app-state assoc :breweries (->> breweries
                                               (sort-by :name)
                                               (map (fn [brw]
                                                      (assoc brw :beers (filter #(= (:id brw) (get-in % [:brewery :id])) beers)))))))))
