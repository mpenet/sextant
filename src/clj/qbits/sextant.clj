(ns qbits.sextant)

(defrecord Location [name state-code country-code type
                     population latitude longitude])

(defprotocol LocationFinder
  (find-location [this x]))
