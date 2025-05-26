package org.teamvoided.astralarsenal.particles

import com.mojang.serialization.Codec
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.StringIdentifiable

enum class TomeRuneTexture(val textureName: String) : StringIdentifiable {
    IMPEDING("impeding"),
    MAGNETISING("magnetising");

    override fun asString(): String {
        return textureName
    }

    companion object {
        val CODEC: Codec<TomeRuneTexture> = StringIdentifiable.createEnumCodec(TomeRuneTexture::values)

        val PACKET_CODEC: PacketCodec<PacketByteBuf?, TomeRuneTexture> =
            PacketCodec.create(PacketByteBuf::writeEnumConstant)
            { buf: PacketByteBuf -> buf.readEnumConstant(TomeRuneTexture::class.java) }
    }
}