package org.teamvoided.astralarsenal.kosmogliph.ranged.strikes

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ChargedProjectilesComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.CrossbowItem
import net.minecraft.registry.RegistryKey
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.BlackHoleArrowEntity
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.RangedWeaponKosmogliph

class BlackHoleKosmogliph(
    id: Identifier,
) : SimpleKosmogliph(id, { it.item is CrossbowItem }),
    RangedWeaponKosmogliph {
    override fun preUse(world: World, player: PlayerEntity, hand: Hand) {
        if (world.isClient) return

        val stack = player.getStackInHand(hand)
        val chargedProjectiles = stack.get(DataComponentTypes.CHARGED_PROJECTILES)
        val fireBall = chargedProjectiles != null && !chargedProjectiles.isEmpty
        if (fireBall) {
            stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT)
            val snowballEntity = BlackHoleArrowEntity(world, player)
            snowballEntity.setProperties(player, player.pitch, player.yaw, 0.0f, 2.5f, 0.0f)
            snowballEntity.setPosition(player.x, player.eyeY, player.z)
            snowballEntity.balls = player
            world.spawnEntity(snowballEntity)
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE,
                SoundCategory.PLAYERS,
                1.0F,
                0.01f
            )
            if (!player.isCreative) {
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 1200)
            }
            if (hand == Hand.MAIN_HAND) {
                stack.damageEquipment(1, player, EquipmentSlot.MAINHAND)
            } else if (hand == Hand.OFF_HAND) {
                stack.damageEquipment(1, player, EquipmentSlot.OFFHAND)
            }
        }
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.MULTISHOT, Enchantments.PIERCING)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}