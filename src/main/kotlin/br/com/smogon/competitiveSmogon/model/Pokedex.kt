package br.com.smogon.competitiveSmogon.model

data class PokedexData(
        val data : HashMap<String, Pokedex>
)

data class Pokedex(
        val pokedex_number: Long,
        val name: String,
        val type_1: String,
        val type_2: String?,
        val ability_1: String?,
        val ability_2: String?,
        val ability_hidden: String?,
        val total_points: Long,
        val hp: Int,
        val attack: Int,
        val defense: Int,
        val sp_attack: Int,
        val sp_defense: Int,
        val speed: Int,
        val against_normal : Float,
        val against_fire : Float,
        val against_water : Float,
        val against_grass : Float,
        val against_electric : Float,
        val against_ground : Float,
        val against_rock : Float,
        val against_dark : Float,
        val against_bug : Float,
        val against_psychic : Float,
        val against_poison : Float,
        val against_steel : Float,
        val against_ice : Float,
        val against_fight : Float,
        val against_flying : Float,
        val against_ghost : Float,
        val against_dragon : Float,
        val against_fairy : Float
)