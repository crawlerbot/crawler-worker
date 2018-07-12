# CrawlerWorker
This application was generated using Simlife 5.0.3, you can find documentation and help at [https://www.simlife.io/documentation-archive/v5.0.3](https://www.simlife.io/documentation-archive/v5.0.3).

This is a "microservice" application intended to be part of a microservice architecture, please refer to the [Doing microservices with Simlife][] page of the documentation for more information.

This application is configured for Service Discovery and Configuration with the Simlife-Registry. On launch, it will refuse to start if it is not able to connect to the Simlife-Registry at [http://localhost:8761](http://localhost:8761). For more information, read our documentation on [Service Discovery and Configuration with the Simlife-Registry][].

## Development

To start your application in the dev profile, simply run:

    ./mvnw


For further instructions on how to develop with Simlife, have a look at [Using Simlife in development][].



## Building for production

To optimize the CrawlerWorker application for production, run:

    ./mvnw -Pprod clean package

To ensure everything worked, run:

    java -jar target/*.war


Refer to [Using Simlife in production][] for more details.

## Testing

To launch your application's tests, run:

    ./mvnw clean test
### Other tests

Performance tests are run by [Gatling][] and written in Scala. They're located in [src/test/gatling](src/test/gatling).

To use those tests, you must install Gatling from [https://gatling.io/](https://gatling.io/).

For more information, refer to the [Running tests page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your Simlife development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a mongodb database in a docker container, run:

    docker-compose -f src/main/docker/mongodb.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/mongodb.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./mvnw verify -Pprod dockerfile:build dockerfile:tag@version dockerfile:tag@commit

Then run:

    docker-compose -f src/main/docker/app.yml up -d

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`simlife docker-compose`), which is able to generate docker configurations for one or several Simlife applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`simlife ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[Simlife Homepage and latest documentation]: https://www.simlife.io
[Simlife 5.0.3 archive]: https://www.simlife.io/documentation-archive/v5.0.3
[Doing microservices with Simlife]: https://www.simlife.io/documentation-archive/v5.0.3/microservices-architecture/
[Using Simlife in development]: https://www.simlife.io/documentation-archive/v5.0.3/development/
[Service Discovery and Configuration with the Simlife-Registry]: https://www.simlife.io/documentation-archive/v5.0.3/microservices-architecture/#simlife-registry
[Using Docker and Docker-Compose]: https://www.simlife.io/documentation-archive/v5.0.3/docker-compose
[Using Simlife in production]: https://www.simlife.io/documentation-archive/v5.0.3/production/
[Running tests page]: https://www.simlife.io/documentation-archive/v5.0.3/running-tests/
[Setting up Continuous Integration]: https://www.simlife.io/documentation-archive/v5.0.3/setting-up-ci/

[Gatling]: http://gatling.io/
