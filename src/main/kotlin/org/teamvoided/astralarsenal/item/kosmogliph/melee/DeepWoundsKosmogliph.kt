package org.teamvoided.astralarsenal.item.kosmogliph.melee

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.AxeItem
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.teamvoided.astralarsenal.coroutine.mcCoroutineTask
import org.teamvoided.astralarsenal.entity.DeepWoundEntity
import org.teamvoided.astralarsenal.entity.SlashEntity
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import kotlin.time.Duration.Companion.seconds

class DeepWoundsKosmogliph (id: Identifier) : SimpleKosmogliph(id, { it.item is SwordItem || it.item is AxeItem }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        if (!world.isClient) {
            var w = -20
            repeat(40) {
                    val snowballEntity = DeepWoundEntity(world, player)
                    snowballEntity.setProperties(player, player.pitch, player.yaw + w, 0.0f, 1.0f, 0.0f)
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

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        attacker.absorptionAmount += attacker.absorptionAmount + 0.5f
        super.postHit(stack, target, attacker)
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}