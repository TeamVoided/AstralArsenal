package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEntities

class FlameShotEntity : ThrownItemEntity {

    constructor(entityType: EntityType<out FlameShotEntity?>?, world: World?) :
            super(entityType as EntityType<out ThrownItemEntity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.FLAME_SHOT_ENTITY as EntityType<out ThrownItemEntity?>, owner, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.FLAME_SHOT_ENTITY as EntityType<out ThrownItemEntity?>, x, y, z, world)

    override fun getDefaultItem(): Item {
        return Items.COD
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        if (entityHitResult.entity != owner && entityHitResult.entity is LivingEntity) {
            val hit = entityHitResult.entity
            hit.customDamage(DamageTypes.IN_FIRE, 3.0f, this, owner)
            hit.setOnFireFor(100)
        }
    }


    companion object {
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
    }

    override fun tick() {
        world.addParticle(
            ParticleTypes.FLAME,
            true,
            this.x + random.rangeInclusive(-1, 1).times(0.1),
            this.y + random.rangeInclusive(-1, 1).times(0.1),
            this.z + random.rangeInclusive(-1, 1).times(0.1),
            random.nextDouble().times(2).minus(1).times(0.01),
            random.nextDouble().times(0.1),
            random.nextDouble().times(2).minus(1).times(0.01)
        )
        super.tick()
        if (this.isWet) {
            this.discard()
        }
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        this.discard()
        super.onBlockHit(blockHitResult)
    }
}