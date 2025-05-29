package org.teamvoided.astralarsenal.particles

import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import org.teamvoided.astralarsenal.init.AstralParticles
import kotlin.math.sqrt

class TomeRuneParticleEmitter(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    val provider: SpriteProvider,
    val texture: TomeRuneTexture
) :
    SpriteBillboardParticle(world, x, y, z) {

    init {
        collidesWithWorld = false
        maxAge = FRAME_COUNT
        scale = 0.2F
        setSpriteForAge(provider)
    }

    override fun tick() {
        if (age++ >= maxAge) {
            world?.addParticle(
                TomeRuneParticleEffect(AstralParticles.TOME_RUNE, texture),
                x, y + Y_OFFSET, z,
                0.0, 0.0, 0.0
            )

            markDead()
            return
        }

        if (age <= FRAME_COUNT) {
            setSpriteForAge(provider)

            if (age < FRAME_COUNT) {
                return
            }
        }

        colorAlpha = 0F
        maxAge = 20

        if (age == FRAME_COUNT) {

            // Stolen from fireworks
            for (i in -POOF_AMOUNT..POOF_AMOUNT) {
                for (j in -POOF_AMOUNT..POOF_AMOUNT) {
                    var k: Int = -POOF_AMOUNT
                    while (k <= POOF_AMOUNT) {
                        val g = j.toDouble() + (random.nextDouble() - random.nextDouble()) * 0.5
                        val h = i.toDouble() + (random.nextDouble() - random.nextDouble()) * 0.5
                        val l = k.toDouble() + (random.nextDouble() - random.nextDouble()) * 0.5
                        val m: Double = sqrt(g * g + h * h + l * l) / POOF_SIZE + random.nextGaussian() * 0.05

                        world.addParticle(
                            AstralParticles.TOME_RUNE_POOF,
                            x, y + Y_OFFSET, z,
                            g / m, h / m, l / m
                        )

                        if (i != -POOF_AMOUNT && i != POOF_AMOUNT && j != -POOF_AMOUNT && j != POOF_AMOUNT) {
                            k += POOF_AMOUNT * 2 - 1
                        }
                        ++k
                    }
                }
            }
        }
    }

    override fun getType(): ParticleTextureSheet? {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE
    }

    override fun getFacingCameraMode(): FacingCameraMode? {
        return FacingCameraMode.Y_AND_W
    }

    companion object {
        private const val Y_OFFSET = 0.3
        private const val FRAME_COUNT = 7
        private const val POOF_SIZE = 0.055
        private const val POOF_AMOUNT = 1
    }

    class Factory(val provider: SpriteProvider) : ParticleFactory<TomeRuneParticleEffect> {

        override fun createParticle(
            effect: TomeRuneParticleEffect,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle? {
            return TomeRuneParticleEmitter(world, x, y, z, provider, effect.texture)
        }
    }
}