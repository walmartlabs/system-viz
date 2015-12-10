# system-viz

system-viz is a tiny, simple library to visualize
a system, constructed using Stuart Sierra's
[component](https://github.com/stuartsierra/component)
library.


Use of this project requires that [Graphviz](http://www.graphviz.org) is installed, which can be checked by running `dot -V` at the command line.  If it's not installed, you can do the following:

| platform | directions |
|----------|------------|
| Linux | install `graphviz` using your package manager |
| OS X | [download the installer](http://www.graphviz.org/Download_macos.php) |
| Windows |  [download the installer](http://www.graphviz.org/Download_windows.php) |

## Usage

Here, we are creating a very hypothetical stand-in for some kind of system:

```clj
(require '[com.walmartlabs.system-viz :refer [visualize-system]])

(def sys
  (component/system-map
    :auth (component/using {} {:delegate :local/auth})
    :local/auth (component/using {} [:database])
    :database (component/using {} [])
    :handler (component/using {} [:database :message-queue])
    :message-queue {}
    :router (component/using {} {:queue :message-queue})
    :web-server (component/using {} [:auth :router :handler])))

(visualize-system sys {:enabled true})
```

This will open a window displaying an image somewhat like:

![System](sample-system.png)

Dependencies between components are shown as arrows; the arrows are only labeled
when the component's local key for the dependency is different from the target component's system key.

visualize-system returns the system it is passed; it is intended as a filter used for
side effects (creating and opening the graph image).
Since it is a development-time tool, it must be explicitly enabled.

## License

Copyright © 2015 Walmart Labs

Distributed under the Apache Software License 2.0.