@file:Suppress("HasPlatformType")

package org.teamvoided.astralarsenal.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.component.type.PotionContentsComponent
import net.minecraft.util.dynamic.Codecs
import java.util.*

/*

    Examples cant be found in - SimpleStorageComponent.kt

 */

data class JumpData(val uses: Int, val cooldown: Int, val lastJump: Int, val maxUses: Int) :
    SimpleStorageComponent {
    companion object {
        val DEFAULT: JumpData = JumpData(0, 0, 0, 0)
        val CODEC = Codecs.NONNEGATIVE_INT.listOf().xmap(
            { list -> JumpData(list[0], list[1], list.getOrNull(2) ?: 0, list[3]) },
            { data -> listOf(data.uses, data.cooldown, data.lastJump, data.maxUses) }
        )
    }
}

data class DashData(val uses: Int, val cooldown: Int) : SimpleStorageComponent {
    companion object {
        val DEFAULT: DashData = DashData(0, 0)
        val CODEC = Codecs.NONNEGATIVE_INT.listOf().xmap(
            { list -> DashData(list[0], list[1]) },
            { data -> listOf(data.uses, data.cooldown) }
        )
    }
}

data class DodgeData(val uses: Int, val cooldown: Int) : SimpleStorageComponent {
    companion object {
        val DEFAULT: DodgeData = DodgeData(0, 0)
        val CODEC = Codecs.NONNEGATIVE_INT.listOf().xmap(
            { list -> DodgeData(list[0], list[1]) },
            { data -> listOf(data.uses, data.cooldown) }
        )
    }
}

data class SlamData(val lastFallDistance: Float, val slamming: Boolean) : SimpleStorageComponent {
    companion object {
        val DEFAULT: SlamData = SlamData(0f, false)
        val CODEC = RecordCodecBuilder.create<SlamData> { builder ->
            builder.group(
                Codec.FLOAT.fieldOf("lastFallDistance").forGetter { it.lastFallDistance },
                Codec.BOOL.fieldOf("slamming").forGetter { it.slamming }
            ).apply(builder, ::SlamData)
        }
    }
}

data class AstralStrikeData(val hitTimes: Int) : SimpleStorageComponent {
    companion object {
        val DEFAULT: AstralStrikeData = AstralStrikeData(0)
        val CODEC = Codecs.NONNEGATIVE_INT.listOf().xmap(
            { list -> AstralStrikeData(list[0]) },
            { data -> listOf(data.hitTimes) }
        )
    }
}

data class NailCannonDataV1(val uses: Int, val beingUsed: Int) : SimpleStorageComponent {
    override fun equals(other: Any?): Boolean {
        return if (other == null || other !is NailCannonDataV1) return super.equals(other)
        else true
    }

    companion object {
        val DEFAULT: NailCannonDataV1 = NailCannonDataV1(0, 0)
        val CODEC = Codecs.NONNEGATIVE_INT.listOf().xmap(
            { list -> NailCannonDataV1(list[0], list[1]) },
            { data -> listOf(data.uses, data.beingUsed) }
        )
    }
}

data class NailCannonCooldownData(val cooldown: Int, val fireCooldown: Int) : SimpleStorageComponent {
    override fun equals(other: Any?): Boolean {
        return if (other == null || other !is NailCannonCooldownData) return super.equals(other)
        else true
    }

    companion object {
        val DEFAULT: NailCannonCooldownData = NailCannonCooldownData(0, 0)
        val CODEC = Codecs.NONNEGATIVE_INT.listOf().xmap(
            { list -> NailCannonCooldownData(list[0], list[1]) },
            { data -> listOf(data.cooldown, data.fireCooldown) }
        )
    }
}

data class AlchemistData(val contents: Optional<PotionContentsComponent>, val charges: Int) :
    SimpleStorageComponent {
    companion object {
        val DEFAULT: AlchemistData = AlchemistData(Optional.empty(), 0)
        val CODEC = RecordCodecBuilder.create<AlchemistData> { builder ->
            builder.group(
                PotionContentsComponent.CODEC.lenientOptionalFieldOf("contents").forGetter { it.contents },
                Codec.INT.fieldOf("charges").orElse(0).forGetter { it.charges }
            ).apply(builder, ::AlchemistData)
        }
    }
}

data class PulveriserData(val ticks: Int, val slamming: Boolean) : SimpleStorageComponent {
    companion object {
        val DEFAULT: PulveriserData = PulveriserData(0, false)
        val CODEC = RecordCodecBuilder.create<PulveriserData> { builder ->
            builder.group(
                Codec.INT.fieldOf("ticks").forGetter { it.ticks },
                Codec.BOOL.fieldOf("slamming").forGetter { it.slamming }
            ).apply(builder, ::PulveriserData)
        }
    }
}

data class AstralRainData(val charges: Int) : SimpleStorageComponent {
    companion object {
        @JvmField
        val DEFAULT: AstralRainData = AstralRainData(0)
        val CODEC = RecordCodecBuilder.create<AstralRainData> { builder ->
            builder.group(Codec.INT.fieldOf("ticks").forGetter { it.charges })
                .apply(builder, ::AstralRainData)
        }
    }
}

data class SnipeDataV1(val ticks: Int, val loaded: Boolean) : SimpleStorageComponent {
    companion object {
        val DEFAULT: SnipeDataV1 = SnipeDataV1(0, false)
        val CODEC = RecordCodecBuilder.create<SnipeDataV1> { builder ->
            builder.group(
                Codec.INT.fieldOf("ticks").forGetter { it.ticks },
                Codec.BOOL.fieldOf("slamming").forGetter { it.loaded }
            ).apply(builder, ::SnipeDataV1)
        }
    }
}

data class CapacitanceDataV1(val damage: Float) : SimpleStorageComponent {
    companion object {
        val DEFAULT: CapacitanceDataV1 = CapacitanceDataV1(0f)
        val CODEC = RecordCodecBuilder.create<CapacitanceDataV1> { builder ->
            builder.group(Codec.FLOAT.fieldOf("ticks").forGetter { it.damage })
                .apply(builder, ::CapacitanceDataV1)
        }
    }
}

data class CapacitanceDataV2(
    val dischargeTime: Int, // dischargeTime is the time during witch discharge is possible, countdown time is time until the discharge starts
    val countdownTime: Int,
) : SimpleStorageComponent {
    companion object {
        val DEFAULT: CapacitanceDataV2 = CapacitanceDataV2(0, 0)
        val CODEC = Codecs.NONNEGATIVE_INT.listOf().xmap(
            { list -> CapacitanceDataV2(list[0], list[1]) },
            { data -> listOf(data.dischargeTime, data.countdownTime) }
        )
    }
}

data class TotemData(val target: UUID?) : SimpleStorageComponent {
    companion object {
        val DEFAULT: TotemData = TotemData(null)
        val CODEC = Codecs.ESCAPED_STRING.xmap(
            { string -> TotemData(if (string.isEmpty()) null else UUID.fromString(string)) },
            { data -> data.target.toString() }
        )
    }
}

data class SludgeCooldownData(val cooldown: Int, val uses: Int) :
    SimpleStorageComponent {
    companion object {
        val DEFAULT: SludgeCooldownData = SludgeCooldownData(0, 0)
        val CODEC = RecordCodecBuilder.create<SludgeCooldownData> { builder ->
            builder.group(
                Codec.INT.fieldOf("cooldown").forGetter { it.cooldown },
                Codec.INT.fieldOf("uses").forGetter { it.uses }
            ).apply(builder, ::SludgeCooldownData)
        }

    }
}