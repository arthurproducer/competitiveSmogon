package br.com.smogon.competitiveSmogon.repository

import br.com.smogon.competitiveSmogon.model.EntryProtocol
import br.com.smogon.competitiveSmogon.model.Pokemon
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class RankRepository {

    fun doRequest(entryProtocol: EntryProtocol) {

        val client = HttpClient.newHttpClient()
        val objectMapper = jacksonObjectMapper()

        val request = HttpRequest.newBuilder(
                URI.create(getUrl(entryProtocol)))
                .header("accept", "application/json")
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        println(response.body())
        // TODO Remover caracteres desnecessários na busca
        val rankCSV = response.body().forEach {
            it.toString()
                    .replace("|", ",")
                    .replace(" ", "")
                    .replace("%", "")
        }
        println(rankCSV)


//        for line in listOfLines:
//        line = line.replace("|", ",").replace(" ", "").replace("%", "")
//        if line.startswith(","):
//        line = line[1:-2]
//        outlist.append(line)
//        return outlist

        //DEX -> dictonary
        //Date -> year-month
        //Tier -> gen + entryProcotol.gen + entryProtocol.tier

        val json = objectMapper.readValue(response.body(),object : TypeReference<Pokemon>(){})
        // TODO Converter retorno em txt para um objeto ou igual o original converter para um CSV


    }

    private fun getUrl(entryProtocol: EntryProtocol): String {

        val gtr = String.format("gen%s%s-%s",entryProtocol.gen, entryProtocol.tier, entryProtocol.rating)
        return  "https://www.smogon.com/stats/" +
                "${entryProtocol.year}-${entryProtocol.month}/${gtr}.txt"
    }

}
