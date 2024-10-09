package com.akkarimzai.task7.clients

import com.akkarimzai.task7.responses.ValCurs

interface ICurrencyClient {
    fun fetchAvailableCurrencies(): ValCurs
}