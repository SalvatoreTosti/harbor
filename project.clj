(defproject harbor "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.cli "0.3.5"]]
  :plugins [[lein-kibit "0.1.2"]
            [lein-bin "0.3.5"]]
  :main ^:skip-aot harbor.core
  :bin {:name "harbor"}
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
