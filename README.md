![check](https://github.com/rp199/dropwizard-coroutines-demo/actions/workflows/check.yml/badge.svg)

# Dropwizard coroutine demo

A simple Dropwizard project to provide examples how to bridge it with coroutines for 
http requests.

See [HelloWorldResource](app/src/main/kotlin/com/rp199/resource/HelloWorldResource.kt) for the examples.

## How to run

Start the application:
```
./gradlew :app:run
```

Available endpoints:
```shell
curl "http://localhost:8080/hello-world/blocking"  
```
```shell
curl "http://localhost:8080/hello-world/coroutines-using-future"  
```
```shell
curl "http://localhost:8080/hello-world/coroutine-using-async-response  
```

