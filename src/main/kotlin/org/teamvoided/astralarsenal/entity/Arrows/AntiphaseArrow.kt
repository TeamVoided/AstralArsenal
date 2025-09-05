package org.teamvoided.astralarsenal.entity.Arrows

import net.minecraft.block.BlockState
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralItems

class AntiphaseArrow : ArrowEntity {

    constructor(entityType: EntityType<out AntiphaseArrow>, world: World) :
            super(entityType, world)

    constructor(world: World, owner: LivingEntity, arrow: ItemStack, weapon: ItemStack) : super(
        world, owner, arrow, weapon
    )

    constructor(x: Double, y: Double, z: Double, world: World) : super(
        world, x, y, z, Items.ARROW.defaultStack, AstralItems.NAILCANNON.defaultStack
    )

    val damageMult = 0.5

    override fun tick() {
        if (this.age <= 40) {
            if (this.world is ServerWorld) {
                val ServerWorld = this.world as ServerWorld
                ServerWorld.spawnParticles(
                    ParticleTypes.GLOW,
                    this.x,
                    this.y,
                    this.z,
                    1,
                    0.1,
                    0.1,
                    0.1,
                    0.01
                )
            }
        }
        super.tick()
    }

    override fun hasNoGravity(): Boolean {
        if (this.age <= 40) {
            return true
        } else {
            return super.hasNoGravity()
        }
    }

    override fun tryPickup(player: PlayerEntity?): Boolean {
        if (this.age <= 40) {
            return false
        } else {
            return super.tryPickup(player)
        }
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        if (this.age > 40) {
            super.onBlockHit(blockHitResult)
        }
    }

}