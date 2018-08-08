# CrawlerWorker

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



## Using Docker to simplify development (optional)

For example, to start a mongodb database in a docker container, run:

    docker-compose -f src/main/docker/mongodb.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/mongodb.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./mvnw verify -Pprod dockerfile:build dockerfile:tag@version dockerfile:tag@commit

Then run:

    docker-compose -f src/main/docker/app.yml up -d
