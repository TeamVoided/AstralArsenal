package org.teamvoided.astralarsenal.kosmogliph.melee.mace

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.*
import net.minecraft.world.World
import org.teamvoided.astralarsenal.components.PulveriserData
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.util.playSound

class PulveriserKosmogliph(id: Identifier) :
    SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_PULVERISER) }) {

    override fun getUseTicks(stack: ItemStack, livingEntity: LivingEntity): Int {
        return 72000
    }

    override fun getUseAction(stack: ItemStack): UseAction = UseAction.SPEAR

    override fun onUse(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        player.setCurrentHand(hand)
        return TypedActionResult(ActionResult.CONSUME_PARTIAL, player.getStackInHand(hand))
    }

    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        val data = stack.getOrDefault(AstralDataComponents.PULVERISER_DATA, PulveriserData.DEFAULT)
        if (data.slamming) {
            user.stopUsingItem()
            if (user is PlayerEntity && !user.isCreative) {
                user.itemCooldownManager.set(stack.item, 50)
            }
            return
        }
        val usedTicks = getUseTicks(stack, user) - remainingUseTicks
        if (usedTicks == 20 || usedTicks == 50 || usedTicks == 100) {
            world.playSound(
                user.pos, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 1.0F, 1.0f
            )
        }
    }

    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
        val usedTicks = getUseTicks(stack, user) - remainingUseTicks
        val tickBoost = (
                if (usedTicks >= 100) 2.5
                else if (usedTicks >= 50) 2.0
                else 1.5
                )
        if (usedTicks >= 20) {
            stack.set(AstralDataComponents.PULVERISER_DATA, PulveriserData(usedTicks, true))
            val boost = user.rotationVector.multiply(1.0, 1.0, 1.0).normalize().multiply(tickBoost)
            user.setVelocity(user.velocity.x + boost.x, user.velocity.y + (boost.y * 0.9), user.velocity.z + boost.z)
            user.velocityModified
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val data = stack.getOrDefault(AstralDataComponents.PULVERISER_DATA, PulveriserData.DEFAULT)
        if (data.slamming) {
            if (entity is LivingEntity && entity.getStackInHand(Hand.MAIN_HAND) != stack && entity.getStackInHand(Hand.OFF_HAND) != stack) {
                stack.set(AstralDataComponents.PULVERISER_DATA, PulveriserData(0, false))
                if (entity is PlayerEntity && !entity.isCreative) {
                    entity.itemCooldownManager.set(stack.item, 200)
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected)
    }

}