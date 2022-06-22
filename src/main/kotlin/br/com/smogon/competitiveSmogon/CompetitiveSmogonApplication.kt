package br.com.smogon.competitiveSmogon

import br.com.smogon.competitiveSmogon.model.APOD
import br.com.smogon.competitiveSmogon.model.PokemonRankView
import br.com.smogon.competitiveSmogon.model.smogon_usage_stats.SmogonUsageStats
import br.com.smogon.competitiveSmogon.model.smogon_usage_stats.SpreadsMapper
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
//	callSmogon()
	doRequestRankDetails()
}

fun callSmogon() {

	val client = HttpClient.newHttpClient()

	val request = HttpRequest.newBuilder(
			URI.create("https://www.smogon.com/stats/2022-03/gen5ou-1760.txt"))
			.header("accept", "application/json")
			.build()

	val response = client.send(request, HttpResponse.BodyHandlers.ofString())
	
	val rankCSV = arrayListOf<String>()
	val responsePokemon = arrayListOf<PokemonRankView>()
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
	rankCSV.forEach { values ->
		var filterPokemon = values.split(",")
		filterPokemon = filterPokemon.filterNot { it.isNullOrEmpty()}

		responsePokemon.add(PokemonRankView(
		rank = filterPokemon[0].toLong(),
		pokemon = filterPokemon[1],
		usage_pct = filterPokemon[2].toDouble(),
		raw_usage = filterPokemon[3].toDouble(),
		raw_pct = filterPokemon[4].toDouble(),
		real = filterPokemon[5].toDouble(),
		real_pct =  filterPokemon[6].toDouble()
		))
	}

	println(responsePokemon)

	// TODO Para chamar a smogon eu preciso passar os parametros necessários para a chamada.
}

fun doRequestRankDetails() {
	val objectMapper = jacksonObjectMapper()
	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//	objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)


	val client = HttpClient.newHttpClient()
	val request = HttpRequest.newBuilder(
			URI.create(getUrlRankDetails()))
			.header("accept", "application/json")
			.build()

	val response = client.send(request, HttpResponse.BodyHandlers.ofString())
	println(response.body())


	val product: SmogonUsageStats = objectMapper.readValue(response.body().toString(), SmogonUsageStats::class.java)
	val king = product as SpreadsMapper
	println(product)
	println(king.spreads?.natures)
	val jolly = king.spreads?.any()?.filterKeys { it?.contains("Jolly") ?: false }
	println(jolly?.get("Jolly")?.get("4/252/0/0/0/252"))
	val att = king.spreads?.any()?.filterKeys { it?.contains("Timid") ?: false }
	println(att?.get("Timid"))

}


private fun getUrlRankDetails(): String {
        return "https://smogon-usage-stats.herokuapp.com/2020/12/gen8uu/1630/charizard"

}

