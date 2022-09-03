package br.com.smogon.competitiveSmogon.model.pokeAPI

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class DamageClassResponse (
        @Id
        val name: String?,
        val url: String?
        )
