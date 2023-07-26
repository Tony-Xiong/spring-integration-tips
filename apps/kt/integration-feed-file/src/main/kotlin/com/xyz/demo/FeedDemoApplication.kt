package com.xyz.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportResource
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@ImportResource("classpath:intergration.xml")
@RestController
class FeedDemoApplication
fun main(args: Array<String>) {
    runApplication<FeedDemoApplication>(*args)
}

