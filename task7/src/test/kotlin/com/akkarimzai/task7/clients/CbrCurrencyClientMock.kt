package com.akkarimzai.task7.clients

import com.akkarimzai.task7.responses.ValCurs
import com.akkarimzai.task7.responses.Valute

class CbrCurrencyClientMock : ICurrencyClient {
    override fun fetchAvailableCurrencies(): ValCurs {
        return ValCurs(
            listOf(
                Valute("USD", 94.0, 94.0),
                Valute("EUR", 104.0, 104.0),
                Valute("GBP", 134.0, 134.0),
                Valute("AUD", 67.0, 67.0),
            )
        )
    }
}
