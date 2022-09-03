package br.com.smogon.competitiveSmogon.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Moves(
        val name: String,
        var type: String? = null,
        val usage: String,
        var power: Int? = 0,
        var pp: Int? = 0,
        var accuracy: Int? = 0,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = 0,
        var damage_class: String? = null
)
