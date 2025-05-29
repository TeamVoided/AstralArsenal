package org.teamvoided.astralarsenal.particles

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType

class TomeRuneParticleEffect(val particleType: ParticleType<*>, val texture: TomeRuneTexture) : ParticleEffect {

    override fun getType(): ParticleType<*> {
        return particleType
    }

    companion object {
        fun codec(type: ParticleType<TomeRuneParticleEffect>): MapCodec<TomeRuneParticleEffect> {
            return RecordCodecBuilder.mapCodec {
                it.group(
                    TomeRuneTexture.CODEC.fieldOf("texture").forGetter { it -> it.texture }
                ).apply(it) { id -> TomeRuneParticleEffect(type, id) }
            }
        }

        fun packetCodec(type: ParticleType<TomeRuneParticleEffect>): PacketCodec<PacketByteBuf?, TomeRuneParticleEffect?>? {
            return TomeRuneTexture.PACKET_CODEC.map(
                { texture -> TomeRuneParticleEffect(type, texture) },
                TomeRuneParticleEffect::texture
            )
        }
    }
}