package org.teamvoided.astralarsenal.item

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.thrown.EnderPearlEntity
import net.minecraft.entity.projectile.thrown.SnowballEntity
import net.minecraft.item.*
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.init.AstralEntities

// Please change the summoning functionality into a kosmogliph for cannonball.json and keep the sound functionality
class AstralGreathammerItem(settings: Item.Settings) : SwordItem(ToolMaterials.NETHERITE, settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient) {
            val snowballEntity = CannonballEntity(world, user)
            snowballEntity.setProperties(user, user.pitch, user.yaw, 0.0f, 0.1f, 0.0f)
            snowballEntity.addVelocity(0.0, 0.1, 0.0)
            world.spawnEntity(snowballEntity)
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