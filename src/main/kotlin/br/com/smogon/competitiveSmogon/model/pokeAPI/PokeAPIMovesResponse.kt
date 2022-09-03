package br.com.smogon.competitiveSmogon.model.pokeAPI

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
data class PokeAPIMovesResponse(
        val accuracy: String?,
        @OneToOne
        val damage_class: DamageClassResponse?,
        @Id
        val id: String?,
        val name: String?,
        val power: String?,
        val pp: String?,
        @OneToOne
        val type: TypeResponse?
        )
