package org.teamvoided.astralarsenal.item

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralEffects
import kotlin.math.max

class TomeOfHexesItem(settings: Settings) : Item(settings) {
    override fun getUseTicks(stack: ItemStack?, entity: LivingEntity?): Int {
        return 200
    }

    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        val pageFlip = (remainingUseTicks / 20)
        val pitchShift = world.random.nextFloat().times(0.3f).plus(0.7f)
        if (remainingUseTicks % max(1, pageFlip) == 0){
            world.playSoundFromEntity(user, SoundEvents.ITEM_BOOK_PAGE_TURN,SoundCategory.PLAYERS,3.0f,pitchShift)
        }
        if (remainingUseTicks == 1){
            world.playSoundFromEntity(user, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE,SoundCategory.PLAYERS,3.0f,0.75f)
            if (user is PlayerEntity) {
                user.addStatusEffect(StatusEffectInstance(AstralEffects.BREACHING, 2400, 1))
                user.itemCooldownManager.set(stack.item, 400)
                user.stopUsingItem()
            }
        }
        super.usageTick(world, user, stack, remainingUseTicks)
    }

    override fun onStoppedUsing(stack: ItemStack?, world: World?, user: LivingEntity?, remainingUseTicks: Int) {
        if (user is PlayerEntity){
            user.itemCooldownManager.set(this, 200)
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        player.setCurrentHand(hand)
        return TypedActionResult(ActionResult.CONSUME_PARTIAL, player.getStackInHand(hand))
    }
    override fun getUseAction(stack: ItemStack): UseAction = UseAction.BLOCK
}