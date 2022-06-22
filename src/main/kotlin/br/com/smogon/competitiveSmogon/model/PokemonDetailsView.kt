package br.com.smogon.competitiveSmogon.model

import java.net.URL
import javax.persistence.*

@Entity
data class PokemonDetailsView(
        val tier: String? = null,
        var pokemon: String,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var rank: Long,
        var usage: String,
        var raw: String,
        var dex: Long? = null,
        val date: String? = null,
        val image: URL? = null,
        val type_1: String? = null,
        val type_2: String? = null,
        @OneToMany
        val abilities: List<Abilities?> = listOf(),
        @OneToMany
        val items: List<Items?> = listOf(),
        @OneToMany
        val spreads: List<Spreads?> = listOf(),
        @OneToMany
        val moves: List<Moves?> = listOf(),
        @OneToMany
        val teammates: List<Teammates?> = listOf(),
        @OneToMany
        val checks: List<Checks?> = listOf()
)