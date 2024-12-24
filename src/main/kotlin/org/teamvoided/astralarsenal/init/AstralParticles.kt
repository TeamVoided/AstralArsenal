package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes.simple
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.astralarsenal.AstralArsenal.id

object AstralParticles {
    val SNOWFLAKE = simple()
    fun init(){
        register("snowflake", SNOWFLAKE)
    }
    fun register(id: String, particleType: ParticleType<*>) =
        Registry.register(Registries.PARTICLE_TYPE, id(id), particleType)
}