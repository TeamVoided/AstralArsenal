package org.teamvoided.astralarsenal.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.Projectiles.CannonballEntity

class CannonballItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient) {
            val cannonball = CannonballEntity(world, user)
            cannonball.setProperties(user, user.pitch, user.yaw, 0.0f, 0.1f, 0.0f)
            cannonball.addVelocity(0.0, 0.1, 0.0)
            world.spawnEntity(cannonball)
            user.playSound(SoundEvents.BLOCK_BARREL_OPEN)
            if (!user.isCreative) {
                user.itemCooldownManager.set(this, 100)
            }
        }
        return super.use(world, user, hand)
    }
}