package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.PendingParticleFactory
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import org.teamvoided.astralarsenal.particles.DusksSnowFlakeParticle
import org.teamvoided.astralarsenal.particles.TomeRuneParticle
import org.teamvoided.astralarsenal.particles.TomeRuneParticleEmitter
import org.teamvoided.astralarsenal.particles.TomeRunePoofParticle

object AstralParticlesClient {

    fun init() {
        register(AstralParticles.SNOWFLAKE, DusksSnowFlakeParticle::Factory)
        register(AstralParticles.TOME_RUNE_EMITTER) { TomeRuneParticleEmitter.Factory(it) }
        register(AstralParticles.TOME_RUNE) { TomeRuneParticle.Factory() }
        register(AstralParticles.TOME_RUNE_POOF) { TomeRunePoofParticle.Factory(it) }
    }

    fun <T : ParticleEffect> register(type: ParticleType<T>, constructor: PendingParticleFactory<T>) =
        ParticleFactoryRegistry.getInstance().register(type, constructor)
}