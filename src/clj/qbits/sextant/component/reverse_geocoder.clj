(ns qbits.sextant.component.reverse-geocoder
  (:require
   [com.stuartsierra.component :as com]
   [qbits.sextant :as sextant]
   [qbits.sextant.reverse-geocoder :as reverse-geocoder])
  (:import
   (geocode ReverseGeoCode)))

(defrecord LocationFinder [^ReverseGeoCode reverse-geocoder]
  sextant/LocationFinder
  (find-location [this coords]
    (sextant/find-location reverse-geocoder coords))
  com/Lifecycle
  (start [this]
    (assoc this :reverse-geocoder (reverse-geocoder/load)))
  (stop [this]
    (assoc this :reverse-geocoder nil)))

(def x (com/start (map->LocationFinder {})))
;; (sextant/find-location x [0 1])

(prn (qbits.sextant/find-location x [48.85341 2.3488]))
