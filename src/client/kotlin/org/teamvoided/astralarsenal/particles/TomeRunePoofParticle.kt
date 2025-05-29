package org.teamvoided.astralarsenal.particles

import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType

class TomeRunePoofParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    val provider: SpriteProvider
) :
    SpriteBillboardParticle(world, x, y, z) {

    init {
        this.velocityX = velocityX * 0.8
        this.velocityY = velocityY * 0.8
        this.velocityZ = velocityZ * 0.8
        setSpriteForAge(provider)
        maxAge = 22
    }

    override fun tick() {
        super.tick()
        setSpriteForAge(provider)
    }

    override fun getBrightness(tint: Float): Int {
        return 120
    }

    override fun getType(): ParticleTextureSheet? {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE
    }

    class Factory(val provider: SpriteProvider) : ParticleFactory<DefaultParticleType> {

        override fun createParticle(
            effect: DefaultParticleType?,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            return TomeRunePoofParticle(world, x, y, z, velocityX, velocityY, velocityZ, provider)
        }
    }
}
