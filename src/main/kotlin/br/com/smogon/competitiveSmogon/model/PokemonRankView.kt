package br.com.smogon.competitiveSmogon.model

import java.net.URL
import javax.persistence.*

@Entity
data class PokemonRankView(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var rank: Long,
        var pokemon: String,
        var dex: Long? = null,
        var usage_pct: Double,
        var raw_usage: Double,
        var raw_pct: Double,
        var real: Double,
        var real_pct: Double,
        val date: String? = null,
        val tier: String? = null,
        val previous_month: Long? = null,
        val image: URL?= null
)