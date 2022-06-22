package br.com.smogon.competitiveSmogon.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Spreads(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        var nature: String? = null,
        var spread_complete: String? = null,
        var usage: String? = null,
        var hp: Int? = null,
        var attack: Int? = null,
        var defense: Int? = null,
        var sp_atk: Int? = null,
        var sp_def: Int? = null,
        var speed: Int? = null
) {
        constructor() : this (null)
}
