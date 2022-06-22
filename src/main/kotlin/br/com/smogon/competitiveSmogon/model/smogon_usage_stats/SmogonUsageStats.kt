package br.com.smogon.competitiveSmogon.model.smogon_usage_stats

import javax.persistence.*

@Entity
open class SmogonUsageStats : DynamicSpreadsInterface {
        val tier : String = ""
        val pokemon: String = ""
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var rank: String = ""
        val usage: String = ""
        val raw: String = ""
        val abilities: HashMap<String, String> = HashMap()
        val items: HashMap<String, String> = HashMap()
        val moves: HashMap<String, String> = HashMap()
        val teammates: HashMap<String, String> = HashMap()
        val checks: HashMap<String, StyleKillsChecks> = HashMap()
}
