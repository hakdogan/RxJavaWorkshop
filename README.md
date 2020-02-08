# Rx Java Workshop

This repository has some use cases related to `Rx Java` and three missions for the workshop.

`FactoryMethods` module has factory methods that are commonly used for creating Observable. 

`TypesOfObservable` module has some Observable types.

`Operators` module has some operators with use cases.

`UtilityForJavaDay` module has an application that builds with Spring Boot. The application offers a variety of services for speakers of `Java Day İstanbul`. For example, mailing, notifications about cheap flight and hotel information, etc. Our missions will be related to this application.

## Missions

In this workshop, we have three missions.

### Mission 1
In the application, we use `Elasticsearch` as a cache. Therefore, we have a `scheduler` that responsible for keeping `Elasticsearch` synchronized with the database.

```java
final List<Speaker> speakers = daoService.findAllChangeSpeakersByTime(lastQueryTime);
final BulkRequest bulkRequest = new BulkRequest();
speakers.stream().forEach(speaker -> {
    bulkRequest.add(new IndexRequest(INDEX).id(String.valueOf(speaker.getId()))
               .source(XContentType.JSON,"name", speaker.getName(),
               "title", speaker.getTitle(),
               "approve", speaker.isApprove(),
               "retracted", speaker.isRetracted(),
               "mail", speaker.getMail(),
               "updateTime", speaker.getUpdateTime()));
    });
```
Related to this case, our mission is to **use real-time synchronization with subject object of Rx Java instead of periodical synchronization with Scheduler.** 

### Mission 2
We would like to inform the speakers whose talks are not approved and we also want to be aware of unsuccessful sendings while doing this. For that need, we use the following code block.

```java
final List<Pair<Speaker, Future<String>>> tasks = speakers
        .stream().map(speaker -> Pair.of(speaker, sendEmailAsync(speaker)))
        .collect(toList());

final List<Speaker> failures = tasks.stream()
        .flatMap(pair -> {
            try {
                Future<String> future = pair.getRight();
                future.get(1, TimeUnit.SECONDS);
                return Stream.empty();
            } catch (Exception e) {
                Speaker speaker = pair.getLeft();
                logger.warn("Failed to send {}", speaker, e);
                return Stream.of(speaker);
            }
        }).collect(toList());
```

Related to this case, our mission is to **use Observable instead of Future.** 

### Mission 3
Our speakers have several criteria for travel. For example, they are suitable for travel only certain days, some prefer to travel in sunny weather they also have limits for flight and accommodation costs, etc. We have three streams each is independent of the other they are `weather`, `flight`, and `hotel`. 

Related to this case, our mission is to **report flights and hotels that match the criteria and declared weather conditions by composing Observables.**

## Requirements

1. Bring your laptop

2. Install `JDK 8` or later by your computer. 

3. Because the `UtilityForJavaDay` module uses `Elasticsearch` you must have a running `Elasticsearch Server`. You can download current Elasticsrarch version here https://www.elastic.co/downloads/elasticsearch In other option is `Docker`.  The following command starts the `Elasticsearch Server` with `Docker`. 

```shell script
docker run -d --rm --name elasticsearch \ 
--net esnetwork \
-p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" \
elasticsearch:7.5.2
```

## Solutions

The solutions of the missions will be pushed solutions branch on the workshop day.