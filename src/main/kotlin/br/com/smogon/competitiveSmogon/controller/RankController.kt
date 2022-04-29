package br.com.smogon.competitiveSmogon.controller

import br.com.smogon.competitiveSmogon.model.Pokemon
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
    ): List<Pokemon> {
    return rankService.listar(year,month,gen,tier,rating)
    }

}