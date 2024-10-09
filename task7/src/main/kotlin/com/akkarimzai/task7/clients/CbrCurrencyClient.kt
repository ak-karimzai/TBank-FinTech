package com.akkarimzai.task7.clients

import com.akkarimzai.task7.exceptions.ServiceUnavailableException
import com.akkarimzai.task7.responses.ValCurs
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import java.io.IOException

@Service
class CbrCurrencyClient(private val client: RestClient) : ICurrencyClient {
    private val logger = KotlinLogging.logger {}

    @Value("\${api.cbr.daily-rates}")
    private lateinit var currencyEndPoint: String

    @Cacheable(value = [CACHE_NAME])
    @CircuitBreaker(name = CIRCUIT_BREAKER_KEY, fallbackMethod = "onServiceFailure")
    override fun fetchAvailableCurrencies(): ValCurs {
        logger.info { "Fetching available currencies." }
        val response = client.get()
            .uri(currencyEndPoint)
            .retrieve()
            .toEntity<ValCurs>()

        if (!response.statusCode.is2xxSuccessful || response.body == null) {
            logger.error { "CBR service is unavailable request response is not 200: ${response.statusCode}" }
            onServiceFailure()
        }
        return response.body!!.also {
            logger.info { "Fetched available currencies from CBR number of available currencies: ${it.valutes.size}" }
        }
    }

    @CacheEvict(value = [CACHE_NAME], allEntries = true)
    @Scheduled(fixedDelay = CACHE_DELAY)
    fun clearCurrencyCache() {
        logger.debug { "Clearing cached currency values." }
    }

    fun onServiceFailure(ex: Throwable? = null): ValCurs {
        logger.error { "An error occurred while trying to fetch available currencies." }
        throw ServiceUnavailableException("Russia's central bank service not available.")
    }

    companion object {
        private const val CIRCUIT_BREAKER_KEY = "Currencies"
        @Value("\${cache.currencies.key}")
        private const val CACHE_NAME = "Currencies"
        @Value("\${cache.currencies.ttl}")
        private const val CACHE_DELAY = 3600000L
    }
}