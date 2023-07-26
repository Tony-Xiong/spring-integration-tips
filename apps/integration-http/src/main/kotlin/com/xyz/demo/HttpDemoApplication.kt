package com.xyz.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.QueueChannel
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.http.dsl.Http
import org.springframework.integration.http.dsl.HttpRequestHandlerEndpointSpec
import org.springframework.integration.http.multipart.UploadedMultipartFile
import org.springframework.messaging.MessageChannel
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class HttpDemoApplication {

    @Bean
    fun httpInboundAdapter(): HttpRequestHandlerEndpointSpec {
        return Http.inboundChannelAdapter("/inboundAdapter.htm").apply {
            id("httpInboundAdapter")
            requestChannel("receiveChannel")
        }
    }

    @Bean
    fun receiveChannel():MessageChannel {
        return QueueChannel(100)
    }

    @ServiceActivator(inputChannel = "receiveChannel")
    fun receiveActivator(multipartRequest: LinkedMultiValueMap<String, Any>) {
        println("### Successfully received multipart request ###")
        for (key in multipartRequest.keys) {
            when (key) {
                "company" -> {
                    println("\t $key - ${multipartRequest[key]?.get(0)}")
                }
                "company-logo" -> {
                    println("\t $key - as UploadedMultipartFile: ${(multipartRequest.getFirst(key) as UploadedMultipartFile).originalFilename}")
                }
                else -> {
                    println("else")}
            }
        }
    }


    @Bean
    fun flowFromSupplier() =
        IntegrationFlow.fromSupplier ({ "bar" }) { e -> e.poller { p -> p.fixedDelay(10).maxMessagesPerPoll(1) } }
            .channel { c -> c.queue("fromSupplierQueue") }
            .get()
    @Bean
    fun flowFromSupplier1() =
        IntegrationFlow.fromSupplier ({ "bar" }, { e -> e.poller { p -> p.fixedDelay(10).maxMessagesPerPoll(1) } })
            .channel { c -> c.queue("fromSupplierQueue") }
            .get()
    @Bean
    fun flowFromSupplier2(): IntegrationFlow {
        return IntegrationFlow.fromSupplier { "the" }
            .channel { e -> e.direct()}
            .get()
    }



    @GetMapping("/ping")
    fun pong():String{
        println("hello")
        "hehe".testIt { println(it) }
        "hihi".testThis { println(this) }
        return """{ "msg" : "pong"}"""
    }

}

fun String.testIt(f: (String) -> Unit) {
    f(this)
}

fun String.testThis(f: String.() -> Unit) {
    f(this)
}

fun main(args: Array<String>) {
    runApplication<HttpDemoApplication>(*args)
}

