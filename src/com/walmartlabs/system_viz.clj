(ns com.walmartlabs.system-viz
  "Visualize a component system using Graphviz."
  (:require
    [com.stuartsierra.component :as component]
    [dorothy.core :as dorothy :as d]
    [clojure.set :as set]
    [clojure.java.browse :refer [browse-url]]
    [clojure.java.io :as io]
    [clojure.string :as str])
  (:import
    (java.io File)))

(defn ^:private component-attributes
  [component highlight-attrs]
  (if-not (map? component)
    {}
    (let [{:keys [:systemviz/attrs :systemviz/highlight :systemviz/color]} component]
      (cond-> {}
        attrs (merge attrs)
        color (assoc :color color :style :filled)
        highlight (merge highlight-attrs)))))

(defn system->dot
  [system horizontal highlight-attrs]
  (let [dependency-keys (->> system
                             vals
                             (mapcat (comp vals component/dependencies))
                             set)
        all-keys (into dependency-keys (keys system))
        id (volatile! 0)
        key->node-id (reduce (fn [m k]
                               (assoc m k (str (name k) "_" (vswap! id inc))))
                             {}
                             all-keys)
        component-nodes (for [[component-key component] system]
                          [(key->node-id component-key) (assoc (component-attributes component highlight-attrs)
                                                               :label (str component-key))])
        edges (for [[component-key component] system
                    [local-key system-key] (component/dependencies component)]
                (cond-> [(key->node-id component-key) (key->node-id system-key)]
                  (not= local-key system-key) (conj {:label (str local-key)})))
        bad-keys (set/difference all-keys (-> system keys set))
        bad-nodes (map (fn [k]
                         [(key->node-id k) {:label (str k)}]) bad-keys)]
    (->> [(when horizontal
            [(d/graph-attrs {:rankdir :LR})])
          component-nodes
          (when (seq bad-nodes)
            [(d/node-attrs {:color :red :style :filled :fontcolor :white})])
          bad-nodes
          ;; edges have to come last, otherwise they may "create" the bad nodes
          ;; and the node-attrs above do not take effect
          edges]
         (remove nil?)
         (reduce into [])
         (d/digraph)
         d/dot)))

(def ^:private wait 5)

(defn visualize-system
  "Visualizes the system as a graph, using Graphviz.
  A temporary file is created to store the Graphviz
  program; this is then fed through the `dot`
  command line tool to generate a PDF file.

  Visualization recognizes three special keys:

  :graphviz/highlight
  : Adds node attributes to highlight the component in
    the graph. By default, skyblue color and 24 point font.

  :graphviz/color
  : Sets the color of the component to the provided color value,
    and sets the style of the node to be filled.

  :graphviz/attrs
  : A map of keys and values; these are added as node attributes.

  The resulting PDF file may optionally be opened, or
  its location output to the console.

  Generally, this should be invoked before the call to
  `system-start`.

  Options:

  :format
  : The output format as a keyword; defaults to :pdf, but
    :png or :svg are also good choices.

  :open
  : if true (the default), then the generated image file
    will be opened.  If false, then the path to the image file
    will be printed to \\*out\\*.
    
  :horizontal
  : If true (the default is false), then the image will be laid out
    horizontally instead of vertically.

  :highlight
  : A map of Graphviz attributes and values to be applied to components
    where :graphviz/highlight is true.
    
  :save-as
  : If provided, then this is the path to which the graphviz source file 
    will be saved.  If not provided, the graphviz source file is generated
    as a temporary file.
      
  Returns the system unchanged."
  ([system]
   (visualize-system system nil))
  ([system options]
   (let [{:keys [format open save-as horizontal highlight]
            :or {format :pdf
                 horizontal false
                 open true
                 highlight {:color :skyblue
                            :style :filled
                            :fontsize 24}}} options
           format-name (name format)
         image-file (File/createTempFile "system-" (str "." format-name))
         image-url (.toURL image-file)
         dot (system->dot system horizontal highlight)]

     (d/save! dot image-file {:format format})

     (when save-as
       (spit (io/file save-as) dot))

     (if open
       (browse-url image-url)
       (println "System graph rendered to:" (str image-url))))

    ;; Ignore the result of the above, and just return the system.
   system))
