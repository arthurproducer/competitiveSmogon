package br.com.smogon.competitiveSmogon.util

import br.com.smogon.competitiveSmogon.model.Pokedex
import br.com.smogon.competitiveSmogon.model.PokedexData
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import java.nio.file.Paths

@Component
class Commons {

    fun createDex() : List<Pokedex> {
        val objectMapper = jacksonObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val json = objectMapper.readValue<PokedexData>(Paths.get("src/main/resources/assets/pokedex.json").toFile())
//        val json = objectMapper.readValue(Paths.get("assets/pokedex.json").toFile(),PokedexData.class)
        return ArrayList<Pokedex>(json.data.values)
    }

    fun findDex(name: String, pokedex: List<Pokedex>): Pokedex? {
        val poke = pokedex.filter { it.name.equals(name,true) }
        return if(poke.isEmpty()) {
            null
        } else {
            poke[0]
        }
    }
}