(defproject walmartlabs/system-viz "0.3.0"
  :description "Graphviz visualization of a component system"
  :url "https://github.com/walmartlabs/system-viz"
  :license {:name "Apache Sofware License 2.0"
            :url  "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :plugins [[lein-codox "0.10.3"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.stuartsierra/component "0.3.2"]]
  :aliases {"release" ["do"
                       "clean,"
                       "deploy" "clojars"]}
  :codox {:source-uri "https://github.com/walmartlabs/system-viz/blob/master/{filepath}#L{line}"
          :metadata   {:doc/format :markdown}})
