(defproject cc.qbits/sextant "1.2.0"
  :description "Decode locations from things"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.stuartsierra/component "0.3.2"]
                 [org.slf4j/slf4j-api "1.7.25"]]
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :global-vars {*warn-on-reflection* true}
  :profiles {:dev {:dependencies [[org.slf4j/slf4j-log4j12 "1.7.25"]]}})
