(ns expedite-bartender.components
  (:require [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary :refer-macros [defroute]]))

(defn navbar []
  [:header.navbar-fixed
   [:nav
    [:div.nav-wrapper
     [:a.brand-logo
      [:img {:src "https://raw.githubusercontent.com/docker-library/docs/master/clojure/logo.png"
             :width "50"}]
      "Expedite Bartender"]]]])

(defn preloader []
  [:div.valign-wrapper
   [:div.center-align.valign
    [:div.preloader-wrapper.big.active
     [:div.spinner-layer.spinner-blue
      [:div.circle-clipper.left
       [:div.circle]]
      [:div.gap-patch
       [:div.circle]]
      [:div.circle-clipper.right
       [:div.circle]]]]]])

(defn beer-item [beer]
  (let [expanded? (atom false)]
    (fn []
      [:div.collection-item.row {:on-click #(swap! expanded? not)}
       [:div.col.s1.m1.l1
        [:img.responsive-img {:src (:image beer)}]]
       [:div.col.s11.m11.l11
        [:h5
         (:name beer)
         [:span.abv.badge (:abv beer)]]
        (when @expanded? [:p (:description beer)])]])))

(defn beer-list [beers]
  [:div.col.s12.m8.l9.collection.beer-list
   (for [beer beers]
     ^{:key (:id beer)} [beer-item  beer])])

(defn brewery-item [brewery selected?]
  [:div {:class (str (when selected? "active ")
                     "brewery "
                     "collection-item ")
         :on-click #(secretary/dispatch! (str "/brewery/" (:id brewery)))}
   [:h6
    (:name brewery)
    [:span.badge (count (:beers brewery))]]])

(defn brewery-list [breweries selected-brewery-id]
  [:div.col.s12.m4.l3.sidebar
   [:div.collection
    (for [brewery breweries]
      ^{:key (:id brewery)} [brewery-item brewery (= selected-brewery-id (:id brewery))])]])

(defn content [breweries selected-brewery-id]
  (if (empty? breweries)
    [preloader]
    [:div.row
     [brewery-list breweries selected-brewery-id]
     [beer-list (:beers (some #(if (= selected-brewery-id (:id %)) %) breweries))]]))
