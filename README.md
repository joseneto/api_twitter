# Api Twitter with Clojure

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

Api Twitter it's a simple service that emulated the main resources from Twitter, it's still incomplete, but I will update regulary, the goals is have a complete service. 

### Technology

Api Twitter use some open source libraries:

* [Leininger] - Do you know Maven for Java?, Gems for Ruby? Well this is what Leininger is for Clojure
* [Ring] - A libray for web applications
* [Cheshire] - Json encode/decode
* [Midje] - Great framework to create test in for Clojure
* [Monger] - MongoDB client for a lot of documentation

Api Twitter is also an open source project, you can download and learn much more with it.
 

### Install

Api Intranet require Leininger, you can download it here [Leininger](https://leiningen.org/). You will need it to run the project, the installation is super simple, if you are using windows there is an installer here [Leininger Windows Install ](https://djpowell.github.io/leiningen-win-installer/) 

After install Leininger, you can go to your project root path and run the command below, Leining will install all dependecies.

```sh
$ lein deps
```

We are almost ready to run the project, make sure you have the MongoDB installed. If everything is ok, run the command below to start the service!

```sh
$ lein ring server-headless
```

If you MongoDB are not installed locally or using another port, you can edit the file db.clj and change the settings.


### Services

There is a lot of tools to connect with the services, but I recommend [Insomnia](https://insomnia.rest/) it's a great Rest client and you will love it. Api Twitter has for while the follow services

| Name | Endppoint |
| ------ | ------ |
| Register User |[POST] localhost:3000\register |
| Sign User |[POST] localhost:3000\sign |
| Create Twitter |[POST] localhost:3000\twitter|
| Like Twitter |[POST] localhost:3000\like|
| List All My Twitter | [GET] localhost:3000\twitter\\:user |
| See My Twitter |[GET] localhost:3000\twitter\\:status\\:id |

Let's see some examples how you can use that services.

```sh
[POST] localhost:3000\register
{
	"user-name" : "admin", 
	"email": "admin1@upnid.com", 
	"pass-hash": "@gd67grdfbf87fhb87b872dh27"
}
```

```sh
[POST] localhost:3000\sign 
{
	"user-name" : "admin", 
	"pass-hash": "@gd67grdfbf87fhb87b872dh27"
}
```

```sh
[POST] localhost:3000\twitter
{
	"user-name" :"admin", 
	"post": "My new twitter is so cool!"
}
```

```sh
[POST] localhost:3000\like
{
	"user-name" :"admin", 
	"user-twitter" :"admin", 
	"twitter-oid": "5e4f27e9000efc3474cd9d37"
}
```

```sh
[GET] localhost:3000\twitter\admin
```

```sh
[GET] localhost:3000\twitter\status\5e4f27e9000efc3474cd9d37
```
### Tests

Api Twitter was built using TDD(Test Driven Development), all development was guided by the tests, there are 40 now, between units tests and acceptance tests that can be easyle executed running with Leininger.

Remenber that when all the tests are executed, all data created in the database will be erase.

Running all tests:
```sh
$ lein midje
```
Running only acceptance tests
```sh
$ lein midje :filter acceptance
```
Running only unit tests
```sh
$ lein midje :filter -acceptance
```

### Project Structure

The project was divided as some layers:

* [src/api_twitter/core.clj] - This is our main file, where all requests are packed and the routes are created.
* [src/api_twitter/user_service.clj] - This file handle all logic related to users and send the data to the database service
* [src/api_twitter/twitter_service.clj] - This file handle all logic related to twitters and send the data to the database service
* [src/api_twitter/db.clj] - Make all connection with the MongoDB and receive all data from the services files
* [test/acceptance/api_twitter/handller_acceptance_test.clj] - This file realize all acceptence tests
* [test/units/api_twitter/handller_unit_test.clj] - This file realize all unit tests
* [test/api_twitter/utils.clj] - File with some help functions for the tests

#### Next

I am still planning use Elasticsearch, Redis and change the authentication to use OAuth2, lets try get some scalability and security in this project. New updates soon! :D








