Multiplication microservices pet project
=========================

This is a spring boot application with microservices architecture.
The service discovery, load balancing, API gateway pattern and circuit breaker
are realized in this project.
The following technologies are used: Spring Cloud Eureka, Ribbon, Zuul, Hystrix and Cucumber.
Jetty web server is used for the frontend running.

Remarks on the Code
-------------------
* **social-multiplication** - Back end service with REST API to get and provide results of 
the multiplications. When an user send attempt it emits an event for the gamification service.
* **gamification** - This service consumes event from the social-multiplication 
service and assign points and badges to the user. Additionally this back end service provides a REST API
to get game statistics and build leader board on the frontend.
* **service-registry** - The Eureka server, which is contacted by backend services
and the gateway.
* **gateway** - Routing service implemented with Zuul. It connects with Eureka for service discovery, and
performs load balancing with Ribbon.
* **ui** - Static files of the frontend application.
* **tests-e2e** - End to end test cases developed with Cucumber.

How to run
----------

1. Download and install [RabbitMQ](https://www.rabbitmq.com/download.html).
2. Download and install [Jetty](http://www.eclipse.org/jetty/download.html).
3. Start the following services: social-multiplication, gamification, service-registry and gateway.