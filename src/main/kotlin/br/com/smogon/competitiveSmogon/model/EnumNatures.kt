package br.com.smogon.competitiveSmogon.model

import java.util.*

enum class EnumNatures {
    ADAMANT,
    LONELY,
    NAUGHTY,
    BRAVE,
    BOLD,
    IMPISH,
    LAX,
    RELAXED,
    MODEST,
    MILD,
    RASH,
    QUIET,
    CALM,
    GENTLE,
    CAREFUL,
    SASSY,
    TIMID,
    HASTY,
    JOLLY,
    NAIVE,
    BASHFUL,
    DOCILE,
    HARDY,
    QUIRKY,
    SERIOUS
}

inline fun <reified K : Enum<K>, V> List<V>.enumMapOf(keys:Array<K>): EnumMap<K, V> {
    val map = EnumMap<K, V>(K::class.java)
    keys.forEachIndexed{i,k-> map[k]=this[i]}
    return map
}

inline fun <reified T : Enum<T>> enumContains(name: String?): Boolean {
    return enumValues<T>().any { it.name == name}
}