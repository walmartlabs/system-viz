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