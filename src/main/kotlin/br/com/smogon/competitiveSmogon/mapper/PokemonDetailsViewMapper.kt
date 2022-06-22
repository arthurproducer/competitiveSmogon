package br.com.smogon.competitiveSmogon.mapper

import br.com.smogon.competitiveSmogon.model.*
import br.com.smogon.competitiveSmogon.model.smogon_usage_stats.SmogonUsageStats
import br.com.smogon.competitiveSmogon.model.smogon_usage_stats.SpreadsMapper
import org.springframework.stereotype.Component
import java.util.EnumMap
import java.util.stream.Collectors
import kotlin.streams.toList

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
                abilities = listOf(),
                items = listOf(),
                spreads = spreadMap(t),
                moves = listOf(),
                teammates = listOf(),
                checks = listOf()
        )
    }
    private fun spreadMap(t: SmogonUsageStats): List<Spreads> {
        val listSpreds = arrayListOf<Spreads>()
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
                    println(converted)
                    listSpreds.add(Spreads(
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
                    println(listSpreds)
                }
            }
        }
        return listSpreds
    }
}