# Flags 

[![Build Status](https://travis-ci.org/hackorama/flags.svg?branch=master)](https://travis-ci.org/hackorama/flags)
[![Code Cov](https://codecov.io/gh/hackorama/flags/branch/master/graph/badge.svg)](https://codecov.io/gh/hackorama/flags)


Micro Service for Country Flags 

## Build  
 
Requires Java 8 or later in path and web access for pulling in the maven dependencies. 

```
$ git clone git@github.com:hackorama/flags.git
$ cd flags 
```
 
If you are behind a corp web proxy, please add a gradle.properties files with  the proxy settings

Without the proxy set, you will see build errors failing to download the Maven dependencies, like Could not determine the dependencies of task, Unable to load Maven meta-data from https://jcenter.bintray.com) 

```
$ vi gradle.properties 
systemProp.http.proxyHost=<host>
systemProp.http.proxyPort=<port>
systemProp.http.proxyHost=<host>
systemProp.http.proxyPort=<port>
```

```
$ ./gradlew build 
```
 
## Develop  

Build an Eclipse project  

```
$ ./gradlew eclipse 
```
 
Import project into Eclipse  


File -> Import -> Existing Projects into Workspace 
 
## Deploy 

```
$ java -jar build/libs/flags-all.jar 

```
 
## Quick Test 

Following curl examples uses three different services 

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
 
## Get all flags 
 
```
$ curl http://localhost:8080/flags
{"Asia":{"Bangladesh":"🇧🇩","Pakistan":"🇵🇰","China":"🇨🇳","India":"🇮🇳","Indonesia":"🇮🇩"},"Eu          rope":{"UK":"🇬🇧","Italy":"🇮🇹","France":"🇫🇷","      Germany":"🇩🇪","Russia":"🇷🇺"},"Africa":{"DR Congo":"🇨🇩","Egypt":"🇪🇬","South Africa":"🇿🇦","N          igeria":"🇳🇬","Ethiopia":"🇪🇹"},"America":{"Colom    bia":"🇨🇴","USA":"🇺🇸","Argentina":"🇦🇷","Brazil":"🇧🇷","Mexico":"🇲🇽"},"Oceania":{"New Zealand          ":"🇳🇿","Papua New Guinea":"🇵🇬","Fiji":"🇫🇯","A      ustralia":"🇦🇺","Solomon Islands":"🇸🇧"}}    hackorama@home
```
## Get all flags for a Continent

```
$ curl http://localhost:8080/flags/America
{"Colombia":"🇨🇴","USA":"🇺🇸","Argentina":"🇦🇷","Brazil":"🇧🇷","Mexico":"🇲🇽"}       
```

## Get flag for a Country 

```
$ curl http://localhost:8080/flags/USA
{"USA":"🇺🇸"} 
```

## Report error for invalid Country or Continent

```
$ curl http://localhost:8080/flags/unknown
{"error":"Invalid country or continent"}
```

## Report error for invalid API paths 

```
$ curl http://localhost:8080/unknown
{"timestamp":"2019-02-03T02:38:35.185+0000","path":"/unknown","status":404,"error":"Not Found","message":null}
```
  
## API 

  
| Method | URL | Request | Response |
|--------|-----|---------|----------|
| GET | `/flags/`  |  | List of all country flags grouped by continent |
| GET | `/flags/:id`  | | When id is a valid country, return the country flag |
| GET | `/flags/:id`  | | When id is a valid continent, return all country flags for the continent |
  
## Design Approach 

Pragmatic adoption of Twelve-Factor App, SOLID, YAGNI, DRY principles in the design.  

### Service design 

Complete separation of each service from underlying web framework and data store framework through interfaces.
So different web frameworks and data stores can be easily used without changing the service code at all.  

Services are composed fluently by injecting web, data and client implementation as needed.  


[Service] -- [Server Interface] --  [Spring or Sparkjava Server implementation]

```
Service flagService = new FlagService().configureUsing(new SpringServer("Flag Service")).configureUsing(new MapdbDataStore()).start(); 
```
### Data design 

Two key value tables are used.

- COUNTRY to FLAG  with One to One
- CONTINENT to COUNTRIES is One to Many

Data store abstraction separates the service from actual underlying database.
And service specific repository implementation sits between web service and data store

[Service] -- [Repository] -- [DataStore Interface] -- [Key Value, Doc Store or JDBC Implementations of Data Store]

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
- Coverage: build/reports/jacoco/test/html/index.html (83%) 
- SpotBugs : build/reports/spotbugs/main.html (0 errors)

## DevOps

- Gradle based builds
- Github Travis and CodeCov integration

