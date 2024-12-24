package org.teamvoided.astralarsenal.particles

import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.MathHelper.lerp
import java.util.*
import kotlin.math.max

class DusksSnowFlakeParticle (
    world: ClientWorld, x: Double, y: Double, z: Double, velX: Double, velY: Double, velZ: Double
) : SpriteBillboardParticle(world, x, y, z, velX, velY, velZ) {

    //(ender) delet this?
    constructor(
        world: ClientWorld, x: Double, y: Double, z: Double
    ) : this(
        world,
        x,
        y,
        z,
        (Math.random() * 2.0 - 1.0) * 0.05,
        (Math.random() * 2.0 - 1.0) * 0.05,
        (Math.random() * 2.0 - 1.0) * 0.05
    )

    private val rotationSpeed1: Float
    private val rotationSpeed2: Float
    private var lerp: Float
    private var lerpSpeed: Float

    init {
        this.velocityMultiplier = 1.0f
        this.velocityX = velX
        this.velocityY = velY
        this.velocityZ = velZ
        this.scale = 0.1f * (this.random.nextFloat() * this.random.nextFloat() * 1.0f + 1.0f)
        this.rotationSpeed1 = (Math.random().toFloat() - 0.5f) * 0.1f
        this.rotationSpeed2 = (Math.random().toFloat() - 0.5f) * 0.1f
        this.lerp = Math.random().toFloat()
        this.lerpSpeed = -0.01f
        this.maxAge = (this.random.nextFloat() * 900).toInt() + 600
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_OPAQUE
    override fun getGroup(): Optional<ParticleGroup> {
        return Optional.of(SNOWFLAKE_PARTICLE_GROUP)
    }

    //    override fun tick() {
//        super.tick()
//        this.angle += 3.1415927f * this.rotationSpeed * 2.0f
//        this.velocityX *= 0.95
//        this.velocityY *= 0.9
//        this.velocityZ *= 0.95
//    }

    override fun tick() {
        this.prevPosX = this.x
        this.prevPosY = this.y
        this.prevPosZ = this.z
        if (this.age++ >= this.maxAge) {
            this.markDead()
        } else {
            if (this.onGround) {
                this.prevAngle = this.angle
                age += 4
            } else {
                this.prevAngle = this.angle
                if (this.lerp >= 1) {
                    this.lerpSpeed = -0.05f * Math.random().toFloat()
                } else if (this.lerp <= 0) {
                    this.lerpSpeed = 0.05f * Math.random().toFloat()
                }
                lerp += lerpSpeed
                this.angle += Math.PI.toFloat() * lerp(lerp, rotationSpeed1, rotationSpeed2) * 2.0f
            }
            this.move(this.velocityX, this.velocityY, this.velocityZ)
            this.velocityX *= 0.9
            this.velocityZ *= 0.9
            this.velocityY = max((this.velocityY - 0.002) * 0.9, -0.04)
        }
    }

    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            defaultParticleType: DefaultParticleType, world: ClientWorld,
            x: Double, y: Double, z: Double,
            velX: Double, velY: Double, velZ: Double
        ): Particle {
            val snowflakeParticle = DusksSnowFlakeParticle(world, x, y, z, velX, velY, velZ)
            snowflakeParticle.setSprite(spriteProvider)
            return snowflakeParticle
        }
    }

    companion object {
        val SNOWFLAKE_PARTICLE_GROUP: ParticleGroup = ParticleGroup(32768)
    }
}