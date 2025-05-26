package org.teamvoided.astralarsenal.particles

import net.minecraft.client.MinecraftClient
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.particle.SpriteBillboardParticle
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.world.ClientWorld
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.mixin.ParticleManagerAccessor

class TomeRuneParticle(world: ClientWorld, x: Double, y: Double, z: Double, val texture: TomeRuneTexture) :
    SpriteBillboardParticle(world, x, y, z) {

    val atlas: SpriteAtlasTexture = (MinecraftClient.getInstance().particleManager as ParticleManagerAccessor).atlas
    var frame = 0

    init {
        setSpriteForAge(0)
        scale = 0.35F
        maxAge = MAX_AGE
        collidesWithWorld = false
        velocityY = 0.01
    }

    override fun tick() {
        super.tick()

        if (age > FIRST_FRAME_DELAY) {
            setSpriteForAge(frame)

            if (age % FRAME_TIME == 0) {
                frame++
                colorAlpha -= 1F / (MAX_AGE - FIRST_FRAME_DELAY)
            }
        }
    }

    fun setSpriteForAge(frame: Int) {
        if (!dead) {
            val name = texture.textureName
            setSprite(atlas.getSprite(AstralArsenal.id("tome_rune/runes/$name/${name}_$frame")))
        }
    }

    override fun getType(): ParticleTextureSheet? {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun getFacingCameraMode(): FacingCameraMode? {
        return FacingCameraMode.Y_AND_W
    }

    companion object {
        private const val FIRST_FRAME_DELAY = 20
        private const val FRAME_COUNT = 9
        private const val FRAME_TIME = 2
        private const val MAX_AGE = FIRST_FRAME_DELAY + (FRAME_COUNT * FRAME_TIME)
    }

    class Factory() : ParticleFactory<TomeRuneParticleEffect> {

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
            return TomeRuneParticle(world, x, y, z, effect.texture)
        }
    }
}