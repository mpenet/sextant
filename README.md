# Sextant

[![cljdoc badge](https://cljdoc.xyz/badge/cc.qbits/sextant)](https://cljdoc.xyz/d/cc.qbits/sextant/CURRENT)


Offline location geocoder/reverse geocoder from GeoName datasets

Shamelessly using code from
both
[twitter-location-parser](https://github.com/smallrivers/twitter-location-parser) and
[OfflineReverseGeocode](https://github.com/AReallyGoodName/OfflineReverseGeocode) for
the reverse GeoCoding parts.

Made it clojure friendly, the geocoder and reverse geocoder both
return the same record type, and implement the same protocol.

They both have ready to use [components](https://github.com/stuartsierra/component).

``` clojure
(require 'qbits.sextant.geocoder)
(require 'qbits.sextant)
(def g (qbits.sextant.geocoder/load))
(qbits.sextant/find-location g "paris")

=>
#qbits.sextant.Location{
 :name "Paris",
 :state-code "11",
 :country-code "FR",
 :type :city,
 :population 2138551,
 :latitude 48.85341,
 :longitude 2.3488
}

;; And from the reverse geocoder

(require 'qbits.sextant.reverse-geocoder)
(def g (qbits.sextant.reverse-geocoder/load))
(qbits.sextant/find-location g [48.85341 2.3488])

=> #qbits.sextant.Location{
    :name "Paris",
    :state-code "11",
    :country-code "FR",
    :type :city,
    :population 2138551,
    :latitude 48.85341,
    :longitude 2.3488
    }
```

The component is in `qbits.sextant.component`, it uses the same api, you
just call `qbits.sextant/find-location` on it once it's `start`ed.

## Installation

add this to your `project.clj`

[![Clojars Project](https://img.shields.io/clojars/v/cc.qbits/sextant.svg)](https://clojars.org/cc.qbits/sextant)
