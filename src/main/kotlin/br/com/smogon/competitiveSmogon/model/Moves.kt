package br.com.smogon.competitiveSmogon.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Moves(
        val name: String,
        val type: String? = null,
        val usage: String,
        val power: Int? = 0,
        val pp: Int? = 0,
        val accuracy: Int? = 0,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = 0,
        val damage_class: String? = null
)
