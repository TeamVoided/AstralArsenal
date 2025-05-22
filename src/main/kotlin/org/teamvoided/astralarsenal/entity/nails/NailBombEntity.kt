package org.teamvoided.astralarsenal.entity.nails

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.init.AstralItems

class NailBombEntity : PersistentProjectileEntity {

    constructor(entityType: EntityType<out NailBombEntity>, world: World) :
            super(entityType, world)

    constructor(world: World, owner: LivingEntity) : super(
        AstralEntities.NAILBOMB, owner, world, Items.ARROW.defaultStack, AstralItems.NAILCANNON.defaultStack
    )

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if (entityHitResult.entity is LivingEntity && entityHitResult.entity.world is ServerWorld) {
            val target = entityHitResult.entity
            target.customDamage(AstralDamageTypes.NAILED, 5f, this, this.owner)
            repeat(target.world.random.rangeInclusive(10, 30)) {
                val nail =
                    if (this.owner is LivingEntity) {
                        NailEntity(world = world, this.owner as LivingEntity)
                    } else {
                        NailEntity(this.x, this.y, this.z, world)
                    }
                nail.setPosition(this.pos)
                nail.velocity = Vec3d(
                    world.random.nextDouble().minus(0.5).times(3),
                    world.random.nextDouble().minus(0.5).times(3),
                    world.random.nextDouble().minus(0.5).times(3)
                )
                world.spawnEntity(nail)
            }
            this.discard()
        }
        super.onEntityHit(entityHitResult)
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        repeat(this.world.random.rangeInclusive(10, 30)) {
            val nail =
                if (this.owner is LivingEntity) {
                    NailEntity(world = world, this.owner as LivingEntity)
                } else {
                    NailEntity(this.x, this.y, this.z, world)
                }
            nail.setPosition(this.pos)
            nail.velocity = Vec3d(
                world.random.nextDouble().minus(0.5).times(3),
                world.random.nextDouble().minus(0.5).times(3),
                world.random.nextDouble().minus(0.5).times(3)
            )
            world.spawnEntity(nail)
        }
        this.discard()
        super.onBlockHit(blockHitResult)
    }

    override fun getDefaultItemStack(): ItemStack = Items.AIR.defaultStack
}