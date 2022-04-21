package br.com.smogon.competitiveSmogon.model

import java.net.URL
import javax.persistence.*

@Entity
data class Pokemon(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        var rank: Long,
        var pokemon: String,
        var dex: Long,
        var usage_pct: Double,
        var raw_usage: Double,
        var raw_pct: Double,
        var real: Double,
        var real_pct: Double,
        val date: String,
        val tier: String,
        val previous_month: Long,
        val image: URL
)