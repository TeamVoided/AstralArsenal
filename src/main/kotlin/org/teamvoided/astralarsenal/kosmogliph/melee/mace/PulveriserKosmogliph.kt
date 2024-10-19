package org.teamvoided.astralarsenal.kosmogliph.melee.mace

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.Holder
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.*
import net.minecraft.util.dynamic.Codecs
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.armor.SlamKosmogliph
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
        val data = stack.get(AstralItemComponents.PULVERISER_DATA) ?: return
        if(data.slamming){
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
                if(usedTicks >= 100) 2.5
                else if(usedTicks >= 50) 2.0
                else 1.5
                )
        if (usedTicks >= 20) {
            stack.set(AstralItemComponents.PULVERISER_DATA, Data(usedTicks, true))
            val boost = user.rotationVector.multiply(1.0, 1.0, 1.0).normalize().multiply(tickBoost)
            user.setVelocity(user.velocity.x + boost.x, user.velocity.y + (boost.y * 0.9), user.velocity.z + boost.z)
            user.velocityModified
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val data = stack.get(AstralItemComponents.PULVERISER_DATA) ?: return
        if(data.slamming){
            if(entity is LivingEntity && entity.getStackInHand(Hand.MAIN_HAND) != stack && entity.getStackInHand(Hand.OFF_HAND) != stack){
                stack.set(AstralItemComponents.PULVERISER_DATA, Data(0, false))
                if (entity is PlayerEntity && !entity.isCreative) {
                    entity.itemCooldownManager.set(stack.item, 200)
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    class Data(
        val ticks: Int,
        val slamming: Boolean
    ) {
        companion object {
            val CODEC: Codec<Data> = RecordCodecBuilder.create { builder ->
                val group = builder.group(
                    Codec.INT.fieldOf("ticks").forGetter { it.ticks },
                    Codec.BOOL.fieldOf("slamming").forGetter { it.slamming }
                )

                group.apply(builder, PulveriserKosmogliph::Data)
            }
        }
    }

}