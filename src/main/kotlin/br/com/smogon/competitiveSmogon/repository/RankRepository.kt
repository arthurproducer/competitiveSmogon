package br.com.smogon.competitiveSmogon.repository

import br.com.smogon.competitiveSmogon.model.Pokemon
import org.springframework.data.jpa.repository.JpaRepository

interface RankRepository: JpaRepository<Pokemon, Long> {

}
