package br.com.smogon.competitiveSmogon.repository

import br.com.smogon.competitiveSmogon.mapper.PokemonDetailsViewMapper
import br.com.smogon.competitiveSmogon.model.*
import br.com.smogon.competitiveSmogon.model.smogon_usage_stats.SmogonUsageStats
import br.com.smogon.competitiveSmogon.model.smogon_usage_stats.SpreadsMapper
import com.fasterxml.jackson.core.type.TypeReference
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
class RankRepository(
        private val pokemonDetailsViewMapper: PokemonDetailsViewMapper
) {

    fun doRequestRankList(entryProtocol: EntryProtocol) : List<PokemonRankView> {
        val dex = createDex()
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder(
                URI.create(getUrlRankList(entryProtocol)))
                .header("accept", "application/json")
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        println(response.body())

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
                it.startsWith(",") -> rankCSV.add(it)
            }
        }
        rankCSV.forEach { values ->
            var filterPokemon = values.split(",")
            filterPokemon = filterPokemon.filterNot { it.isEmpty()}

            responsePokemon.add(PokemonRankView(
                    rank = filterPokemon[0].toLong(),
                    pokemon = filterPokemon[1],
                    dex = findDex(filterPokemon[1],dex),
                    usage_pct = filterPokemon[2].toDouble(),
                    raw_usage = filterPokemon[3].toDouble(),
                    raw_pct = filterPokemon[4].toDouble(),
                    real = filterPokemon[5].toDouble(),
                    real_pct =  filterPokemon[6].toDouble(),
                    tier = "gen" + entryProtocol.gen + entryProtocol.tier,
                    date = entryProtocol.year + "/" + entryProtocol.month
            ))
        }
        return responsePokemon

        //TODO Tratar os campos abaixo:
        // PREVIOUS_MONTH
        // IMAGE
    }

    private fun getUrlRankList(entryProtocol: EntryProtocol): String {

        val gtr = String.format("gen%s%s-%s",entryProtocol.gen, entryProtocol.tier, entryProtocol.rating)
        return  "https://www.smogon.com/stats/" +
                "${entryProtocol.year}-${entryProtocol.month}/${gtr}.txt"
    }

    private fun getUrlRankDetails(entryProtocol: EntryProtocol, name: String): String {
        val gtr = String.format("gen%s%s/%s",entryProtocol.gen, entryProtocol.tier, entryProtocol.rating)
        return  "https://smogon-usage-stats.herokuapp.com/" +
                "${entryProtocol.year}/${entryProtocol.month}/${gtr}/${name}"
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

    fun doRequestRankDetails(entryProtocol: EntryProtocol, name: String): PokemonDetailsView {
        val objectMapper = jacksonObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val dex = createDex()
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder(
                URI.create(getUrlRankDetails(entryProtocol,name)))
                .header("accept", "application/json")
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        println(response.body())

        val responseSmogon: SmogonUsageStats = objectMapper.readValue(response.body().toString(), SmogonUsageStats::class.java)
        return pokemonDetailsViewMapper.map(responseSmogon)
    }
}
