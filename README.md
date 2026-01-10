![check](https://github.com/rp199/dropwizard-coroutines-demo/actions/workflows/check.yml/badge.svg)

# Dropwizard Coroutine Demo

This is a simple [Dropwizard](https://www.dropwizard.io/en/stable/) project demonstrating how to integrate coroutines with HTTP requests.

For examples, see [HelloWorldResource](app/src/main/kotlin/com/rp199/resource/HelloWorldResource.kt).

Developed in the context of the Coroutine Mastery Course by [kt.academy](https://kt.academy/).

## How to run

To start the application, run:
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
curl "http://localhost:8080/hello-world/coroutine-using-async-response"
```

## Benchmark

This project includes a [vegeta](https://github.com/tsenart/vegeta) script to perform simple load tests on the endpoints, comparing blocking and coroutine-based implementations. The results illustrate that the blocking endpoint quickly exhausts server threads, whereas the coroutine-based endpoints efficiently free resources to handle many concurrent requests.

Key points:
* Jetty threads are limited to 4 to intentionally reduce server capacity for handling HTTP requests. In production, this number would typically be higher, but this setup provides meaningful results with limited resources.
* Blocking requests queue up once the Jetty thread pool is exhausted, causing increased response times.
* The coroutine approach immediately frees the request thread, preventing server overload and maintaining response times close to the hardcoded service delay.
* Both coroutine approaches perform similarly, with no significant differences in performance or resource usage.

Note that this testing approach is simple and not exhaustive, focusing on a specific scenario.

To run the benchmark script, ensure the app is running, then execute:
```shell
./load-test/run-test.sh 
```

This will send requests to all endpoints and display the results.

### `blocking` results:
```
Requests      [total, rate, throughput]         1000, 50.05, 0.50
Duration      [total, attack, wait]             49.981s, 19.98s, 30.001s
Latencies     [min, mean, 50, 90, 95, 99, max]  2.001s, 29.43s, 30.001s, 30.001s, 30.001s, 30.002s, 30.002s
Bytes In      [total, mean]                     500, 0.50
Bytes Out     [total, mean]                     0, 0.00
Success       [ratio]                           2.50%
Status Codes  [code:count]                      0:975  200:25  

```

### `coroutines-using-future` results:
```
Requests      [total, rate, throughput]         1000, 50.05, 45.49
Duration      [total, attack, wait]             21.985s, 19.98s, 2.005s
Latencies     [min, mean, 50, 90, 95, 99, max]  2s, 2.004s, 2.004s, 2.006s, 2.006s, 2.007s, 2.016s
Bytes In      [total, mean]                     39000, 39.00
Bytes Out     [total, mean]                     0, 0.00
Success       [ratio]                           100.00%
Status Codes  [code:count]                      200:1000  

```

### `coroutine-using-async-response` results:
```
Requests      [total, rate, throughput]         1000, 50.05, 45.49
Duration      [total, attack, wait]             21.985s, 19.98s, 2.005s
Latencies     [min, mean, 50, 90, 95, 99, max]  2s, 2.004s, 2.004s, 2.005s, 2.006s, 2.006s, 2.008s
Bytes In      [total, mean]                     46000, 46.00
Bytes Out     [total, mean]                     0, 0.00
Success       [ratio]                           100.00%
Status Codes  [code:count]                      200:1000  
```
