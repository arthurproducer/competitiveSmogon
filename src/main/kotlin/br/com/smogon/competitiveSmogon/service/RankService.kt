package br.com.smogon.competitiveSmogon.service

import br.com.smogon.competitiveSmogon.repository.RankRepository
import org.springframework.stereotype.Service

@Service
class RankService(private val rankRepository: RankRepository) {
    fun listar(year: String, month: String, gen: String, tier: String, rating: String) {
        TODO("chamar a repository que vai ficar responsável por pegar os dados na smogon")
    }
}