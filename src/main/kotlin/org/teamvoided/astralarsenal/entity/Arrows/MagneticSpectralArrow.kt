package org.teamvoided.astralarsenal.entity.Arrows

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.SpectralArrowEntity
import net.minecraft.entity.projectile.TridentEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.Projectiles.CannonballEntity
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.mixin.PersistentProjectileEntityAccessor
import org.teamvoided.astralarsenal.mixin.TridentEntityAccessor
import org.teamvoided.astralarsenal.util.sillyLightningTime

class MagneticSpectralArrow : SpectralArrowEntity {

    constructor(entityType: EntityType<out AntiphaseSpectralArrow>, world: World) :
            super(entityType, world)

    constructor(world: World, owner: LivingEntity, arrow: ItemStack, weapon: ItemStack) : super(
        world, owner, arrow, weapon
    )

    constructor(x: Double, y: Double, z: Double, world: World) : super(
        world, x, y, z, Items.SPECTRAL_ARROW.defaultStack, AstralItems.NAILCANNON.defaultStack
    )

    val range = 5.0 //magnetic range
    val strength = 0.4 //magnetic strength
    val damageMult = 0.25

    override fun tick() {
        super.tick()
        if (!this.inGround) {
            val nearbyEntities = mutableListOf<Entity>()
            nearbyEntities.addAll(
                this.world.getOtherEntities(
                    this, Box(
                        this.x + range,
                        this.eyeY + range,
                        this.z + range,
                        this.x - range,
                        this.eyeY - range,
                        this.z - range
                    )
                )
                    .filter {
                        it is LivingEntity && it != this.owner && (this.eyePos.distanceTo(
                            it.pos
                        ) <= range)
                    })
            for (entity in nearbyEntities) {
                val str = strength
                val desiredVec = entity.eyePos.subtract(this.eyePos)
                val change = desiredVec.subtract(this.velocity).normalize().multiply(str)
                this.addVelocity(change)
                this.velocityModified
            }
            sparkNearbyEntities(this, this)
        }
    }

    fun sparkNearbyEntities(cause: Entity, base: Entity) {
        val entities = mutableListOf<Entity>()
        entities.addAll(
            base.world.getOtherEntities(
                cause, Box(
                    base.x + range,
                    base.eyeY + range,
                    base.z + range,
                    base.x - range,
                    base.eyeY - range,
                    base.z - range
                )
            ).filter {
                it is LivingEntity && it != cause && it != base && base.eyePos.distanceTo(
                    it.pos
                ) <= range
            }
        )
        if (entities.isNotEmpty()) {
            for (entiity in entities) {
                if (entiity != this.owner) {
                    if (base.world is ServerWorld) {
                        sillyLightningTime(
                            Vec3d(base.pos.x, base.pos.y + (base.height / 2f), base.pos.z),
                            Vec3d(entiity.pos.x, entiity.pos.y + (entiity.height / 2f), entiity.pos.z),
                            ((base.world as ServerWorld)),
                            2,
                            5,
                            1,
                            0.03f,
                            0.5
                        )
                    }
                }
            }
        }
    }
}