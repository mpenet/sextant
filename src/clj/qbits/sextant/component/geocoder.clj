(ns qbits.sextant.component.geocoder
  (:require
   [com.stuartsierra.component :as com]
   [qbits.sextant :as sextant]
   [qbits.sextant.geocoder :as geocoder])
  (:import
   (com.taykey.twitterlocationparser DefaultLocationParser)))

(defrecord LocationFinder [^DefaultLocationParser geocoder]
  sextant/LocationFinder
  (find-location [this text]
    (sextant/find-location geocoder text))
  com/Lifecycle
  (start [this]
    (assoc this :geocoder (geocoder/load)))
  (stop [this]
    (assoc this :geocoder nil)))

;; (def x (com/start (map->LocationFinder {})))
;; (sextant/find-location x "paris")
