(ns com.walmartlabs.system-viz-test
  (:require [com.walmartlabs.system-viz :refer [visualize-system]]
            [com.stuartsierra.component :as component]))

(def sys
  (component/system-map
    :auth (component/using {} {:delegate :local/auth})
    :local/auth (component/using {} [:database])
    :database (component/using {} [])
    :handler (component/using {} [:database :message-queue])
    :message-queue {}
    :router (component/using {} {:queue :message-queue})
    :web-server (component/using {} [:auth :router :handler])))


(def bad-sys
  (component/system-map
    :auth (component/using {} {:delegate :local/auth})
    :local/auth (component/using {} [:database])
    :database (component/using {} [])
    :handler (component/using {} [:database :massage-queue])
    :message-queue {}
    :router (component/using {} {:queue :message-queue})
    :web-server (component/using {} [:authr :router :handler])))

(def customized-sys
  (component/system-map
    :auth (component/using {} {:delegate :local/auth})
    :local/auth (component/using {:systemviz/color 'magenta} [:database])
    :database (component/using {:systemviz/highlight true} [])
    :handler (component/using {} [:database :message-queue])
    :message-queue {:systemviz/attrs {:shape 'box3d}}
    :router (component/using {} {:queue :message-queue})
    :web-server (component/using {} [:auth :router :handler])))


(comment
  (visualize-system sys)
  (visualize-system bad-sys)
  (visualize-system customized-sys)
  (visualize-system sys {:horizontal true})

  )
