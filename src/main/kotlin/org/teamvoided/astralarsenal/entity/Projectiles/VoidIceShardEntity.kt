package org.teamvoided.astralarsenal.entity.Projectiles

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.FreezeShotEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.util.setPropertiesTwo

class VoidIceShardEntity : ThrownItemEntity {

    constructor(entityType: EntityType<out VoidIceShardEntity>, world: World?) :
            super(entityType, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.VOID_SHARD, owner, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.VOID_SHARD, x, y, z, world)

    override fun getDefaultItem(): Item {
        return AstralItems.CANNONBALL
    }

    override fun hasNoGravity(): Boolean {
        return true
    }

    override fun tick() {
        if (this.velocity.x < 0.1f && this.velocity.y < 0.1f && this.velocity.z < 0.1f){
            if (this.owner is LivingEntity) {
                repeat(20) {
                    val freezeBallEntity = FreezeShotEntity(this.world, this.owner as LivingEntity)
                    setPropertiesTwo(freezeBallEntity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                    freezeBallEntity.addVelocity(
                        this.random.nextDouble().minus(0.5),
                        this.random.nextDouble().times(0.5),
                        this.random.nextDouble().minus(0.5)
                    )
                    freezeBallEntity.setPosition(this.pos)
                    this.world.spawnEntity(freezeBallEntity)
                }
                this.discard()
            }
        }
        super.tick()
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        val target = entityHitResult.entity
        super.onEntityHit(entityHitResult)
        if (target != owner && target is LivingEntity) {
            val hit = target
            hit.customDamage(AstralDamageTypes.CHILLED, 3.0f, this, owner)
            if (hit.frozenTicks < 140) {
                hit.frozenTicks = 140
            }
            if (hit.frozenTicks < 540) {
                hit.frozenTicks.plus(100)
            }
            if (this.owner is LivingEntity)
            repeat(20) {
                val freezeBallEntity = FreezeShotEntity(this.world, this.owner as LivingEntity)
                setPropertiesTwo(freezeBallEntity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                freezeBallEntity.addVelocity(
                    this.random.nextDouble().minus(0.5),
                    this.random.nextDouble().times(0.5),
                    this.random.nextDouble().minus(0.5)
                )
                freezeBallEntity.setPosition(this.pos)
                this.world.spawnEntity(freezeBallEntity)
            }
            this.discard()
        }
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        if (this.owner is LivingEntity) {
            repeat(20) {
                val freezeBallEntity = FreezeShotEntity(this.world, this.owner as LivingEntity)
                setPropertiesTwo(freezeBallEntity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                freezeBallEntity.addVelocity(
                    this.random.nextDouble().minus(0.5),
                    this.random.nextDouble().times(0.5),
                    this.random.nextDouble().minus(0.5)
                )
                freezeBallEntity.setPosition(this.pos)
                this.world.spawnEntity(freezeBallEntity)
            }
            this.discard()
        }
    }

}