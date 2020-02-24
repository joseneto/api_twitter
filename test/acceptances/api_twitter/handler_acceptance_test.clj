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
                                (http/post (default-address "/register") (json-content {:user-name "admin" :email "admin@upnid.com" :pass-hash "@gd67grdfbf87fhb87b872dh27"}))]

                            (:status response) => 201))

                    (fact "user registration refused missing user-name" :acceptance
                          (let [response
                                (http/post (default-address "/register") (json-content {:user-name " " :email "admin@upnid.com" :pass-hash "@gd67grdfbf87fhb87b872dh27"}))]

                            (:status response) => 422))

                    (fact "user registration refused missing email" :acceptance
                          (let [response
                                (http/post (default-address "/register") (json-content {:user-name "admin" :email " " :pass-hash "@gd67grdfbf87fhb87b872dh27"}))]

                            (:status response) => 422))

                    (fact "user registration refused missing password" :acceptance
                          (let [response
                                (http/post (default-address "/register") (json-content {:user-name "admin" :email "admin@upnid.com" :pass-hash " "}))]

                            (:status response) => 422))

                    (fact "user or email already exist" :acceptance
                          (let [response
                                (http/post (default-address "/register") (json-content {:user-name "admin" :email "admin@upnid.com" :pass-hash "@gd67grdfbf87fhb87b872dh27"}))]

                            (:status response) => 422))

                    (fact "sign with user-name" :acceptance
                          (contains? (json/parse-string (:body (http/post (default-address "/sign")
                                                                          (json-content {:user-name "admin" :email " " :pass-hash "@gd67grdfbf87fhb87b872dh27"}))) true) :_id) => true)

                    (fact "sign with email" :acceptance
                          (contains? (json/parse-string (:body (http/post (default-address "/sign")
                                                                          (json-content {:user-name " " :email "admin@upnid.com" :pass-hash "@gd67grdfbf87fhb87b872dh27"}))) true) :_id) => true)

                    (fact "user-name incorrect" :acceptance
                          (json/parse-string (:body (http/post (default-address "/sign")
                                                               (json-content {:user-name "example" :email " " :pass-hash "@gd67grdfbf87fhb87b872dh27"}))) true) => ())

                    (fact "email incorrect" :acceptance
                          (json/parse-string (:body (http/post (default-address "/sign")
                                                               (json-content {:user-name " " :email "example@upnid.com" :pass-hash "@gd67grdfbf87fhb87b872dh27"}))) true) => ())

                    (fact "password incorrect" :acceptance
                          (json/parse-string (:body (http/post (default-address "/sign")
                                                               (json-content {:user-name "admin" :email "example@upnid.com" :pass-hash "@gd67grdfbf87fhb87b872dh88"}))) true) => ())

                    (fact "twitter post success" :acceptance

                          (let [response
                                (http/post (default-address "/twitter") (json-content {:user-name "admin" :post (twitter-text 100)}))]

                            (:status response) => 201))

                    (fact "twitter post exceed characters limit" :acceptance
                          (let [response
                                (http/post (default-address "/twitter") (json-content {:user-name "admin" :post (twitter-text 281)}))]

                            (:status response) => 422))

                    (fact "twitter post missing user" :acceptance
                          (let [response
                                (http/post (default-address "/twitter") (json-content {:user-name " " :post (twitter-text 281)}))]

                            (:status response) => 422))

                    (fact "twitter post missing text" :acceptance
                          (let [response
                                (http/post (default-address "/twitter") (json-content {:user-name "admin" :post " "}))]

                            (:status response) => 422))

                    (fact "my twitters success" :acceptance
                          (count (json/parse-string (:body (http/get (default-address "/twitter/admin"))))) => 1)

                    (fact "my twitters success with pagination" :acceptance
                          (count (json/parse-string (:body (http/get (default-address "/twitter/admin?skip=0"))))) => 1)

                    (fact "my twitters end pagination" :acceptance
                          (count (json/parse-string (:body (http/get (default-address "/twitter/admin?skip=10"))))) => 0)


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
                                                                  (default-address (str "/twitter/status/" ((first response) "_id")))))) "_id") => true))

                    (fact "view twitter invalid twitter id" :acceptance

                          (let [response
                                (http/get (default-address "/twitter/status/024032840804923804")(json-content {}) )]

                            (:status response) => 422))


                    (fact "twitter like" :acceptance

                          (let [response (json/parse-string (:body (http/get (default-address "/twitter/admin"))))

                                response2 (http/post (default-address "/like") (json-content {:user-name "admin", :user-twitter "admin" :twitter-oid ((first response) "_id")}))]

                            (:status response2) => 201))

                    (fact "twitter like invalid user session" :acceptance

                          (let [response (json/parse-string (:body (http/get (default-address "/twitter/admin"))))
                                response2 (http/post (default-address "/like") (json-content {:user-name "admin1", :user-twitter "admin" :twitter-oid ((first response) "_id")}))]

                            (:status response2) => 422))

                    (fact "twitter like invalid user twitter onwer" :acceptance

                          (let [response (json/parse-string (:body (http/get (default-address "/twitter/admin"))))
                                response2 (http/post (default-address "/like") (json-content {:user-name "admin", :user-twitter "admin1" :twitter-oid ((first response) "_id")}))]

                            (:status response2) => 422))

                    (fact "twitter like invalid twitter oid" :acceptance

                          (let [response (http/post (default-address "/like") (json-content {:user-name "admin", :user-twitter "admin" :twitter-oid  "024032840804923804"}))]

                            (:status response) => 422))

                    (fact "twitter dislike" :acceptance

                          (let [response (json/parse-string (:body (http/get (default-address "/twitter/admin"))))
                                response2 (http/post (default-address "/like") (json-content {:user-name "admin", :user-twitter "admin" :twitter-oid ((first response) "_id")}))]

                            (:status response2) => 201)))
     