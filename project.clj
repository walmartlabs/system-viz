(defproject walmartlabs/system-viz "0.1.2"
  :description "Graphviz visualization of a component system"
  :url "https://github.com/walmartlabs/system-viz"
  :license {:name "Apache Sofware License 2.0"
            :url  "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :plugins [[lein-codox "0.9.3"]]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [io.aviso/toolchest "0.1.4"]
                 [com.stuartsierra/component "0.3.1"]]
  :aliases {"release" ["do"
                       "clean,"
                       "deploy" "clojars"]}
  :codox {:source-uri "https://github.com/walmartlabs/system-viz/blob/master/{filepath}#L{line}"
          :metadata   {:doc/format :markdown}})
