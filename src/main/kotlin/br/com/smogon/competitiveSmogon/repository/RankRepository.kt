package br.com.smogon.competitiveSmogon.repository

import br.com.smogon.competitiveSmogon.model.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Paths

@Repository
class RankRepository {

    fun doRequest(entryProtocol: EntryProtocol) : List<Pokemon> {
        val dex = createDex()
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
                it.startsWith(",") -> rankCSV.add(it)
            }
        }
        rankCSV.forEach { values ->
            var filterPokemon = values.split(",")
            filterPokemon = filterPokemon.filterNot { it.isEmpty()}

            responsePokemon.add(Pokemon(
                    rank = filterPokemon[0].toLong(),
                    pokemon = filterPokemon[1],
                    dex = findDex(filterPokemon[1],dex),
                    usage_pct = filterPokemon[2].toDouble(),
                    raw_usage = filterPokemon[3].toDouble(),
                    raw_pct = filterPokemon[4].toDouble(),
                    real = filterPokemon[5].toDouble(),
                    real_pct =  filterPokemon[6].toDouble(),
            ))
        }
        return responsePokemon

        //TODO Tratar os campos abaixo:
        // PREVIOUS_MONTH -
        // IMAGE
        // Date -> year-month
        // Tier -> gen + entryProcotol.gen + entryProtocol.tier
    }

    private fun getUrl(entryProtocol: EntryProtocol): String {

        val gtr = String.format("gen%s%s-%s",entryProtocol.gen, entryProtocol.tier, entryProtocol.rating)
        return  "https://www.smogon.com/stats/" +
                "${entryProtocol.year}-${entryProtocol.month}/${gtr}.txt"
    }

    fun createDex() : List<Pokedex> {
        val objectMapper = jacksonObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val json = objectMapper.readValue<PokedexData>(Paths.get("src/main/resources/assets/pokedex.json").toFile())
//        val json = objectMapper.readValue(Paths.get("assets/pokedex.json").toFile(),PokedexData.class)
        return ArrayList<Pokedex>(json.data.values)
    }

    private fun findDex(name: String, pokedex: List<Pokedex>): Long {
        val poke = pokedex.filter { it.name.equals(name,true) }
        return if(poke.isEmpty()) {
            0
        } else {
            poke[0].pokedex_number
        }
    }
}
