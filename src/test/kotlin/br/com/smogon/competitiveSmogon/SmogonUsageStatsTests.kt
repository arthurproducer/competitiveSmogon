package br.com.smogon.competitiveSmogon

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

//DynamicSpreadsMapper
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
        JsonSubTypes.Type(SpreadsMapper::class))
interface SmogonUsageStatsMapperInterface

// SmogonUsageStatsMapper
open class NamedSmogonUsageStatsMapper : SmogonUsageStatsMapperInterface {
    val pokemon: String? = null
}

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
        JsonSubTypes.Type(Natures::class))
interface SpreadsInterface

open class SpreadsMapper : NamedSmogonUsageStatsMapper(), SpreadsInterface {
    val spreads: Natures? = null // standard setters and getters
}

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
class Natures {
    @JsonProperty("Other")
    val other: String? = null
    val natures: HashMap<String?, HashMap<String?,String?>>? = HashMap()

    @JsonAnyGetter
    fun any(): HashMap<String?, HashMap<String?,String?>>? {
        return natures
    }

    @JsonAnySetter
    operator fun set(name: String?, value: HashMap<String?,String?>) {
        natures?.set(name, value)
    }
}

class SmogonUsageStatsTests {
    private val objectMapper = jacksonObjectMapper()

    @Test
    @Throws(java.lang.Exception::class)
    fun givenASpread_whenMapping_thenExpectASpreadTypewithNaturesAndOtherHashMap() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val smogonJson = formatJson("{'pokemon':'Garchomp', 'spreads':{'Jolly':{'0/252/0/0/4/252': '46.864%'}, 'Other':'46%'}}")

        val character = objectMapper.readValue(smogonJson, SmogonUsageStatsMapperInterface::class.java)
        Assertions.assertTrue(character is SpreadsMapper)
        Assertions.assertSame(character.javaClass, SpreadsMapper::class.java)
        val king = character as SpreadsMapper
        Assertions.assertEquals("Garchomp", king.pokemon)
        val weapon = king.spreads as Natures
//        Assertions.assertEquals(340, weapon)
        val att = weapon.any()?.filterKeys { it?.contains("Jolly") ?: false }
        Assertions.assertEquals("46.864%", att?.get("Jolly")?.get("0/252/0/0/4/252"))
    }
}