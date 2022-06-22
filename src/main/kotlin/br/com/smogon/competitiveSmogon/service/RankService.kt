package br.com.smogon.competitiveSmogon.service

import br.com.smogon.competitiveSmogon.model.EntryProtocol
import br.com.smogon.competitiveSmogon.model.PokemonDetailsView
import br.com.smogon.competitiveSmogon.model.PokemonRankView
import br.com.smogon.competitiveSmogon.repository.RankRepository
import org.springframework.stereotype.Service

@Service
class RankService(private val rankRepository: RankRepository) {
    fun listar(year: String, month: String, gen: String, tier: String, rating: String) : List<PokemonRankView>  {
        val entryProtocol = EntryProtocol(year, month, gen, tier, rating)
        return rankRepository.doRequestRankList(entryProtocol)
    }

    fun findByName(entryProtocol: EntryProtocol, name: String): PokemonDetailsView {
        return rankRepository.doRequestRankDetails(entryProtocol,name)
//                .orElseThrow { NotFoundException(notFoundMessage) }
//        return topicoViewMapper.map(topico)
    }
}