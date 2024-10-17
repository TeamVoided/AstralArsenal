package org.teamvoided.astralarsenal.kosmogliph.melee.mace

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.*
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.util.playSound
import org.teamvoided.astralarsenal.world.explosion.maceExplosions.MaceExplosionBehavior
import org.teamvoided.astralarsenal.world.explosion.maceExplosions.MaceStrongExplosionBehavior
import org.teamvoided.astralarsenal.world.explosion.maceExplosions.MaceWeakExplosionBehavior

class WindEruptionKosmogliph (id: Identifier) :
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
        val explosionBehavior = (
                if(usedTicks >= 100) MaceStrongExplosionBehavior(user)
                else if(usedTicks >= 50) MaceExplosionBehavior(user)
                else MaceWeakExplosionBehavior(user)
                )
        val jumpPower =(
                if(usedTicks >= 100) 2.5
                else if(usedTicks >= 50) 1.5
                else 1.0
                )
        if(usedTicks >= 20){
            world.createExplosion(
                user, user.damageSources.explosion(null, user),
                explosionBehavior,
                user.x,
                user.y,
                user.z,
                2.0f,
                false,
                World.ExplosionSourceType.MOB,
                ParticleTypes.SMALL_GUST,
                ParticleTypes.GUST_EMITTER_LARGE,
                SoundEvents.ENTITY_BREEZE_WIND_BURST)
            user.addVelocity(0.0, jumpPower,0.0)
            user.velocityDirty
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }

}