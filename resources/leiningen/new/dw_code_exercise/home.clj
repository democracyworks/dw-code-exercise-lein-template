(ns {{name}}.home
  (:require [hiccup.page :refer [html5]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [{{name}}.us-state :as us-state]))

(defn header [_]
  [:head
   [:meta {:charset "UTF-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1.0, maximum-scale=1.0"}]
   [:title "Find my next election"]
   [:link {:rel "stylesheet" :href "default.css"}]])

(defn instructions [_]
  [:div {:class "instructions"}
   [:h1 "Getting started"]
   [:p "Thank you for applying to work at Democracy Works! "
    "This coding exercise is designed to show off your ability to program web applications in Clojure. "
    "You should spend no more than 2 hours on it and then turn it in to us "
    "by running the command " [:code "lein submit"] " and following the instructions it prints out. "
    "While we will be evaluating how much of the project you complete, we know that 2 hours isn't enough time to do a "
    "thorough and complete job on all of it, and we're not expecting you to. We just want to see what you get working "
    "in that amount of time."]
   [:p "It is a server-side web application written in Clojure and using the "
    [:a {:href "https://github.com/ring-clojure/ring"} "Ring"] ", "
    [:a {:href "https://github.com/weavejester/compojure"} "Compojure"] ", and "
    [:a {:href "https://github.com/weavejester/hiccup"} "Hiccup"] " libraries."
    "You should feel free to use other libraries as you see fit."]
   [:p "Right now the form below submits to a missing route in the app. To complete the exercise, do the following:"]
   [:ul
    [:li "Create the missing /search route"]
    [:li "Ingest the incoming form parameters"]
    [:li "Derive a basic set of OCD-IDs from the address (see below for further explanation)"]
    [:li "Retrieve upcoming elections from the Democracy Works election API using those OCD-IDs"]
    [:li "Display any matching elections to the user"]]
   [:p "You will get bonus points for:"
    [:ul
     [:li "Documenting your code"]
     [:li "Adding tests for your code"]
     [:li "Standardizing and/or augmenting the address data to derive more OCD division IDs (e.g. county and "
      "legislative districts)"]
     [:li "Noting additional features or other improvements you would make if you had more time"]]]
   [:h2 "All about OCD-IDs"]
   [:ul
    [:li "OCD-IDs are "
     [:a {:href "http://opencivicdata.readthedocs.io/en/latest/data/datatypes.html"}
      "Open Civic Data division identifiers"]
     " and they look like this (for the state of Alabama): "
     [:code "ocd-division/country:us/state:al"]]
    [:li "A given address can be broken down into several OCD-IDs."
     "For example an address in Birmingham, Alabama would be associated with the following OCD-IDs:"]
    [:ul
     [:li [:code "ocd-division/country:us"]]
     [:li [:code "ocd-division/country:us/state:al"]]
     [:li [:code "ocd-division/country:us/state:al/county:jefferson"]]
     [:li [:code "ocd-division/country:us/state:al/place:birmingham"]]]
    [:li "Not all of those are derivable from just an address (without running it through a standardization and augmentation service). "
     "For example, just having a random address in Birmingham doesn't tell us what county it is in. "
     "But we can derive a basic set of country, state, and place OCD-IDs that will be a good starting point for this project."]
    [:li "Elections can be retrieved from the Democracy Works elections API for a set of district divisions like so:"]
    [:ul
     [:li [:code "curl 'https://api.turbovote.org/elections/upcoming?district-divisions=ocd-division/country:us/state:al,ocd-division/country:us/state:al/county:jefferson,ocd-division/country:us/state:al/place:birmingham'"]]
     [:li "The response will be in the "
      [:a {:href "https://github.com/edn-format/edn"}
       "EDN format"]
      " (commonly used in Clojure) by default, but you can request JSON by setting your request's Accept header to 'application/json' if you prefer"]]]])

(defn address-form [_]
  [:div {:class "address-form"}
   [:h1 "Find my next election"]
   [:form {:action "/search" :method "post"}
    (anti-forgery-field)
    [:p "Enter the address where you are registered to vote"]
    [:div
     [:label {:for "street-field"} "Street:"]
     [:input {:id "street-field"
              :type "text"
              :name "street"}]]
    [:div
     [:label {:for "street-2-field"} "Street 2:"]
     [:input {:id "street-2-field"
              :type "text"
              :name "street-2"}]]
    [:div
     [:label {:for "city-field"} "City:"]
     [:input {:id "city-field"
              :type "text"
              :name "city"}]
     [:label {:for "state-field"} "State:"]
     [:select {:id "state-field"
               :name "state"}
      [:option ""]
      (for [state us-state/postal-abbreviations]
        [:option {:value state} state])]
     [:label {:for "zip-field"} "ZIP:"]
     [:input {:id "zip-field"
              :type "text"
              :name "zip"
              :size "10"}]]
    [:div.button
     [:button {:type "submit"} "Search"]]]])

(defn page [request]
  (html5
   (header request)
   (instructions request)
   (address-form request)))
