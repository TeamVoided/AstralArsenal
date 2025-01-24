@file:Suppress("HasPlatformType")

package org.teamvoided.astralarsenal.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.dynamic.Codecs
import java.util.*

interface SimpleStorageComponents

// The suppression is here to not show warnings for the bad names on EXAMPLE_COMPONENT
@Suppress("ClassName", "PropertyName", "unused")
data class EXAMPLE_COMPONENT(val VALUE: Int) : SimpleStorageComponents {
    companion object {
        fun default(): EXAMPLE_COMPONENT = EXAMPLE_COMPONENT(0)
        val CODEC = Codec.INT.xmap({ int -> EXAMPLE_COMPONENT(int) }, { component -> component.VALUE })
    }
}


data class CapacitanceData(val damage: Float) : SimpleStorageComponents {
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
) : SimpleStorageComponents {
    companion object {
        fun default(): CapacitanceDataV2 = CapacitanceDataV2(0, 0)
        val CODEC = Codecs.NONNEGATIVE_INT.listOf().xmap(
            { list -> CapacitanceDataV2(list[0], list[1]) },
            { data -> listOf(data.dischargeTime, data.countdownTime) }
        )
    }
}

data class TotemData(val target: UUID?) : SimpleStorageComponents {
    companion object {
        fun default(): TotemData = TotemData(null)
        val CODEC = Codecs.ESCAPED_STRING.xmap(
            { string -> TotemData(if (string.isEmpty()) null else UUID.fromString(string)) },
            { data -> data.target.toString() }
        )
    }
}