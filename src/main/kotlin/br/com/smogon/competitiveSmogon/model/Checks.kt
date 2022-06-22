package br.com.smogon.competitiveSmogon.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Checks(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int,
        val name : String,
        val ko: String,
        val switched: String
)
