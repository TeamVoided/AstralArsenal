package org.teamvoided.astralarsenal.particles

import com.mojang.serialization.Codec
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.StringIdentifiable

enum class TomeRuneTexture(val textureName: String) : StringIdentifiable {
    IMPEDING("impeding"),
    MAGNETISING("magnetising"),
    BREACHING("breaching"),
    DIMINISHING("diminishing"),
    WEAKENING("weakening"),
    BLAZING("blazing"),
    CLEANSING("cleansing");

    override fun asString() = textureName

    companion object {
        val CODEC: Codec<TomeRuneTexture> = StringIdentifiable.createEnumCodec(TomeRuneTexture::values)

        val PACKET_CODEC: PacketCodec<PacketByteBuf, TomeRuneTexture> =
            PacketCodec.create<PacketByteBuf, TomeRuneTexture>(PacketByteBuf::writeEnumConstant)
            { it.readEnumConstant(TomeRuneTexture::class.java) }
    }
}