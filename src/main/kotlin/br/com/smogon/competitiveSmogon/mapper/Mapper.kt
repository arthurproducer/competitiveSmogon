package br.com.smogon.competitiveSmogon.mapper

interface Mapper<T, U> {
    fun map(t:T): U
}