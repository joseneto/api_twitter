(defproject api_twitter "0.1.0-SNAPSHOT"
  :description "Api Twitter Simulating"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :uberjar-name	"api_twitter.jar"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json	"0.4.0"]
                 [clj-http	"3.9.1"]
                 [cheshire	"5.8.1"]
                 [com.novemberain/monger "3.1.0"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler api-twitter.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]
                        [midje "1.9.6"]
                        [ring/ring-core "1.7.1"]
                        [ring/ring-jetty-adapter "1.7.1"]]
         :plugins	[[lein-midje "3.2.1"]
                   [lein-cloverage "1.0.13"]]}}
  :test-paths	["test/acceptances" "test/units" "test"])
