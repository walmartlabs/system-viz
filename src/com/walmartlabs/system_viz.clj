(ns com.walmartlabs.system-viz
  "Visualize a component system using Graphviz."
  (:require [com.stuartsierra.component :as component]
            [io.aviso.toolchest.macros :refer [cond-let]]
            [clojure.string :as str]
            [clojure.java.browse :refer [browse-url]])
  (:import (java.io File)
           (java.util.concurrent TimeUnit)))

(defn- simplify
  "Removes characters that outside of alphanumerics."
  [s]
  (str/replace s #"\W" ""))

(defn- system->dot
  [system]
  (let [component-ids (keys system)
        component-id->node-id (zipmap component-ids
                                      (map #(-> % name simplify gensym) component-ids))]
    (println "digraph System {")

    (doseq [[k v] component-id->node-id]
      (println (format "  %s [label=\"%s\"];" v k))
      (doseq [[local-id system-id] (-> system
                                       (get k)
                                       component/dependencies)]
        (print (format "  %s -> %s"
                       v
                       (component-id->node-id system-id)))

        (when-not (= local-id system-id)
          (print (format " [label=\"%s\"]"
                         local-id)))

        (println ";")))

    (println "}")))

(def ^:private wait 5)

(defn visualize-system
  "Visualizes the system as a graph, using Graphviz.
  A temporary file is created to store the Graphviz
  program; this is then fed through the `dot`
  command line tool to generate a PDF file.

  The resulting PDF file may optionally be opened, or
  its location output to the console.

  Options:

  :enabled
  : if true, then the graph will be generated and opened.
    Defaults to false, since this is generally unwanted behavior
    in production.

  :open
  : if true (the default), then the generated image file
    will be opened.  If false, then the path to the image file
    will be printed to \\*out\\*.

  Returns the system unchanged."
  [system options]

  (cond-let
  [{:keys [enabled open]
      :or {enabled false
           open true}} options]

    (not enabled)
    nil

    [dot (with-out-str
           (system->dot system))
     gvfile (File/createTempFile "system-" ".gv")
     imagefile (File/createTempFile "system-" ".pdf")
     process (do
               (spit gvfile dot)
               (.exec (Runtime/getRuntime) (str "dot -Tpdf " gvfile " -o " imagefile)))]
    (do
      (.waitFor process wait (TimeUnit/SECONDS))
      (.isAlive process))
    (binding [*out* *err*]
      (println "DOT process (to render system map) did not finish after" wait "seconds.")
      (.destroyForcibly process))

    [exit-value (.exitValue process)]

    (not (zero? exit-value))
    (binding [*out* *err*]
      (println "DOT process failed with status" (.exitValue process))
      (println (slurp (.getErrorStream process))))

    [image-url (.toURL imagefile)]

    open
    (browse-url image-url)

    :else
    (println "System graph rendered to:" (str image-url)))

  ;; Ignore the result of cond-let, and just return the system.
  system)