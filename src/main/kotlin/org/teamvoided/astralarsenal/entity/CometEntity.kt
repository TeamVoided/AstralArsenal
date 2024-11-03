package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.TargetPredicate
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.entity.nails.NailEntity
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.world.explosion.CometExplosionBehavior
import org.teamvoided.astralarsenal.world.explosion.KnockbackExplosionBehavior
import kotlin.math.absoluteValue
import kotlin.math.sign

class CometEntity : PersistentProjectileEntity {

    constructor(entityType: EntityType<out CometEntity>, world: World) :
            super(entityType, world)

    constructor(world: World, owner: LivingEntity) : super(
        AstralEntities.COMET_ENTITY, owner, world, Items.ARROW.defaultStack, AstralItems.NAILCANNON.defaultStack
    )

    var damage = 15f
    var homing = false

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        world.createExplosion(
            null,
            damageSources.explosion(null, this.owner),
            CometExplosionBehavior(this.owner, null, damage),
            this.x,
            this.y,
            this.z,
            2.0f,
            false,
            World.ExplosionSourceType.TNT
        )
        this.discard()
        super.onBlockHit(blockHitResult)
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        world.createExplosion(
            null,
            damageSources.explosion(null, this.owner),
            CometExplosionBehavior(this.owner, entityHitResult.entity, damage),
            this.x,
            this.y,
            this.z,
            2.0f,
            false,
            World.ExplosionSourceType.TNT
        )
        this.discard()
        super.onEntityHit(entityHitResult)
    }

    val entities = mutableListOf<Entity>()
    override fun tick() {
        world.addParticle(
            ParticleTypes.CAMPFIRE_COSY_SMOKE,
            true,
            this.x + random.rangeInclusive(-1, 1).times(0.1),
            this.y + random.rangeInclusive(-1, 1).times(0.1),
            this.z + random.rangeInclusive(-1, 1).times(0.1),
            0.0, 0.0, 0.0
        )
        if (homing) {
            if (entities.isEmpty()) {
                val e = mutableListOf<Entity>()
                e.addAll(
                    world.getOtherEntities(
                        this.owner, Box(
                            this.x - 5,
                            this.y - 5,
                            this.z - 5,
                            this.x + 5,
                            this.y + 5,
                            this.z + 5
                        )
                    ).filter { it is LivingEntity })
                if (e.isNotEmpty()) {
                    entities.addFirst(e[0])
                }
            } else {
                val entity = entities[0]
                val x = (entity.x - this.x)
                val y = (entity.eyeY - this.y)
                val z = (entity.z - this.z)
                this.addVelocity(x.sign * 0.1, y.sign * 0.1, z.sign * 0.1)
                this.velocityDirty
                if (!entity.isAlive || x.absoluteValue > 15 || y.absoluteValue > 15 || z.absoluteValue > 15) {
                    entities.removeFirst()
                }
            }
        }
        if (this.age > (30 * 20)) {
            world.createExplosion(
                null,
                damageSources.explosion(null, this.owner),
                CometExplosionBehavior(this.owner, null, damage),
                this.x,
                this.y,
                this.z,
                2.0f,
                false,
                World.ExplosionSourceType.TNT
            )
            this.discard()
        }
        super.tick()
    }

    override fun getDefaultItemStack(): ItemStack = Items.AIR.defaultStack
}