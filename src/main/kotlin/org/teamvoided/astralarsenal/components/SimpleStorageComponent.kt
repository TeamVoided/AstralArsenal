package org.teamvoided.astralarsenal.components

import com.mojang.serialization.Codec

interface SimpleStorageComponent

// The suppression is here to not show warnings for the bad names on EXAMPLE_COMPONENT
@Suppress("ClassName", "PropertyName", "unused")
data class EXAMPLE_COMPONENT(val VALUE: Int) : SimpleStorageComponent {
    companion object {
        fun default(): EXAMPLE_COMPONENT = EXAMPLE_COMPONENT(0)
        val CODEC = Codec.INT.xmap({ int -> EXAMPLE_COMPONENT(int) }, { component -> component.VALUE })
    }
}