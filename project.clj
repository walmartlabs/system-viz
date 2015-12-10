(defproject system-viz "0.1.0"
  :description "Graphviz visualization of a component system"
  :url "https://github.com/walmartlabs/system-viz"
  :license {:name "Apache Sofware License 2.0"
            :url  "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :plugins [[io.aviso/pretty "0.1.20"]
            [lein-codox "0.9.0"]
            [lein-shell "0.4.0"]]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [io.aviso/toolchest "0.1.2"]
                 [com.stuartsierra/component "0.3.1"]]
  :profiles {:dev {:dependencies [[io.aviso/pretty "0.1.20"]]}}
  :shell {:commands {"scp" {:dir "target/doc"}}}
  :aliases {"deploy-doc" ["shell"
                          "scp" "-r" "." "hlship_howardlewisship@ssh.phx.nearlyfreespeech.net:com.walmartlabs/system-viz"]
            "release" ["do"
                       "clean,"
                       "codox,"
                       "deploy-doc,"
                       "deploy" "clojars"]}
  :codox {:source-uri "https://github.com/walmartlabs/system-viz/blob/master/{filepath}#L{line}"
          :metadata {:doc/format :markdown}})
