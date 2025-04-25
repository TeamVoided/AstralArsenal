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
    var hard_damage = 0
    var enraged = false
    var outerColour = 0x00ffffff
    var innerColour = 0x00ffffff

    override fun tick() {
        var particles = if (enraged) ParticleTypes.GLOW else ParticleTypes.END_ROD
        if (!world.isClient) {
            val serverWorld = world as ServerWorld
            serverWorld.spawnParticles(
                particles,
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
        if (this.hasNoGravity()) {
            this.setNoGravity(false)
        }
        super.tick()
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        if (!world.isClient) {
            val beamOfLightEntity = BeamOfLightEntity(world, balls)
            beamOfLightEntity.setPosition(this.x, this.y, this.z)
            beamOfLightEntity.DOT = DOT
            beamOfLightEntity.side = side
            beamOfLightEntity.THRUST = THRUST
            beamOfLightEntity.TIMEACTIVE = TIMEACTIVE
            beamOfLightEntity.WINDUP = WINDUP / 4
            beamOfLightEntity.DMG = DMG
            beamOfLightEntity.trackTime = trackTime / 4
            beamOfLightEntity.owner = balls
            beamOfLightEntity.hard_damage = hard_damage
            beamOfLightEntity.enraged = enraged
            beamOfLightEntity.outerColour = outerColour
            beamOfLightEntity.innerColour = innerColour
            world.spawnEntity(beamOfLightEntity)
            this.discard()
        }
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if (!world.isClient && (entityHitResult.entity != this.balls || this.age > 5)) {
            val beamOfLightEntity = BeamOfLightEntity(world, balls)
            beamOfLightEntity.setPosition(this.x, this.y, this.z)
            beamOfLightEntity.DOT = DOT
            beamOfLightEntity.side = side
            beamOfLightEntity.THRUST = THRUST
            beamOfLightEntity.TIMEACTIVE = TIMEACTIVE
            beamOfLightEntity.WINDUP = WINDUP
            beamOfLightEntity.DMG = DMG
            beamOfLightEntity.targetEntity = entityHitResult.entity
            beamOfLightEntity.trackTime = trackTime
            beamOfLightEntity.owner = balls
            beamOfLightEntity.enraged = enraged
            beamOfLightEntity.outerColour = outerColour
            beamOfLightEntity.innerColour = innerColour
            world.spawnEntity(beamOfLightEntity)
            this.discard()
        }
    }

    init {
        this.setNoGravity(false)
    }
}