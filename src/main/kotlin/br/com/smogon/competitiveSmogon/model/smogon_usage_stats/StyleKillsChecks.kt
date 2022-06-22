package br.com.smogon.competitiveSmogon.model.smogon_usage_stats

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class StyleKillsChecks(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        var ko: String,
        var switched: String
)
