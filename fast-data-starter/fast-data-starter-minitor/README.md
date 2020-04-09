# A Spring Boot Starter for Prometheus

This is Spring Boot Starter module for activating a Prometheus endpoint in Spring Boot applications.

## What you get

A new Spring Boot Actuator endpoint - designed for Prometheus:

    http://localhost:8080/actuator/prometheus

If you curl that you will get something like:

    # HELP heap_used heap_used
    # TYPE heap_used gauge

Exactly how Prometheus likes it.

## How you get it

It's as easy as any other Spring Boot Starter ;). Find your recipe below...

```xml
<dependency>
  <groupId>ltd.fdsa</groupId>
  <artifactId>prometheus-spring-boot-starter</artifactId>
  <version>3.0.0</version>
</dependency>
```

## Configuration

You can configure application properties:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "zhumingwu,prometheus"
  endpoint:
    health:
      show-details: always 
```

Same configuration as you will find with the standard Spring Boot Actuator endpoints.

