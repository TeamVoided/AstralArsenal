package org.teamvoided.astralarsenal.kosmogliph.melee.mace

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.Holder
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.*
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.util.playSound
import org.teamvoided.astralarsenal.world.explosion.maceExplosions.*

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
        val usedTicks = getUseTicks(stack, user) - remainingUseTicks
        if (usedTicks == 20 || usedTicks == 50 || usedTicks == 100) {
            world.playSound(
                user.pos, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 1.0F, 1.0f
            )
        }
    }

    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
        val usedTicks = getUseTicks(stack, user) - remainingUseTicks
        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }

}