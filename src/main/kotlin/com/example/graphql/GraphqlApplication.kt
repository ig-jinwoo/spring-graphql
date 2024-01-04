package com.example.graphql

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import reactor.core.publisher.Mono
import java.time.Duration


@SpringBootApplication
class GraphqlApplication

fun main(args: Array<String>) {
    runApplication<GraphqlApplication>(*args)
}

@Bean
fun interceptor(): WebGraphQlInterceptor {
    return WebGraphQlInterceptor { webInput: WebGraphQlRequest?, interceptorChain: WebGraphQlInterceptor.Chain ->
        Mono.delay(Duration.ofMillis(10))
            .flatMap<WebGraphQlResponse> { aLong: Long? -> interceptorChain.next(webInput!!) }
    }
}
