package com.akkarimzai.task7.clients

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter
import org.springframework.web.client.RestClient

@Configuration
@EnableCaching
class ClientConfigs {
    @Bean("cbrClient")
    fun cbrClient(@Value("\${api.cbr.base-url}") cbrBaseUrl: String): RestClient {
        val doubleDeserializer = SimpleModule("comma-double-deserializer", Version.unknownVersion())
            .addDeserializer(Double::class.java, object : JsonDeserializer<Double?>() {
                override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): Double? {
                    return jp.valueAsString.trim().replace(",", ".").toDoubleOrNull()
                }
            })

        val mapper = XmlMapper.builder()
            .addModule(doubleDeserializer)
            .build()
        val converter = MappingJackson2XmlHttpMessageConverter(mapper)

        return RestClient
            .builder()
            .baseUrl(cbrBaseUrl)
            .messageConverters { it.add(converter) }
            .build()
    }
}