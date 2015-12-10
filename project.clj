(defproject system-viz "0.1.0-SNAPSHOT"
  :description "Graphviz visualization of a component system"
  :url "http://example.com/FIXME"
  :license {:name "Apache Sofware License 2.0"
            :url  "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :plugins [[io.aviso/pretty "0.1.20"]]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [io.aviso/toolchest "0.1.2"]
                 [com.stuartsierra/component "0.3.1"]]
  :profiles {:dev {:dependencies [[io.aviso/pretty "0.1.20"]]}})
