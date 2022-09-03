package br.com.smogon.competitiveSmogon.repository

import br.com.smogon.competitiveSmogon.exception.NotFoundException
import br.com.smogon.competitiveSmogon.mapper.PokemonDetailsViewMapper
import br.com.smogon.competitiveSmogon.model.*
import br.com.smogon.competitiveSmogon.model.pokeAPI.PokeAPIMovesResponse
import br.com.smogon.competitiveSmogon.model.smogon_usage_stats.SmogonUsageStats
import br.com.smogon.competitiveSmogon.util.Commons
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class RankRepository(
        private val pokemonDetailsViewMapper: PokemonDetailsViewMapper,
        private val commons: Commons
) {

    fun doRequestRankList(entryProtocol: EntryProtocol) : List<PokemonRankView> {
        val dex = commons.createDex()
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
                    dex = commons.findDex(filterPokemon[1],dex)?.pokedex_number,
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

    fun doRequestRankDetails(entryProtocol: EntryProtocol, name: String): PokemonDetailsView {
        val objectMapper = jacksonObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder(
                URI.create(getUrlRankDetails(entryProtocol,name)))
                .header("accept", "application/json")
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        println(response.body())

        val responseSmogon: SmogonUsageStats = objectMapper.readValue(response.body().toString(), SmogonUsageStats::class.java)
        val detailsMapper = pokemonDetailsViewMapper.map(responseSmogon)
        return movesMapFromPokeApi(detailsMapper)
    }

    private fun getUrlRankDetails(entryProtocol: EntryProtocol, name: String): String {
        val gtr = String.format("gen%s%s/%s",entryProtocol.gen, entryProtocol.tier, entryProtocol.rating)
        return  "https://smogon-usage-stats.herokuapp.com/" +
                "${entryProtocol.year}/${entryProtocol.month}/${gtr}/${name}"
    }

    fun doRequestMoves(move: String): PokeAPIMovesResponse? {
        val objectMapper = jacksonObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val client = HttpClient.newHttpClient()
        try {
            val request = HttpRequest.newBuilder(
                    URI.create(getUrlPokeAPIMoves(move)))
                    .header("accept", "application/json")
                    .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            println(response.body())

            return objectMapper.readValue(response.body().toString(), PokeAPIMovesResponse::class.java)
        }catch (e: Exception) {
            println(e.localizedMessage) // TODO Substituir por Logger
        }
        return null
    }

    private fun getUrlPokeAPIMoves(move: String) : String {
        return "https://pokeapi.co/api/v2/move/$move"
    }

    private fun movesMapFromPokeApi(p: PokemonDetailsView): PokemonDetailsView {
        p.moves.forEach {
            //Fazer de maneira assincrona todas as chamadas de Moves
            it?.name?.let { moveName ->
                val moveResponse = doRequestMoves(moveName.lowercase().replace(" ","-"))
                //TODO Tratar múltiplas chamadas
                it.accuracy = moveResponse?.accuracy?.toInt()
                it.damage_class = moveResponse?.damage_class?.name
                it.id = moveResponse?.id?.toLong()
                it.power = moveResponse?.power?.toInt()
                it.pp = moveResponse?.pp?.toInt()
                it.type = moveResponse?.type?.name
            }
        }
        return p
    }


}
