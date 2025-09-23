@file:Suppress("PropertyName", "ClassName", "HasPlatformType", "unused")

package org.teamvoided.astralarsenal.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

interface SimpleStorageComponent

//   _______                              __
//  |    ___|.--.--.---.-.--------.-----.|  |.-----.-----.
//  |    ___||_   _|  _  |        |  _  ||  ||  -__|__ --|
//  |_______||__.__|___._|__|__|__|   __||__||_____|_____|
//                                |__|

data class ONE_VALUE(val VALUE: Int) : SimpleStorageComponent {
    companion object {
        val DEFAULT: ONE_VALUE = ONE_VALUE(0)
        val CODEC = Codec.INT.xmap({ int -> ONE_VALUE(int) }, { component -> component.VALUE })
    }
}

// For Multi Value Components you can always add more values to the end,
// just look at commented out boolean for how to do it.
// Be careful to not miss the commas!
data class MULTI_VALUE(val INT_VALUE: Int, val STRING_VALUE: String/*, val BOOL_VALUE: Boolean*/) :
    SimpleStorageComponent {
    companion object {
        val DEFAULT: MULTI_VALUE = MULTI_VALUE(0, ""/*, false*/)
        val CODEC = RecordCodecBuilder.create<MULTI_VALUE> { builder ->
            builder.group(
                Codec.INT.fieldOf("INT_VALUE").forGetter { it.INT_VALUE },
                Codec.STRING.fieldOf("STRING_VALUE").forGetter { it.STRING_VALUE }
//                ,
//                Codec.BOOL.fieldOf("BOOL_VALUE").forGetter { it.BOOL_VALUE }
            ).apply(builder, ::MULTI_VALUE)
        }

    }
}