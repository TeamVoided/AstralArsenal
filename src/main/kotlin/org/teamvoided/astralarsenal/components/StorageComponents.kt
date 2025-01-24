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

data class CometLauncherData(val uses: Int, val cooldown: Int): SimpleStorageComponent {
    companion object {
        fun default(): CometLauncherData = CometLauncherData(0, 0)
        val CODEC = Codecs.NONNEGATIVE_INT.listOf().xmap(
            { list -> CometLauncherData(list[0], list[1]) },
            { data -> listOf(data.uses, data.cooldown) }
        )
    }
}

data class GrappleData(val jumps: Int, val timer: Int, val negateFallDamage: Boolean) : SimpleStorageComponent {
    companion object {
        fun default(): GrappleData = GrappleData(0, 0, false)
        val CODEC = RecordCodecBuilder.create<GrappleData> { builder ->
            builder.group(
                Codec.INT.fieldOf("jumps").forGetter { it.jumps },
                Codec.INT.fieldOf("timer").forGetter { it.timer },
                Codec.BOOL.fieldOf("negateFallDamage").forGetter { it.negateFallDamage },
            ).apply(builder, ::GrappleData)
        }
    }
}

data class AlchemistData(val contents: Optional<PotionContentsComponent>, val charges: Int) :
    SimpleStorageComponent {
    companion object {
        fun default(): AlchemistData = AlchemistData(Optional.empty(), 0)
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
        fun default(): PulveriserData = PulveriserData(0, false)
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
        fun default(): AstralRainData = AstralRainData(0)
        val CODEC = RecordCodecBuilder.create<AstralRainData> { builder ->
            builder.group(Codec.INT.fieldOf("ticks").forGetter { it.charges })
                .apply(builder, ::AstralRainData)
        }
    }
}

data class SnipeDataV1(val ticks: Int, val loaded: Boolean) : SimpleStorageComponent {
    companion object {
        fun default(): SnipeDataV1 = SnipeDataV1(0, false)
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
        fun default(): CapacitanceDataV1 = CapacitanceDataV1(0f)
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
        fun default(): CapacitanceDataV2 = CapacitanceDataV2(0, 0)
        val CODEC = Codecs.NONNEGATIVE_INT.listOf().xmap(
            { list -> CapacitanceDataV2(list[0], list[1]) },
            { data -> listOf(data.dischargeTime, data.countdownTime) }
        )
    }
}

data class TotemData(val target: UUID?) : SimpleStorageComponent {
    companion object {
        fun default(): TotemData = TotemData(null)
        val CODEC = Codecs.ESCAPED_STRING.xmap(
            { string -> TotemData(if (string.isEmpty()) null else UUID.fromString(string)) },
            { data -> data.target.toString() }
        )
    }
}