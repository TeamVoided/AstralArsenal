package org.teamvoided.astralarsenal.item

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterials
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.CannonballEntity

// Please change the summoning functionality into a kosmogliph for cannonball and keep the sound functionality
class AstralGreathammerItem(settings: Item.Settings) : SwordItem(ToolMaterials.NETHERITE, settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient) {
            val snowballEntity = CannonballEntity(world, user)
            snowballEntity.setProperties(user, user.pitch, user.yaw, 0.0f, 0.1f, 0.0f)
            snowballEntity.addVelocity(0.0, 0.1, 0.0)
            world.spawnEntity(snowballEntity)
            world.playSound(user.x,user.y,user.z,SoundEvents.BLOCK_BARREL_OPEN,SoundCategory.PLAYERS,1.0F,1.0F, false)
            if (!user.isCreative) {
                user.itemCooldownManager.set(this, 100)
            }
        }
        return super.use(world, user, hand)
    }

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        target.world.playSound(
            null,
            target.x,
            target.y,
            target.z,
            SoundEvents.ITEM_MACE_SMASH_GROUND,
            SoundCategory.PLAYERS,
            1.0F,
            1.0F
        )
        return super.postHit(stack, target, attacker)
    }
}