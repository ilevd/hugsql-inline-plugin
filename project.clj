(defproject org.clojars.ilevd/hugsql-inline-plugin "0.1.0"
  :description "HugSQL plugin that allows inline sqlvec parameters to SQL string"
  :url "https://github.com/ilevd/hugsql-inline-plugin"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.layerware/hugsql-core "0.5.3"]]
  :repl-options {:init-ns hugsql-inline-plugin.core})
