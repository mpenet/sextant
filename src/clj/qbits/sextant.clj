(ns qbits.sextant
  (:import
   (com.taykey.twitterlocationparser.dto.Location)
   (com.taykey.twitterlocationparser DefaultLocationParser)
   (com.taykey.twitterlocationparser.dto LocationType)))

(defrecord Location [name state-code country-code type
                     population latitude longitude])

(defprotocol LocationFinder
  (find-location [this x]))

;; (clojure.pprint/pprint (find-location p "geneve"))
;; (clojure.pprint/pprint (find-location p "paris"))
