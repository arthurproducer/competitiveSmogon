package br.com.smogon.competitiveSmogon.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Abilities(
        val name : String,
        val usage: String,
        val is_hidden: Boolean? = false,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val slot: Int
)
