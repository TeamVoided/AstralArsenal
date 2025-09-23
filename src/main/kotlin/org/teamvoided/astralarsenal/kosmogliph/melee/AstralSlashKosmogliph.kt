package org.teamvoided.astralarsenal.kosmogliph.melee

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.*
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.SlashEntity
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.util.playSound

class AstralSlashKosmogliph(id: Identifier) : SimpleKosmogliph(id, AstralItemTags.SUPPORTS_ASTRAL_SLASH) {
    override fun getUseAction(stack: ItemStack): UseAction = UseAction.BOW
    override fun getUseTicks(stack: ItemStack, livingEntity: LivingEntity): Int = 72000
    override fun onUse(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        player.setCurrentHand(hand)
        return TypedActionResult(ActionResult.CONSUME_PARTIAL, player.getStackInHand(hand))
    }

    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        val ticks = getUseTicks(stack, user) - remainingUseTicks
        if (ticks == 20) {
            world.playSound(
                user.pos, SoundEvents.BLOCK_TRIAL_SPAWNER_EJECT_ITEM, SoundCategory.PLAYERS, 1.0F, 1.0f
            )
        }
        super.usageTick(world, user, stack, remainingUseTicks)
    }

    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
        val ticks = getUseTicks(stack, user) - remainingUseTicks
        if (!world.isClient && ticks >= 20) {
            var w = -20
            repeat(40) {
                val snowballEntity = SlashEntity(world, user)
                snowballEntity.setDmg(6f)
                setPropertiesTwo(snowballEntity, user.pitch, user.yaw + w, 0.0f, 2.0f, 0.0f)
                world.spawnEntity(snowballEntity)
                w++
            }
            world.playSound(
                null,
                user.x,
                user.y,
                user.z,
                SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
                SoundCategory.PLAYERS,
                1.0F,
                1.0f
            )
            world.playSound(
                null,
                user.x,
                user.y,
                user.z,
                SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE,
                SoundCategory.PLAYERS,
                1.0F,
                1.0f
            )
            if (user is PlayerEntity) {
                if (!user.isCreative) {
                    user.itemCooldownManager.set(stack.item, 200)
                }
            }
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }
}