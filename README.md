# Flags 

[![Build Status](https://travis-ci.org/hackorama/flags.svg?branch=master)](https://travis-ci.org/hackorama/flags)
[![Code Cov](https://codecov.io/gh/hackorama/flags/branch/master/graph/badge.svg)](https://codecov.io/gh/hackorama/flags)


Micro Service for Country Flags 

## Build  
 
Requires Java 8 or later in path and web access for pulling in the maven dependencies. 

```
$ git clone https://github.com/hackorama/flags.git
or
$ git clone git@github.com:hackorama/flags.git

$ cd flags 
```
 
If you are behind a corporate web proxy, please add a `gradle.properties` file with your proxy settings.

```
$ vi gradle.properties 
systemProp.http.proxyHost=<host>
systemProp.http.proxyPort=<port>
systemProp.https.proxyHost=<host>
systemProp.https.proxyPort=<port>
```

> Without the proxy set, you will see build errors failing to download the Maven dependencies, like : `Could not determine the dependencies of task, Unable to load Maven meta-data from https://jcenter.bintray.com `


```
$ ./gradlew build 
```
 
## Develop  

Build an Eclipse project  

```
$ ./gradlew eclipse 
```
 
Import the project into Eclipse  

> File -> Import -> Existing Projects into Workspace 
 
## Deploy Options

Select the data store to use.

```
$ ./gradlew build 
```

```
$ ./gradlew run --args='-h'

Flag Service

Usage : java Main [-jdbc] [-mapdb] [-h]
   -jdbc
         Use the default embedded H2 JDBC Data Store
   -jdbc [url] [driver] [user] [pass]
         Connect to a JDBC Data Store like MySQL
   -mapdb
         Use Mapdb Key Value Data Store
   -h
         Print this help

```

Default embedded JDBC data store H2

```
$ ./gradlew run --args='-jdbc'
...
2019-02-03 15:14:25:446 +0000 [main] INFO JDBCDataStore - Using Database H2 1.4.197 (2018-03-18)
...
2019-02-03 15:14:25:448 +0000 [main] INFO FlagService - Starting flag service using server com.hackorama.flags.server.spring.SpringServer,
                                          data store com.hackorama.flags.data.jdbc.JDBCDataStore
...
```
External JDBC data store MySQL

```
$ ./gradlew run --args='-jdbc jdbc:mysql://localhost/flags com.mysql.cj.jdbc.Driver test [password]'
...

2019-02-03 15:00:24:324 +0000 [main] INFO JDBCDataStore - Using Database MySQL 5.5.54-0ubuntu0.12.04.1
...
2019-02-03 15:13:08:498 +0000 [main] INFO FlagService - Starting flag service using server com.hackorama.flags.server.spring.SpringServer,
                                          data store com.hackorama.flags.data.jdbc.JDBCDataStore
...
```

Use embedded key value store MapDB

```
$ ./gradlew run --args='-mapdb'
...

2019-02-03 05:40:07:246 +0000 [main] INFO FlagService - Starting flag service using server com.hackorama.flags.server.spring.SpringServer,
                                          data store com.hackorama.flags.data.mapdb.MapdbDataStore
...
```
Default in memory data store

```
$ ./gradlew run
...
2019-02-03 05:41:00:876 +0000 [main] INFO FlagService - Starting flag service using server com.hackorama.flags.server.spring.SpringServer,
                                          data store com.hackorama.flags.data.MemoryDataStore
...
```

## API 

| Method | URL | Request | Response |
|--------|-----|---------|----------|
| GET | `/flags/`  |  | List of all country flags grouped by continent |
| GET | `/flags/:id`  | | When id is a valid country, return the country flag |
| GET | `/flags/:id`  | | When id is a valid continent, return all country flags for the continent |

## Quick Test 

Use `curl` for quick testing of the API, you can also optionally use `jq` for pretty printing the `JSON` response.

```
$ ./gradlew build 
$ ./gradlew run

2019-02-03 02:33:40:478 +0000 [main] INFO FlagService - Starting flag service using server com.hackorama.flags.server.spring.SpringServer, data store com.hackorama.flags.data.MemoryDataStore
2019-02-03 02:33:40:640 +0000 [main] INFO DataLoader - Initializing the store
2019-02-03 02:33:40:675 +0000 [main] INFO MemoryDataStore - Created multi key data store CONTINENT_COUNTRIES
2019-02-03 02:33:40:711 +0000 [main] INFO MemoryDataStore - Created data store COUNTRY_FLAG
...
 :: Spring Boot ::        (v2.1.2.RELEASE)
...
2019-02-03 02:33:45:249 +0000 [main] INFO Main - Started Main in 4.226 seconds (JVM running for 6.149)
...
```
 
### Get all flags 
 
```
$ curl http://localhost:8080/flags | jq
{
  "Asia": {
    "Bangladesh": "🇧🇩"  ,
    "Pakistan": "🇵🇰"  ,
    "China": "🇨🇳"  ,
    "India": "🇮🇳"  ,
    "Indonesia": "🇮🇩"
  },
  "Europe": {
    "UK": "🇬🇧"  ,
    "Italy": "🇮🇹"  ,
    "France": "🇫🇷"  ,
    "Germany": "🇩🇪"  ,
    "Russia": "🇷🇺"
  },
  "Africa": {
    "DR Congo": "🇨🇩"  ,
    "Egypt": "🇪🇬"  ,
    "South Africa": "🇿🇦"  ,
    "Nigeria": "🇳🇬"  ,
    "Ethiopia": "🇪🇹"
  },
  "America": {
    "Colombia": "🇨🇴"  ,
    "USA": "🇺🇸"  ,
    "Argentina": "🇦🇷"  ,
    "Brazil": "🇧🇷"  ,
    "Mexico": "🇲🇽"
  },
  "Oceania": {
    "New Zealand": "🇳🇿"  ,
    "Papua New Guinea": "🇵🇬"  ,
    "Fiji": "🇫🇯"  ,
    "Australia": "🇦🇺"  ,
    "Solomon Islands": "🇸🇧"
  }
}
```
### Get all flags for a Continent

```
$ curl http://localhost:8080/flags/America | jq
{
  "Colombia": "🇨🇴"  ,
  "USA": "🇺🇸"  ,
  "Argentina": "🇦🇷"  ,
  "Brazil": "🇧🇷"  ,
  "Mexico": "🇲🇽"
}
```

### Get flag for a Country 

```
$ curl http://localhost:8080/flags/USA | jq
{
  "USA": "🇺🇸"
}
```

### Report error for invalid Country or Continent

```
$ curl http://localhost:8080/flags/unknown | jq
{
  "error": "Invalid country or continent"
}
```

### Report error for invalid API paths 

```
$ curl http://localhost:8080/unknown | jq
{
  "timestamp": "2019-02-03T05:16:11.090+0000",
  "path": "/unknown",
  "status": 404,
  "error": "Not Found",
  "message": null
}
```
  
## Design Approach 

Pragmatic adoption of Twelve-Factor App, SOLID, YAGNI, DRY principles in the design.  

### Service design 

Complete separation of each service from underlying web framework and data store framework through interfaces.
So different web frameworks and data stores can be easily used without changing the service code at all.  

Services are composed fluently by injecting web and data implementation as needed.  

[Service] -- [Server Interface] --  [Spring or Sparkjava Server implementation]

```
Service flagService = new FlagService().configureUsing(new SpringServer("Flag Service")).configureUsing(new MemoryDataStore()).start(); ``
```

Or use any of the other two already implemented data store types like JDBC or Key Value store. 


```
Service flagService = new FlagService().configureUsing(new SpringServer("Flag Service")).configureUsing(new JDBCDataStore()).start(); 
Service flagService = new FlagService().configureUsing(new SpringServer("Flag Service")).configureUsing(new MapdbDataStore()).start(); 
```

> Please see `Deploy Options` section above where you can see the deployment of the same service with different data stores.

Similiarly you can replace Spring with Spark, Vert.x or other implementations.

```
Service flagService = new FlagService().configureUsing(new SparkServer("Flag Service")).configureUsing(new MongoDataStore()).start(); 

Service flagService = new FlagService().configureUsing(new VertxServer("Flag Service")).configureUsing(new RedisDataStore()).start(); 
```

The key design benefit is there is no direct dependency to any of the underlying web/data platform in the service package `com.hackorama.flags.service`

The only third party package import in our service package is the `slf4j` logger. This makes the service implementation truly micro and completley portable to any web/data platform.


### Data design 

Two simple key value tables are used.

- COUNTRY to FLAG  with One to One
- CONTINENT to COUNTRIES is One to Many

Data store abstraction separates the service from actual underlying database.
And service specific repository implementation sits between web service and data store

[Service] -- [Repository] -- [DataStore Interface] -- [Key Value, Doc Store or JDBC Implementations of Data Store]

#### Schema

As explained in data design above, to target any type of underlying data store a simple one to one or one to many key value association is used.

Here is how it looks when an RDBMS liek MySQL is used as the data store.

```
> Tables_in_flags|
--------------------|
CONTINENT_COUNTRIES |
COUNTRY_FLAG        |
```

```
> DESCRIBE COUNTRY_FLAG;

Field|Type        |Null|Key|Default|Extra|
-----|------------|----|---|-------|-----|
k    |varchar(64) |NO  |PRI|       |     |
v    |varchar(256)|YES |   |       |     |
```

```
> DESCRIBE CONTINENT_COUNTRIES;
Field|Type        |Null|Key|Default|Extra|
-----|------------|----|---|-------|-----|
k    |varchar(64) |YES |   |       |     |
v    |varchar(256)|YES |   |       |     |
```

> The column names and types are created by Data Store implementations on first access automatically, this could be customized.


## Audit Logs

Standard `slf4j` logging with debug level providing additional details is enabled.

Audit logs (like in the example below conections made to and by who) are enabled at `INFO` log level with special marker `AUDIT` for easy parsing by external log management and/or audit systems.

```
$ ./gradlew run --args='-jdbc jdbc:mysql://localhost/flags com.mysql.cj.jdbc.Driver test [password]'
...

2019-02-03 15:00:24:325 +0000 [main] INFO JDBCDataStore - AUDIT : Connecting to database at jdbc:mysql://localhost/flags as test 
...
2019-02-03 15:02:30:979 +0000 [jetty-http@6cbcf243-22] INFO Handler - AUDIT : Connection from Optional[/127.0.0.1:56528] requesting GET /flags/USA
...
2019-02-03 15:02:52:391 +0000 [jetty-http@6cbcf243-17] INFO Handler - AUDIT : Connection from Optional[/127.0.0.1:56530] requesting GET /flags/
...


## Metrics

Flag metrics, reported to the console every second 


```
$ ./gradlew build 
$ ./gradlew run

...
...
...
2/3/19 3:40:42 AM ==============================================================

-- Meters ----------------------------------------------------------------------
America
             count = 6
         mean rate = 0.35 events/second
     1-minute rate = 1.02 events/second
     5-minute rate = 1.16 events/second
    15-minute rate = 1.19 events/second
Asia
             count = 3
         mean rate = 0.36 events/second
     1-minute rate = 0.60 events/second
     5-minute rate = 0.60 events/second
    15-minute rate = 0.60 events/second
UK
             count = 5
         mean rate = 0.16 events/second
     1-minute rate = 0.54 events/second
     5-minute rate = 0.74 events/second
    15-minute rate = 0.78 events/second
USA
             count = 5
         mean rate = 0.11 events/second
     1-minute rate = 0.42 events/second
     5-minute rate = 0.70 events/second
    15-minute rate = 0.77 events/second

```


## Testing 

Integrated into Gradle build 

- JUnit    : build/reports/tests/test/index.html (100%) 
- Coverage: build/reports/jacoco/test/html/index.html (~60%) 
- SpotBugs : build/reports/spotbugs/main.html (0 errors)

## DevOps

- Gradle based builds
- Github Travis and CodeCov integration

