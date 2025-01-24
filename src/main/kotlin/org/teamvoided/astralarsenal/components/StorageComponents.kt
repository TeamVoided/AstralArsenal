@file:Suppress("HasPlatformType")

package org.teamvoided.astralarsenal.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.dynamic.Codecs
import java.util.*

/*

    Examples cant be found in - StorageComponents.kt

 */


data class CapacitanceData(val damage: Float) : SimpleStorageComponent {
    companion object {
        fun default(): CapacitanceData = CapacitanceData(0f)
        val CODEC = RecordCodecBuilder.create<CapacitanceData> { builder ->
            builder.group(Codec.FLOAT.fieldOf("ticks").forGetter { it.damage })
                .apply(builder, ::CapacitanceData)
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