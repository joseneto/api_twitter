(ns api-twitter.utils-test
	(:require [api-twitter.handler :refer [app]]
              [ring.adapter.jetty :refer [run-jetty]]
              [cheshire.core :as json]
              [clj-http.client :as http]))

(def server (atom	nil))

(defn start-server [port]
	(swap! server 
        (fn [_] (run-jetty app {:port port :join? false}))))

(defn stop-server []
	(.stop @server))

(def default-port 3001)

(defn default-address [route] (str "http://localhost:" default-port route))

(defn twitter-text [characters]
  (clojure.string/join (take characters (repeat "x"))))

(defn json-content [content]
  {:content-type :json
   :body (json/generate-string content)
   :throw-exceptions false})

