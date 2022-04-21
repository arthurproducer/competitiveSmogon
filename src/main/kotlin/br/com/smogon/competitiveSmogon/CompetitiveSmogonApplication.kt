package br.com.smogon.competitiveSmogon

import br.com.smogon.competitiveSmogon.model.APOD
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@SpringBootApplication
class CompetitiveSmogonApplication

fun main(args: Array<String>) {
	runApplication<CompetitiveSmogonApplication>(*args)

	val client = HttpClient.newHttpClient()
	val objectMapper = jacksonObjectMapper()

	val request = HttpRequest.newBuilder(
			URI.create("https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY"))
			.header("accept", "application/json")
			.build()

	val response = client.send(request, HttpResponse.BodyHandlers.ofString())

	val json = objectMapper.readValue(response.body(),object : TypeReference<APOD>(){})

	println(json.title)
}

fun callSmogon() {
	// TODO Para chamar a smogon eu preciso passar os parametros necessários para a chamada.
}

