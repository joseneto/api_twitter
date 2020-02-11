(ns api-twitter.handler-acceptance-test
  (:require [midje.sweet :refer :all]
            [api-twitter.handler :refer :all]
            [api-twitter.utils-test :refer :all]
            [api-twitter.db :as db]
            [cheshire.core :as json]
            [clj-http.client :as http]))

;;Basic acceptance tests

(against-background [(before :facts [(start-server default-port)])
                     (after :facts  (stop-server))]

                    (fact "user registration accepted" :acceptance
                          (db/clear)
                          (let [response
                                (http/post (default-address "/register") (json-content {:username "admin" :email "admin@upnid.com" :pass_hash "@gd67grdfbf87fhb87b872dh27"}))]

                            (:status response) => 201))

                    (fact "user registration refused missing username" :acceptance
                          (let [response
                                (http/post (default-address "/register") (json-content {:username " " :email "admin@upnid.com" :pass_hash "@gd67grdfbf87fhb87b872dh27"}))]

                            (:status response) => 422))

                    (fact "user registration refused missing email" :acceptance
                          (let [response
                                (http/post (default-address "/register") (json-content {:username "admin" :email " " :pass_hash "@gd67grdfbf87fhb87b872dh27"}))]

                            (:status response) => 422))

                    (fact "user registration refused missing password" :acceptance
                          (let [response
                                (http/post (default-address "/register") (json-content {:username "admin" :email "admin@upnid.com" :pass_hash " "}))]

                            (:status response) => 422))

                    (fact "user or email already exist" :acceptance
                          (let [response
                                (http/post (default-address "/register") (json-content {:username "admin" :email "admin@upnid.com" :pass_hash "@gd67grdfbf87fhb87b872dh27"}))]

                            (:status response) => 422))

                    (fact "sign with username" :acceptance
                          (contains? (json/parse-string (:body (http/post (default-address "/sign")
                                                                          (json-content {:username "admin" :email " " :pass_hash "@gd67grdfbf87fhb87b872dh27"}))) true) :_id) => true)

                    (fact "sign with email" :acceptance
                          (contains? (json/parse-string (:body (http/post (default-address "/sign")
                                                                          (json-content {:username " " :email "admin@upnid.com" :pass_hash "@gd67grdfbf87fhb87b872dh27"}))) true) :_id) => true)

                    (fact "username incorrect" :acceptance
                          (json/parse-string (:body (http/post (default-address "/sign")
                                                               (json-content {:username "example" :email " " :pass_hash "@gd67grdfbf87fhb87b872dh27"}))) true) => ())

                    (fact "email incorrect" :acceptance
                          (json/parse-string (:body (http/post (default-address "/sign")
                                                               (json-content {:username " " :email "example@upnid.com" :pass_hash "@gd67grdfbf87fhb87b872dh27"}))) true) => ())

                    (fact "password incorrect" :acceptance
                          (json/parse-string (:body (http/post (default-address "/sign")
                                                               (json-content {:username "admin" :email "example@upnid.com" :pass_hash "@gd67grdfbf87fhb87b872dh88"}))) true) => ())

                    (fact "twitter post success" :acceptance
                          (let [response
                                (http/post (default-address "/twitter") (json-content {:username "admin" :post (twitter-text 100)}))]

                            (:status response) => 201))

                    (fact "twitter post exceed characters limit" :acceptance
                          (let [response
                                (http/post (default-address "/twitter") (json-content {:username "admin" :post (twitter-text 281)}))]

                            (:status response) => 422))

                    (fact "twitter post missing user" :acceptance
                          (let [response
                                (http/post (default-address "/twitter") (json-content {:username " " :post (twitter-text 281)}))]

                            (:status response) => 422))

                    (fact "twitter post missing text" :acceptance
                          (let [response
                                (http/post (default-address "/twitter") (json-content {:username "admin" :post " "}))]

                            (:status response) => 422))

                    (fact "my twitters success" :acceptance
                          
                          (contains? (json/parse-string (:body (http/get (default-address "/twitter/admin")))) :_id)) => true
                    

                    (fact "my twitters inexistent user" :acceptance
                          (let [response
                                (http/get (default-address "/twitter/adlon"))]

                            (:status response) => 200))

                    (fact "my twitters missing user" :acceptance
                          (let [response
                                (http/get (default-address "/twitter")  (json-content {}))]

                            (:status response) => 404))

                    (fact "view twitter" :acceptance
                        
                        (let [response (json/parse-string (:body (http/get (default-address "/twitter/admin"))))]
                          
                          (contains? (json/parse-string (:body (http/get
                                                                (default-address (str "/twitter/admin/status/" (get (first (get response "twitters")) "_id")))))) "_id") => true))

                    )
     