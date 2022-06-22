package br.com.smogon.competitiveSmogon

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


open class Knight : NamedCharacter(), StyleWeapon {
    val weapon: StyleWeaponImpl? = null
}

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
        JsonSubTypes.Type(TypeWeapon::class),
        JsonSubTypes.Type(AtributesWeapon::class))
interface StyleWeapon

open class StyleWeaponImpl : StyleWeapon {
    val name: String? = null // standard setters and getters
}

class TypeWeapon: StyleWeaponImpl() {
    val physical: String? = null // standard setters and getters
}

class AtributesWeapon: StyleWeaponImpl() {
    val weight: Int = 0 // standard setters and getters
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

class ImperialSpy : Character


@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
        JsonSubTypes.Type(ImperialSpy::class),
        JsonSubTypes.Type(King::class),
        JsonSubTypes.Type(Knight::class))
interface Character


open class NamedCharacter : Character {
    val name: String? = null // standard setters and getters
}


class King : NamedCharacter() {
    val land: String? = null // standard setters and getters
}

fun formatJson(input: String): String {
    return input.replace("'".toRegex(), "\"")
}

class JsonTypeTests {

    private val objectMapper = jacksonObjectMapper()


    @Test
    fun givenAKingWithoutType_whenMapping_thenExpectAnError() {
        val kingJson: String = formatJson("{'name': 'Old King Allant', 'land':'Boletaria'}")
        assertThrows(InvalidTypeIdException::class.java) { objectMapper.readValue(kingJson, Character::class.java) }
    }

    @Test
    @Throws(Exception::class)
    fun givenAKing_whenMapping_thenExpectAKingType() {
        val kingJson = formatJson("{'name': 'Old King Allant', 'land':'Boletaria', '@type':'King'}")
        val character: Character = objectMapper.readValue(kingJson, Character::class.java)
        assertTrue(character is King)
        assertSame(character.javaClass, King::class.java)
        val king = character as King
        assertEquals("Boletaria", king.land)
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun givenAKnight_whenMapping_thenExpectAKnightTypewithTypeWeapon() {
        val knightJson = formatJson("{'name':'Ostrava, of Boletaria', 'weapon':{'name':'sword', 'physical': 'Rune Sword'}}")
        val character = objectMapper.readValue(knightJson, Character::class.java)
        assertTrue(character is Knight)
        assertSame(character.javaClass, Knight::class.java)
        val king = character as Knight
        assertEquals("Ostrava, of Boletaria", king.name)
        val weapon = king.weapon as TypeWeapon
        assertEquals("Rune Sword", weapon.physical)
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun givenAKnight_whenMapping_thenExpectAKnightTypewithWeightWeapon() {
        val knightJson = formatJson("{'name':'Ostrava, of Boletaria', 'weapon':{'name':'sword', 'weight': 340}}")
        val character = objectMapper.readValue(knightJson, Character::class.java)
        assertTrue(character is Knight)
        assertSame(character.javaClass, Knight::class.java)
        val king = character as Knight
        assertEquals("Ostrava, of Boletaria", king.name)
        val weapon = king.weapon as AtributesWeapon
        assertEquals(340, weapon.weight)
    }


    @Test
    @Throws(java.lang.Exception::class)
    fun givenAKnight_whenMapping_thenExpectAKnightTypewithWeightWeaponHashMap() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val knightJson = formatJson("{'name':'Ostrava, of Boletaria', 'weapon':{'Jolly':{'0/252/0/0/4/252': '46.864%'},'name':'sword', 'weight': 340}}")
        val character = objectMapper.readValue(knightJson, Character::class.java)
        assertTrue(character is Knight)
        assertSame(character.javaClass, Knight::class.java)
        val king = character as Knight
        assertEquals("Ostrava, of Boletaria", king.name)
        val weapon = king.weapon as AtributesWeapon
        assertEquals(340, weapon.weight)
        val att = weapon.any()?.filterKeys { it?.contains("Jolly") ?: false }
        assertEquals("46.864%", att?.get("Jolly")?.get("0/252/0/0/4/252"))
    }
}