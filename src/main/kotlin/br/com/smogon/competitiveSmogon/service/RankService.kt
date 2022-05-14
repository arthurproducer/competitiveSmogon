package br.com.smogon.competitiveSmogon.service

import br.com.smogon.competitiveSmogon.model.EntryProtocol
import br.com.smogon.competitiveSmogon.model.Pokemon
import br.com.smogon.competitiveSmogon.repository.RankRepository
import org.springframework.stereotype.Service

@Service
class RankService(private val rankRepository: RankRepository) {
    fun listar(year: String, month: String, gen: String, tier: String, rating: String) : List<Pokemon>  {
        val entryProtocol = EntryProtocol(year, month, gen, tier, rating)
        return rankRepository.doRequest(entryProtocol)
    }
}