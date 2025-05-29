package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes.simple
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.particles.TomeRuneParticleEffect

object AstralParticles {
    val SNOWFLAKE: DefaultParticleType = simple()
    val TOME_RUNE_EMITTER: ParticleType<TomeRuneParticleEffect> =
        FabricParticleTypes.complex(TomeRuneParticleEffect::codec, TomeRuneParticleEffect::packetCodec)
    val TOME_RUNE: ParticleType<TomeRuneParticleEffect> =
        FabricParticleTypes.complex(TomeRuneParticleEffect::codec, TomeRuneParticleEffect::packetCodec)
    val TOME_RUNE_POOF: DefaultParticleType = simple()

    fun init() {
        register("snowflake", SNOWFLAKE)
        register("tome_rune_emitter", TOME_RUNE_EMITTER)
        register("tome_rune", TOME_RUNE)
        register("tome_rune_poof", TOME_RUNE_POOF)
    }

    fun register(id: String, particleType: ParticleType<*>): ParticleType<*> =
        Registry.register(Registries.PARTICLE_TYPE, id(id), particleType)
}