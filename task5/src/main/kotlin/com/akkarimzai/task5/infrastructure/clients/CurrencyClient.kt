package com.akkarimzai.task5.infrastructure.clients

import com.akkarimzai.task5.core.application.contracts.infrastructure.ICurrencyClient
import com.akkarimzai.task5.core.application.exceptions.ServiceUnavailableException
import com.akkarimzai.task5.core.application.models.currency.ConvertCurrencyCommand
import com.akkarimzai.task5.core.application.models.currency.ConvertedCurrency
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity

@Service
class CurrencyClient(
    @Qualifier("currency-client") private val client: RestClient,
    @Value("\${api.currency.convert}") private val convertEndPoint: String
) : ICurrencyClient {
    private val logger = KotlinLogging.logger {}

    @CircuitBreaker(name = CIRCUIT_BREAKER_KEY, fallbackMethod = "onServiceFailure")
    override fun convertCurrency(command: ConvertCurrencyCommand): ConvertedCurrency {
        val response = client.post()
            .uri(convertEndPoint)
            .contentType(MediaType.APPLICATION_JSON)
            .body(command)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity<ConvertedCurrency>()

        if (!response.statusCode.is2xxSuccessful || response.body == null) {
            onServiceFailure()
        }

        return response.body!!
    }

    private fun onServiceFailure(ex: Throwable? = null): ConvertedCurrency {
        logger.error { "An error occurred while trying to convert currency:  ${ex?.message}" }
        throw ServiceUnavailableException("Currency exchange service not available.")
    }

    companion object {
        private const val CIRCUIT_BREAKER_KEY = "Currencies"
    }
}