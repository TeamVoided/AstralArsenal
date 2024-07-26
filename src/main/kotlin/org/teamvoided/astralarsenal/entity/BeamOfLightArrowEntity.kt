package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralEntities

class BeamOfLightArrowEntity : ArrowEntity {
    constructor(entityType: EntityType<out BeamOfLightArrowEntity?>?, world: World?) :
            super(entityType as EntityType<out ArrowEntity?>?, world)

    constructor(world: World, owner: LivingEntity) :
            super(AstralEntities.BEAM_OF_LIGHT_ARROW as EntityType<out ArrowEntity?>, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.BEAM_OF_LIGHT_ARROW as EntityType<out ArrowEntity?>, world)
    var WINDUP = 1
    var TIMEACTIVE = 1
    var DOT = false
    var THRUST = 2.0
    var DMG = 1
    var side = 1
    var trackTime = 0
    var balls: LivingEntity? = null

    override fun tick() {
        if (!world.isClient) {
            val serverWorld = world as ServerWorld
            serverWorld.spawnParticles(
                ParticleTypes.END_ROD,
                this.x,
                this.y,
                this.z,
                10,
                random.nextDouble().minus(0.5),
                random.nextDouble().minus(0.5),
                random.nextDouble().minus(0.5),
                0.0
            )
        }
        super.tick()
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        if (!world.isClient) {
            val snowballEntity = BeamOfLightEntity(world, balls)
            snowballEntity.setPosition(this.x,this.y,this.z)
            snowballEntity.DOT = DOT
            snowballEntity.side = side
            snowballEntity.THRUST = THRUST
            snowballEntity.TIMEACTIVE = TIMEACTIVE
            snowballEntity.WINDUP = WINDUP / 4
            snowballEntity.DMG = DMG
            snowballEntity.trackTime = trackTime / 4
            snowballEntity.owner = this.owner
            world.spawnEntity(snowballEntity)
            this.discard()
        }
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if (!world.isClient) {
            val snowballEntity = BeamOfLightEntity(world, balls)
            snowballEntity.setPosition(this.x,this.y,this.z)
            snowballEntity.DOT = DOT
            snowballEntity.side = side
            snowballEntity.THRUST = THRUST
            snowballEntity.TIMEACTIVE = TIMEACTIVE
            snowballEntity.WINDUP = WINDUP
            snowballEntity.DMG = DMG
            snowballEntity.targetEntity = entityHitResult.entity
            snowballEntity.trackTime = trackTime
            snowballEntity.owner = this.owner
            world.spawnEntity(snowballEntity)
            this.discard()
        }
    }

    init{this.setNoGravity(true)}
}