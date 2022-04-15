package br.com.smogon.competitiveSmogon.model

import com.fasterxml.jackson.annotation.JsonProperty


data class APOD(
        @JsonProperty("copyright")
        @get:JsonProperty("copyright")
        val copyright: String? = null,
        @JsonProperty("date")
        @get:JsonProperty("date")
        val date: String? = null,
        @JsonProperty("explanation")
        @get:JsonProperty("explanation")
        val explanation: String? = null,
        @JsonProperty("hdurl")
        @get:JsonProperty("hdurl")
        val hdurl: String? = null,
        @JsonProperty("media_type")
        @get:JsonProperty("media_type")
        val media_type: String? = null,
        @JsonProperty("service_version")
        @get:JsonProperty("service_version")
        val service_version: String? = null,
        @JsonProperty("title")
        @get:JsonProperty("title")
        val title: String? = null,
        @JsonProperty("url")
        @get:JsonProperty("url")
        val url: String? = null
)