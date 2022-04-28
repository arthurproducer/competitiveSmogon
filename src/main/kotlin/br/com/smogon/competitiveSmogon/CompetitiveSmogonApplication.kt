package br.com.smogon.competitiveSmogon

import br.com.smogon.competitiveSmogon.model.APOD
import br.com.smogon.competitiveSmogon.model.Pokemon
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.Scanner

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
	callSmogon()
}

fun callSmogon() {

	val client = HttpClient.newHttpClient()

	val request = HttpRequest.newBuilder(
			URI.create("https://www.smogon.com/stats/2022-03/gen5ou-1760.txt"))
			.header("accept", "application/json")
			.build()

	val response = client.send(request, HttpResponse.BodyHandlers.ofString())

//	println(response.body())

	var rankCSV = arrayListOf<String>()
	val sb = StringBuilder()

	response.body().forEach {
		sb.append(it.toString()
				.replace("|", ",")
				.replace(" ", "")
				.replace("%", "")
				.replace("+",""))
	}
	val lines = sb.split("\n")
	lines.forEach {
		when {
			it.startsWith(",R") -> return@forEach
			//TODO tudo que for virgula separar em um objeto
			it.startsWith(",") -> rankCSV.add(it)
		}
	}
	rankCSV.forEach {
		//TODO tudo que for virgula separar em um objeto
		println(it)

//		Pokemon(
//		rank = it.substringAfter(",").substringBefore(",").filter { f -> f.isDigit() }.toLong(),
//		pokemon = it.substringAfter(",",","),
//		usage_pct = it.substringAfter(",",",").toDouble(),
//		raw_usage = it.substringAfter(",",",").toDouble(),
//		raw_pct = it.substringAfter(",",",").toDouble(),
//		real = it.substringAfter(",",",").toDouble(),
//		real_pct =  it.substringAfter(",",",").toDouble()
//		)
	}

	println(rankCSV)
	println(rankCSV[0])


	// TODO Para chamar a smogon eu preciso passar os parametros necessários para a chamada.
}

