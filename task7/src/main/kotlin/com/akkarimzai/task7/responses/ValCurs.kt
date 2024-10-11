package com.akkarimzai.task7.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ValCurs(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Valute")
    val valutes: List<Valute>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Valute(
    @JacksonXmlProperty(localName = "CharCode")
    val charCode: String,
    @JacksonXmlProperty(localName = "Value")
    val value: Double,
    @JacksonXmlProperty(localName = "VunitRate")
    val unitRate: Double
)