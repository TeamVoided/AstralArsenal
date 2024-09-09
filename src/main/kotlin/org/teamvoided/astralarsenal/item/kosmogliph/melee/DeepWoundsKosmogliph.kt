package org.teamvoided.astralarsenal.item.kosmogliph.melee

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.DeepWoundEntity
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class DeepWoundsKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_DEEP_WOUNDS) }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        if (!world.isClient) {
            var w = -20
            repeat(40) {
                val snowballEntity = DeepWoundEntity(world, player)
                setPropertiesTwo(snowballEntity, player.pitch, player.yaw + w, 0.0f, 1.0f, 0.0f)
                world.spawnEntity(snowballEntity)
                w++
            }
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
                SoundCategory.PLAYERS,
                1.0F,
                0.1f
            )
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.BLOCK_GRINDSTONE_USE,
                SoundCategory.PLAYERS,
                1.0F,
                0.1f
            )
            if (!player.isCreative) {
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 1800)
            }
        }
    }
    val over = listOf(
        AstralEffects.OVERHEAL
    )
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        attacker.absorptionAmount += 0.5f
        if (!target.isAlive) {
            val ow = attacker as LivingEntity
            var over_levels = target.maxHealth.toInt()
            val effects_two = ow.statusEffects.filter { over.contains(it.effectType) }
            if (effects_two.isNotEmpty()) {
                effects_two.forEach {
                    val w = it.amplifier
                    over_levels += w
                }
            }
            ow.addStatusEffect(
                StatusEffectInstance(
                    AstralEffects.OVERHEAL,
                    100, over_levels,
                    false, false, true
                )
            )
            ow.absorptionAmount += (over_levels * 0.25f)
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