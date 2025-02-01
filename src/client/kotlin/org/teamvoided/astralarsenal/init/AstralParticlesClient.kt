package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.PendingParticleFactory
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import org.teamvoided.astralarsenal.particles.DusksSnowFlakeParticle

object AstralParticlesClient {
    fun init(){
        register(AstralParticles.SNOWFLAKE, DusksSnowFlakeParticle::Factory)
    }

    fun <T : ParticleEffect> register(type: ParticleType<T>, constructor: PendingParticleFactory<T>) =
        ParticleFactoryRegistry.getInstance().register(type, constructor)
}