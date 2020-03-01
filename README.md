### Project Site Report

Please see site report for MIRO widget back-end application
(see: [Site Report](https://www.goldennode.io/) )

### Rest API Operations

Operations supported by widget REST resource are below. They are all testable using swagger client:
(see: [Swagger Client](https://www.goldennode.io/swagger-ui.html#/widget-controller) )

#### Get all the widgets created sorted by z-index
curl -X GET "https://www.goldennode.io/widgets?page=0&pageSize=10" -H "accept: */*"

#### Spatial query sorted by z-index

curl -X GET "https://www.goldennode.io/widgets?page=0&pageSize=10&x1=1&x2=2&y1=3&y2=4" -H "accept: */*"

#### Create a new widget
curl -X POST "https://www.goldennode.io/widgets" -H "accept: */*" -H "Content-Type: application/json" -d "{ "height": 0, "id": "string", "modifiedAt": 0, "width": 0, "xCoordinate": 0, "yCoordinate": 0, "zIndex": 0}"

#### Update an existing widget
curl -X PUT "https://www.goldennode.io/widgets/{widgetId}" -H "accept: */*" -H "Content-Type: application/json" -d "{ "height": 0, "id": "string", "modifiedAt": 0, "width": 0, "xCoordinate": 0, "yCoordinate": 0, "zIndex": 0}"

#### Delete an existing widget
curl -X DELETE "https://www.goldennode.io/widgets/{widgetId}" -H "accept: */*"

#### Gets a single widget
curl -X GET "https://www.goldennode.io/widgets/{widgetId}" -H "accept: */*"

### Notes

- Implementation is in Java 11. 

- Application is thread-safe.

- Unit tests provided. Jococo coverage report shows that %74 of the lines are covered by unit tests.
(see: [Coverage Report](https://www.goldennode.io/jacoco/index.html) )

- Integration tests provided.

- In-memory database(InMemoryDatabase) is implemented in-house with pagination support with HATEOAS links and RTree indexing support
(see: [InMemoryDatabase.java](https://github.com/ozgengunay/miro/blob/master/src/main/java/com/ozgen/miro/domain/InMemoryDatabase.java) )

- Alternatively H2 database(H2Database) may be used by switching the Database wiring from InMemoryDatabase to H2Database. H2 database
implementation also makes use of pagination and RTree implemented previously for InMemoryDatabase.
(see: [H2Database.java](https://github.com/ozgengunay/miro/blob/master/src/main/java/com/ozgen/miro/domain/H2Database.java) )

- Computational complexity of used 3rd party RTree algorithm (see: [3rd parth RTree API](https://github.com/davidmoten/rtree ) ) :
average : O(log(n))
worst   : O(n)
best    : O(log(n))

- Rate limiting is supported for individual end-points and globally. RateLimiter is implemented in-house. Configuration for rate limiter can be overridden
and updated at runtime(Implemented a NIO watcher). You just have to provide "spring.config.location" JVM parameter for external application.properties file at launch.
(see: [MiroRateLimiter.java](https://github.com/ozgengunay/miro/blob/master/src/main/java/com/ozgen/miro/controllers/ratelimiting/MiroRateLimiter.java) )

### How To Build

##### Requirements:

- Java 11
- Maven 3.x

##### Without integration tests:
 
$ mvn clean install -DskipITs

##### With integration tests :
$ mvn clean install

### How To Run
 
#### with default settings:
$ java -jar miro-0.0.1-SNAPSHOT.jar

#### with overridden settings:
$ java -jar miro-0.0.1-SNAPSHOT.jar -Dspring.config.location=/path/to/external/properties/application.properties
 
### DevOPS

-Application is running on AWS EC2 service with auto-scaling enabled. All the configuration is coded in cloudformation templates including full infrastructure:
(see: [Cloudformation scripts](https://github.com/ozgengunay/miro/tree/master/cloud-scripts) )


