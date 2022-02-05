package com.example.client

import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContexts
import org.apache.http.util.EntityUtils
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PostConstruct


@Component
class Client {
    @PostConstruct
    fun start() {
        client()
    }
    private fun client() {
        val sslcontext = SSLContexts.custom()
            .loadTrustMaterial(
                File("my-https.jks"), "secret".toCharArray(), // secret это ключ, который указывали при создании сертификата
                TrustSelfSignedStrategy()
            )
            .build()
        val sslsf = SSLConnectionSocketFactory(
            sslcontext, arrayOf("TLSv1.2"),
            null,
            SSLConnectionSocketFactory.getDefaultHostnameVerifier()
        )
        val httpclient = HttpClients.custom()
            .setSSLSocketFactory(sslsf)
            .build()
        val get = HttpGet("https://localhost:8443/")
        val response: HttpResponse = httpclient.execute(get)
        println(EntityUtils.toString(response.entity, "UTF-8"))
    }
}