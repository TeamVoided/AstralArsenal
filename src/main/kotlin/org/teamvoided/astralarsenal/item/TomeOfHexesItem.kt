package org.teamvoided.astralarsenal.item

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralEffects.BLAZED
import org.teamvoided.astralarsenal.init.AstralEffects.BLAZING
import org.teamvoided.astralarsenal.init.AstralEffects.BREACHED
import org.teamvoided.astralarsenal.init.AstralEffects.BREACHING
import org.teamvoided.astralarsenal.init.AstralEffects.CLEANSED
import org.teamvoided.astralarsenal.init.AstralEffects.CLEANSING
import org.teamvoided.astralarsenal.init.AstralEffects.DIMINISHED
import org.teamvoided.astralarsenal.init.AstralEffects.DIMINISHING
import org.teamvoided.astralarsenal.init.AstralEffects.IMPEDED
import org.teamvoided.astralarsenal.init.AstralEffects.IMPEDING
import org.teamvoided.astralarsenal.init.AstralEffects.WEAKENED
import org.teamvoided.astralarsenal.init.AstralEffects.WEAKENING
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.init.AstralKosmogliphs.HEX_OF_BLAZING
import org.teamvoided.astralarsenal.init.AstralKosmogliphs.HEX_OF_BREACHING
import org.teamvoided.astralarsenal.init.AstralKosmogliphs.HEX_OF_CLEANSING
import org.teamvoided.astralarsenal.init.AstralKosmogliphs.HEX_OF_DIMINISHING
import org.teamvoided.astralarsenal.init.AstralKosmogliphs.HEX_OF_IMPEDING
import org.teamvoided.astralarsenal.init.AstralKosmogliphs.HEX_OF_WEAKENING
import org.teamvoided.astralarsenal.util.getKosmogliphs
import org.teamvoided.astralarsenal.util.hasKosmogliphs
import kotlin.math.max

class TomeOfHexesItem(settings: Settings) : Item(settings) {
    override fun getUseTicks(stack: ItemStack?, entity: LivingEntity?): Int {
        return 200
    }

    val hexAppliers = listOf(
        BREACHING,
        DIMINISHING,
        WEAKENING,
        BLAZING,
        CLEANSING,
        IMPEDING
    )

    val kosList = listOf(
        HEX_OF_BREACHING,
        HEX_OF_DIMINISHING,
        HEX_OF_WEAKENING,
        HEX_OF_BLAZING,
        HEX_OF_CLEANSING,
        HEX_OF_IMPEDING
    )

    val hexes = listOf(
        BREACHED,
        DIMINISHED,
        WEAKENED,
        BLAZED,
        CLEANSED,
        IMPEDED
    )



    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        val pageFlip = (remainingUseTicks / 20)
        val pitchShift = world.random.nextFloat().times(0.3f).plus(0.7f)
        if (remainingUseTicks % max(1, pageFlip) == 0) {
            world.playSoundFromEntity(user, SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.PLAYERS, 3.0f, pitchShift)
            if (world is ServerWorld) {
                world.spawnParticles(
                    ParticleTypes.ENCHANT,
                    user.x,
                    user.eyeY,
                    user.z,
                    10,
                    0.0,
                    0.0,
                    0.0,
                    (1.0 / pageFlip) * 3
                )
            }
        }
        if (remainingUseTicks == 1) {
            world.playSoundFromEntity(user, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 3.0f, 0.5f)
            var effect = hexAppliers.get(world.random.rangeInclusive(0, 5))
            var amplifier = 0
            if (stack.hasKosmogliphs()) {
                val kosmogliph = stack.getKosmogliphs().first()
                val number = kosList.indexOf(kosmogliph)
                effect = hexAppliers.get(number)
                amplifier = 1
            }
            if (user is PlayerEntity) {
                for (hex in hexAppliers){
                    user.removeStatusEffect(hex)
                }
                user.addStatusEffect(StatusEffectInstance(effect, 2400, amplifier))
                user.itemCooldownManager.set(stack.item, 400)
                user.stopUsingItem()
            }
        }
        super.usageTick(world, user, stack, remainingUseTicks)
    }

    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity?, remainingUseTicks: Int) {
        if (user is PlayerEntity && !user.itemCooldownManager.isCoolingDown(stack.item)) {
            var effect = hexes.get(world.random.rangeInclusive(0, 5))
            var amplifier = 0
            if (stack.hasKosmogliphs()) {
                val kosmogliph = stack.getKosmogliphs().first()
                val number = kosList.indexOf(kosmogliph)
                effect = hexes.get(number)
                amplifier = 1
            }
            for (hex in hexes){
                user.removeStatusEffect(hex)
            }
            user.addStatusEffect(StatusEffectInstance(effect, 200, amplifier))
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