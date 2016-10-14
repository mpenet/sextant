(ns qbits.sextant.reverse-geocoder
  (:refer-clojure :exclude [load])
  (:require
   [qbits.sextant :as sextant]
   [clojure.java.io :as io])
  (:import
   (qbits.sextant Location)
   (java.util.zip ZipInputStream)
   (geocode ReverseGeoCode)))

(defn load
  ([geonames-cities-file major]
   (ReverseGeoCode. (ZipInputStream. (io/input-stream geonames-cities-file))
                    (boolean major)))
  ([geonames-cities-file]
   (load geonames-cities-file true))
  ([]
   (load (io/resource "data/cities15000.zip"))))

(extend-protocol sextant/LocationFinder
  ReverseGeoCode
  (find-location [this [lat lng]]
    (when-let [place (.nearestPlace this lat lng)]
      (sextant/Location. (.name place)
                     (.stateCode place)
                     (.country place)
                     :city
                     (.population place)
                     (.latitude place)
                     (.longitude place)))))
