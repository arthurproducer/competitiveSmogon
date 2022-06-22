package br.com.smogon.competitiveSmogon.model.smogon_usage_stats

import com.fasterxml.jackson.annotation.*
import javax.persistence.*

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
        JsonSubTypes.Type(SpreadsMapper::class))
interface DynamicSpreadsInterface  {}

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
        JsonSubTypes.Type(value = Natures::class)
)
interface SpreadsInterface {}

@Entity
open class SpreadsMapper : SmogonUsageStats(), SpreadsInterface {
        @ManyToOne
        val spreads: Natures? = null
}

@Entity
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
class Natures {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id : Long? = null
        @JsonProperty("Other")
        val other: String? = null
        val natures: HashMap<String?, HashMap<String?,String?>>? = HashMap()

        @JsonAnyGetter
        fun any(): HashMap<String?, HashMap<String?,String?>>? {
                return natures
        }

        @JsonAnySetter
        operator fun set(name: String?, value: HashMap<String?,String?>) {
                natures?.set(name, value)
        }
}

