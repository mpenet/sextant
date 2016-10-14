(defproject cc.qbits/sextant "1.0.2"
  :description "Decode locations from things"
  :dependencies [[org.clojure/clojure "1.9.0-alpha13"]
                 [com.stuartsierra/component "0.3.1"]
                 [org.slf4j/slf4j-api "1.7.21"]
                 [org.slf4j/slf4j-log4j12 "1.7.21"]]
  :plugins [[lein-junit "1.1.8"]]
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :global-vars {*warn-on-reflection* true})
