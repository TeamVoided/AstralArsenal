package org.teamvoided.astralarsenal.item.kosmogliph.ranged.strikes

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ChargedProjectilesComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.RegistryKey
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.BeamOfLightArrowEntity
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.RangedWeaponKosmogliph
import org.teamvoided.astralarsenal.util.hasMultiShot

class DevastateKosmogliph(
    id: Identifier,
) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_DEVASTATE) }),
    RangedWeaponKosmogliph {
    override fun preUse(world: World, player: PlayerEntity, hand: Hand) {
        if (world.isClient) return

        val stack = player.getStackInHand(hand)
        val chargedProjectiles = stack.get(DataComponentTypes.CHARGED_PROJECTILES)
        val fireBall = chargedProjectiles != null && !chargedProjectiles.isEmpty
        if (fireBall) {
            stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT)
            val snowballEntity = BeamOfLightArrowEntity(world, player)
            setPropertiesTwo(snowballEntity, player.pitch, player.yaw, 0.0f, 2.5f, 0.0f)
            snowballEntity.setPosition(player.x, player.eyeY, player.z)
            snowballEntity.DOT = false
            snowballEntity.side = 3
            snowballEntity.THRUST = 1.0
            snowballEntity.TIMEACTIVE = 20
            snowballEntity.WINDUP = 50
            snowballEntity.DMG = 8
            snowballEntity.trackTime = 45
            snowballEntity.balls = player
            snowballEntity.hard_damage = 8
            world.spawnEntity(snowballEntity)
            if (player.getStackInHand(hand).hasMultiShot()) {
                val one: Int
                val two: Int
                if (world.random.range(0, 2) == 1) {
                    one = 15
                    two = -15
                } else {
                    one = -15
                    two = 15
                }
                val snowballEntity2 = BeamOfLightArrowEntity(world, player)
                setPropertiesTwo(snowballEntity2, player.pitch, player.yaw + one, 0.0f, 2.5f, 0.0f)
                snowballEntity2.setPosition(player.x, player.eyeY, player.z)
                snowballEntity2.DOT = snowballEntity.DOT
                snowballEntity2.side = snowballEntity.side
                snowballEntity2.THRUST = snowballEntity.THRUST
                snowballEntity2.TIMEACTIVE = snowballEntity.TIMEACTIVE
                snowballEntity2.WINDUP = (snowballEntity.WINDUP * 0.5).toInt()
                snowballEntity2.DMG = snowballEntity.DMG
                snowballEntity2.trackTime = (snowballEntity.trackTime * 0.5).toInt()
                snowballEntity2.balls = player
                snowballEntity2.hard_damage = snowballEntity.hard_damage
                world.spawnEntity(snowballEntity2)
                val snowballEntity3 = BeamOfLightArrowEntity(world, player)
                setPropertiesTwo(snowballEntity3, player.pitch, player.yaw + two, 0.0f, 2.5f, 0.0f)
                snowballEntity3.setPosition(player.x, player.eyeY, player.z)
                snowballEntity3.DOT = snowballEntity.DOT
                snowballEntity3.side = snowballEntity.side
                snowballEntity3.THRUST = snowballEntity.THRUST
                snowballEntity3.TIMEACTIVE = snowballEntity.TIMEACTIVE
                snowballEntity3.WINDUP = (snowballEntity.WINDUP * 1.5).toInt()
                snowballEntity3.DMG = snowballEntity.DMG
                snowballEntity3.trackTime = (snowballEntity.trackTime * 1.5).toInt()
                snowballEntity3.balls = player
                snowballEntity3.hard_damage = snowballEntity.hard_damage
                world.spawnEntity(snowballEntity3)
            }
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE,
                SoundCategory.PLAYERS,
                1.0F,
                1.0f
            )
            if (!player.isCreative) {
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 100)
            }
            if (hand == Hand.MAIN_HAND) {
                stack.damageEquipment(1, player, EquipmentSlot.MAINHAND)
            } else if (hand == Hand.OFF_HAND) {
                stack.damageEquipment(1, player, EquipmentSlot.OFFHAND)
            }
        }
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.PIERCING)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}