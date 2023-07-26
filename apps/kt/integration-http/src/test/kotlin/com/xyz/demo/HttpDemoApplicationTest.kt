package com.xyz.demo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.integration.http.multipart.UploadedMultipartFile
import org.springframework.util.LinkedMultiValueMap

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HttpDemoApplicationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate
    @Test
    fun receiveChannel() {
        val linkedMultiValueMap = LinkedMultiValueMap<String, Any>()
        linkedMultiValueMap.set("company", listOf("test"))
        linkedMultiValueMap.set("company-logo", UploadedMultipartFile(ByteArray(0),"txt","text","test.txt"))
        val requestEntity = RequestEntity.post("inboundAdapter.htm").contentType(MediaType.MULTIPART_FORM_DATA)
            .body(linkedMultiValueMap)
        val pong = restTemplate.getForEntity("/ping", String.Companion::class.java)
        println(pong)
        //val postForEntity = restTemplate.postForEntity("inboundAdapter.htm",requestEntity, String.Companion::class.java)
        //println(postForEntity.statusCode)
    }
}