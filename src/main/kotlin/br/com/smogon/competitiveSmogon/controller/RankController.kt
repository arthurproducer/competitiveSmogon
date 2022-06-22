package br.com.smogon.competitiveSmogon.controller

import br.com.smogon.competitiveSmogon.model.EntryProtocol
import br.com.smogon.competitiveSmogon.model.PokemonDetailsView
import br.com.smogon.competitiveSmogon.model.PokemonRankView
import br.com.smogon.competitiveSmogon.service.RankService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rank")
class RankController(private val rankService: RankService) {

    @GetMapping("/{year}/{month}/{gen}{tier}/{rating}")
    fun listar(@PathVariable year: String,
               @PathVariable month: String,
               @PathVariable gen: String,
               @PathVariable tier: String,
               @PathVariable rating: String
    ): List<PokemonRankView> {
    return rankService.listar(year,month,gen,tier,rating)
    }

    @GetMapping("/{year}/{month}/{gen}{tier}/{rating}/{name}")
    fun detalhar(
            @PathVariable year: String,
            @PathVariable month: String,
            @PathVariable gen: String,
            @PathVariable tier: String,
            @PathVariable rating: String,
            @PathVariable name: String): PokemonDetailsView {
        val entryProtocol = EntryProtocol(year, month, gen, tier, rating)
        return rankService.findByName(entryProtocol, name)
    }

}