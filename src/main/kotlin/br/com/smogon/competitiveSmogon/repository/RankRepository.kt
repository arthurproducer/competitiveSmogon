package br.com.smogon.competitiveSmogon.repository

import br.com.smogon.competitiveSmogon.model.EntryProtocol
import br.com.smogon.competitiveSmogon.model.Pokemon
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class RankRepository {

    fun doRequest(entryProtocol: EntryProtocol) : List<Pokemon> {

        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder(
                URI.create(getUrl(entryProtocol)))
                .header("accept", "application/json")
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        println(response.body())

        val rankCSV = arrayListOf<String>()
        val responsePokemon = arrayListOf<Pokemon>()
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

            responsePokemon.add(Pokemon(
                    rank = filterPokemon[0].toLong(),
                    pokemon = filterPokemon[1],
                    usage_pct = filterPokemon[2].toDouble(),
                    raw_usage = filterPokemon[3].toDouble(),
                    raw_pct = filterPokemon[4].toDouble(),
                    real = filterPokemon[5].toDouble(),
                    real_pct =  filterPokemon[6].toDouble()
            ))
        }
        return responsePokemon

        //TODO Tratar os campos abaixo:
        //ID - PREVIOUS_MONTH - IMAGE
        //DEX -> dictonary
        //Date -> year-month
        //Tier -> gen + entryProcotol.gen + entryProtocol.tier
    }

    private fun getUrl(entryProtocol: EntryProtocol): String {

        val gtr = String.format("gen%s%s-%s",entryProtocol.gen, entryProtocol.tier, entryProtocol.rating)
        return  "https://www.smogon.com/stats/" +
                "${entryProtocol.year}-${entryProtocol.month}/${gtr}.txt"
    }

}
