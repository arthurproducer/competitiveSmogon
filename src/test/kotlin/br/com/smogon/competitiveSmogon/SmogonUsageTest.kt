package br.com.smogon.competitiveSmogon

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


open class SpreadsMapperNew : NamedSmogonUsageStatsMapperInterfaceNew(), StyleWeaponD {
    val spreads: StyleWeaponImplD? = null
}

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
        JsonSubTypes.Type(TypeWeaponD::class),
        JsonSubTypes.Type(AtributesWeaponD::class))
interface StyleWeaponD

open class StyleWeaponImplD : StyleWeaponD {
}

class TypeWeaponD: StyleWeaponImplD() {
    val physical: String? = null // standard setters and getters
}

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
class AtributesWeaponD: StyleWeaponImplD() {
    @JsonProperty("Other")
    val other: String? = null // standard setters and getters
//    val weight: Int = 0 // standard setters and getters
    val atribute: HashMap<String?,HashMap<String?, String?>> = HashMap()


    @JsonAnyGetter
    fun any(): HashMap<String?, HashMap<String?, String?>>? {
        return atribute
    }

    @JsonAnySetter
    operator fun set(name: String?, value: HashMap<String?, String?>) {
        atribute[name] = value
    }


}

class ImperialSpyD : SmogonUsageStatsMapperInterfaceNew


@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
        JsonSubTypes.Type(ImperialSpyD::class),
        JsonSubTypes.Type(KingD::class),
        JsonSubTypes.Type(SpreadsMapperNew::class))
interface SmogonUsageStatsMapperInterfaceNew


open class NamedSmogonUsageStatsMapperInterfaceNew : SmogonUsageStatsMapperInterfaceNew {
    val pokemon: String? = null // standard setters and getters
}


class KingD : NamedSmogonUsageStatsMapperInterfaceNew() {
    val land: String? = null // standard setters and getters
}

class SmogonUsageTest {

    private val objectMapper = jacksonObjectMapper()


    @Test
    @Throws(java.lang.Exception::class)
    fun givenAKnight_whenMapping_thenExpectAKnightTypewithWeightWeaponHashMap() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val knightJson = formatJson("{'pokemon':'Gastrodon', 'spreads':{'Jolly':{'0/252/0/0/4/252': '46.864%'},'Other':'27.859%'}}")
        val character = objectMapper.readValue(knightJson, SmogonUsageStatsMapperInterfaceNew::class.java)
        assertTrue(character is SpreadsMapperNew)
        assertSame(character.javaClass, SpreadsMapperNew::class.java)
        val king = character as SpreadsMapperNew
        assertEquals("Gastrodon", king.pokemon)
        val weapon = king.spreads as AtributesWeaponD
//        assertEquals(340, weapon.weight)
        val att = weapon.any()?.filterKeys { it?.contains("Jolly") ?: false }
        assertEquals("46.864%", att?.get("Jolly")?.get("0/252/0/0/4/252"))
    }
}