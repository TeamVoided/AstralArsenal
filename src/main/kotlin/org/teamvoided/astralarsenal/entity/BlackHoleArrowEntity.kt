package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralEntities

class BlackHoleArrowEntity : ArrowEntity {
    constructor(entityType: EntityType<out BeamOfLightArrowEntity?>?, world: World?) :
            super(entityType as EntityType<out ArrowEntity?>?, world)

    constructor(world: World, owner: LivingEntity) :
            super(AstralEntities.BEAM_OF_LIGHT_ARROW as EntityType<out ArrowEntity?>, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.BEAM_OF_LIGHT_ARROW as EntityType<out ArrowEntity?>, world)

    var balls: LivingEntity? = null

    override fun tick() {
        if (!world.isClient) {
            val serverWorld = world as ServerWorld
            serverWorld.spawnParticles(
                ParticleTypes.DRAGON_BREATH,
                this.x,
                this.y,
                this.z,
                50,
                random.nextDouble().minus(0.5).times(0.2),
                random.nextDouble().minus(0.5).times(0.2),
                random.nextDouble().minus(0.5).times(0.2),
                0.0
            )
        }
        super.tick()
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
            this.discard()
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if (!world.isClient && entityHitResult.entity is LivingEntity) {
            val snowballEntity = BlackHoleEntity(world, balls)
            val x = when(random.nextBoolean()){
                true -> 1
                false -> -1
            }
            val y = when(random.nextBoolean()){
                true -> 1
                false -> -1
            }
            val z = when(random.nextBoolean()){
                true -> 1
                false -> -1
            }
            snowballEntity.setPosition(
                this.x + (x * 5) + random.nextDouble().minus(0.5).times(10),
                this.y + (y * 5) + random.nextDouble().minus(0.5).times(10),
                this.z + (z * 5) + random.nextDouble().minus(0.5).times(10)
            )
            snowballEntity.target = entityHitResult.entity as LivingEntity
            snowballEntity.owner = balls
            world.spawnEntity(snowballEntity)
            this.discard()
        }
    }

    init{this.setNoGravity(true)}
}