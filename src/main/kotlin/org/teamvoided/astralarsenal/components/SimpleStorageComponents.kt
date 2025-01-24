@file:Suppress("HasPlatformType")

package org.teamvoided.astralarsenal.components

import com.mojang.serialization.Codec
import net.minecraft.util.dynamic.Codecs
import java.util.*

interface SimpleStorageComponents

@Suppress("ClassName", "PropertyName", "unused")
data class EXAMPLE_COMPONENT(val VALUE: Int) : SimpleStorageComponents {
    companion object {
        fun default(): EXAMPLE_COMPONENT = EXAMPLE_COMPONENT(0)
        val CODEC = Codec.INT.xmap({ int -> EXAMPLE_COMPONENT(int) }, { component -> component.VALUE })
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