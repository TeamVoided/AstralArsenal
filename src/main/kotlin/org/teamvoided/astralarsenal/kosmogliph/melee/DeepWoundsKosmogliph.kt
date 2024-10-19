package org.teamvoided.astralarsenal.kosmogliph.melee

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.*
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.DeepWoundEntity
import org.teamvoided.astralarsenal.entity.SlashEntity
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.util.playSound

class DeepWoundsKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_DEEP_WOUNDS) }) {
//    override fun onUse(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack>? {
//        if (!world.isClient) {
//            var w = -20
//            repeat(40) {
//                val snowballEntity = DeepWoundEntity(world, player)
//                setPropertiesTwo(snowballEntity, player.pitch, player.yaw + w, 0.0f, 1.0f, 0.0f)
//                world.spawnEntity(snowballEntity)
//                w++
//            }
//            world.playSound(
//                null,
//                player.x,
//                player.y,
//                player.z,
//                SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
//                SoundCategory.PLAYERS,
//                1.0F,
//                0.1f
//            )
//            world.playSound(
//                null,
//                player.x,
//                player.y,
//                player.z,
//                SoundEvents.BLOCK_GRINDSTONE_USE,
//                SoundCategory.PLAYERS,
//                1.0F,
//                0.1f
//            )
//            if (!player.isCreative) {
//                player.itemCooldownManager.set(player.getStackInHand(hand).item, 1800)
//            }
//        }
//        return null
//    }

    override fun onUse(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        player.setCurrentHand(hand)
        return TypedActionResult(ActionResult.CONSUME_PARTIAL, player.getStackInHand(hand))
    }

    override fun getUseAction(stack: ItemStack): UseAction {
        return UseAction.BOW
    }

    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        val ticks = getUseTicks(stack, user) - remainingUseTicks
        if (ticks == 0){
            world.playSound(
                user.pos, SoundEvents.BLOCK_TRIAL_SPAWNER_ABOUT_TO_SPAWN_ITEM, SoundCategory.PLAYERS, 1.0F, 1.0f
            )
        }
        super.usageTick(world, user, stack, remainingUseTicks)
    }

    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
        val ticks = getUseTicks(stack, user) - remainingUseTicks
        if (!world.isClient && ticks >= 40) {
            var w = -20
            repeat(40) {
                val snowballEntity = DeepWoundEntity(world, user)
                setPropertiesTwo(snowballEntity, user.pitch, user.yaw + w, 0.0f, 1.0f, 0.0f)
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
                0.1f
            )
            world.playSound(
                null,
                user.x,
                user.y,
                user.z,
                SoundEvents.BLOCK_TRIAL_SPAWNER_SPAWN_ITEM,
                SoundCategory.PLAYERS,
                1.0F,
                0.5f
            )
            if (user is PlayerEntity && !user.isCreative) {
                user.itemCooldownManager.set(stack.item, 1200)
            }
        }
        else{
            if (user is PlayerEntity && !user.isCreative) {
                user.itemCooldownManager.set(stack.item, 20)
            }
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }

    override fun getUseTicks(stack: ItemStack, livingEntity: LivingEntity): Int {
        return 72000
    }

    val over = listOf(
        AstralEffects.OVERHEAL
    )

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        attacker.absorptionAmount += 0.5f
        if (!target.isAlive) {
            var over_levels = target.maxHealth.toInt()
            val effects_two = attacker.statusEffects.filter { over.contains(it.effectType) }
            if (effects_two.isNotEmpty()) {
                effects_two.forEach {
                    val w = it.amplifier
                    over_levels += w
                }
            }
            attacker.addStatusEffect(
                StatusEffectInstance(
                    AstralEffects.OVERHEAL,
                    100, over_levels,
                    false, false, true
                )
            )
            attacker.absorptionAmount += (over_levels * 0.25f)
        }
        super.postHit(stack, target, attacker)
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}