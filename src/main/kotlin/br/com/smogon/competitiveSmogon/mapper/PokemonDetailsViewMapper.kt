package br.com.smogon.competitiveSmogon.mapper

import br.com.smogon.competitiveSmogon.model.*
import br.com.smogon.competitiveSmogon.model.smogon_usage_stats.SmogonUsageStats
import br.com.smogon.competitiveSmogon.model.smogon_usage_stats.SpreadsMapper
import org.springframework.stereotype.Component

@Component
class PokemonDetailsViewMapper: Mapper<SmogonUsageStats, PokemonDetailsView> {
    override fun map(t: SmogonUsageStats): PokemonDetailsView {

        return PokemonDetailsView(
                tier = t.tier,
                pokemon = t.pokemon,
                rank = t.rank.toLong(),
                usage = t.usage,
                raw = t.raw,
                dex = null,
                date = null,
                image = null,
                type_1 = null,
                type_2 = null,
                abilities = abilitiesMap(t),
                items = itemMap(t),
                spreads = spreadMap(t),
                moves = movesMap(t),
                teammates = teammatesMap(t),
                checks = checkMap(t)
        )
    }

    private fun abilitiesMap(t: SmogonUsageStats): List<Abilities> {
        val listAbilities = arrayListOf<Abilities>()
        var count = 0
        t.abilities.map {
            listAbilities.add(Abilities(
                    name = it.component1(),
                    usage = it.component2(),
                    slot = count+1
            ))
            count += 1
        }
        return listAbilities
    }

    private fun itemMap(t: SmogonUsageStats): List<Items> {
        val listItems = arrayListOf<Items>()
        t.items.map {
            listItems.add(Items(
                    name = it.component1(),
                    usage = it.component2()
            ))
        }
        return listItems
    }

    private fun spreadMap(t: SmogonUsageStats): List<Spreads> {
        val listSpreads = arrayListOf<Spreads>()
        val listNatures: HashMap<String?, HashMap<String?,String?>>?
        val spreads = t as SpreadsMapper

        listNatures = spreads.spreads?.any()?.filterKeys { enumContains<EnumNatures>(it?.uppercase()) } as HashMap<String?, HashMap<String?, String?>>?
        listNatures?.toList()?.map {
            val converted = Spreads()
            converted.nature = it.first.toString()
            it.second.toList().map { s ->
                converted.spread_complete = s.first
                converted.usage = s.second
                val splitSpreadsFromNature = s.first?.split(",")
                splitSpreadsFromNature?.forEach {convertedSpread ->
                    val spread = convertedSpread.split("/")
                    converted.hp = spread[0].toIntOrNull()
                    converted.attack = spread[1].toIntOrNull()
                    converted.defense = spread[2].toIntOrNull()
                    converted.sp_atk = spread[3].toIntOrNull()
                    converted.sp_def = spread[4].toIntOrNull()
                    converted.speed = spread[5].toIntOrNull()
                    listSpreads.add(Spreads(
                            nature = converted.nature,
                            spread_complete = converted.spread_complete,
                            usage = converted.usage,
                            hp = converted.hp,
                            attack = converted.attack,
                            defense = converted.defense,
                            sp_atk = converted.sp_atk,
                            sp_def = converted.sp_def,
                            speed = converted.speed
                    ))
                }
            }
        }
        listSpreads.add(Spreads(
                nature = "Others",
                usage = spreads.spreads?.other,
        ))

        return listSpreads
    }

    private fun movesMap(t: SmogonUsageStats): List<Moves> {
        val listMoves = arrayListOf<Moves>()
        t.moves.map {
            listMoves.add(Moves(
                    name = it.component1(),
                    usage = it.component2()
            ))
        }
        return listMoves
    }

    private fun teammatesMap(t: SmogonUsageStats): List<Teammates> {
        val listTeammates = arrayListOf<Teammates>()
        t.teammates.map {
            listTeammates.add(Teammates(
                    name = it.component1(),
                    usage = it.component2()
            ))
        }
        return listTeammates
    }

    private fun checkMap(t: SmogonUsageStats): List<Checks> {
        val listChecks = arrayListOf<Checks>()
        t.checks.map {
            listChecks.add(Checks(
                    name = it.component1(),
                    ko = it.component2().ko,
                    switched = it.component2().switched
            ))
        }
        return listChecks
    }

}