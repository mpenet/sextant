(ns qbits.sextant.geocoder
  (:refer-clojure :exclude [load])
  (:require [qbits.sextant :as sextant])
  (:import
   (qbits.sextant Location)
   (com.taykey.twitterlocationparser DefaultLocationParser)
   (com.taykey.twitterlocationparser.dto LocationType)))

(defn jloc->loc [^com.taykey.twitterlocationparser.dto.Location loc]
  (Location.
   (.getName loc)
   (.getStateCode loc)
   (.getCountryCode loc)
   (case (-> loc .getType .name)
     "City" :city
     "State" :state
     "Country" :country)
   (.getPopulation loc)
   (.getLatitude loc)
   (.getLongitude loc)))

(defn load []
  (DefaultLocationParser.))

(extend-protocol sextant/LocationFinder
  DefaultLocationParser
  (find-location [this text]
    (some-> this (.parseText text) jloc->loc)))

(defn country-code->location
  [^DefaultLocationParser this code]
  (some-> this (.getCountryByCode code) jloc->loc))
