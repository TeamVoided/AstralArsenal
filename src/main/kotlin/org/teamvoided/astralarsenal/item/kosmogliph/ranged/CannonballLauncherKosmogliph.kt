package org.teamvoided.astralarsenal.item.kosmogliph.ranged

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
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class CannonballLauncherKosmogliph(
    id: Identifier,
) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_CANNONBALL_LAUNCHER) }), RangedWeaponKosmogliph {
    override fun preUse(world: World, player: PlayerEntity, hand: Hand) {
        if (world.isClient) return

        val stack = player.getStackInHand(hand)
        val chargedProjectiles = stack.get(DataComponentTypes.CHARGED_PROJECTILES)
        val fireBall = chargedProjectiles != null && !chargedProjectiles.isEmpty
        if (fireBall) {
            stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT)
            val snowballEntity = CannonballEntity(world, player)
            setPropertiesTwo(snowballEntity, player.pitch, player.yaw, 0.0f, 3.0f, 0.0f)
            snowballEntity.addVelocity(0.0, 0.0, 0.0)
            world.spawnEntity(snowballEntity)
            if(player.getStackInHand(hand).enchantments.enchantments.any {it.isRegistryKey(Enchantments.MULTISHOT)}){
                val one: Int
                val two: Int
                if (world.random.range(0,2) == 1){
                    one = 15
                    two = -15
                }
                else{
                    one = -15
                    two = 15
                }
                val snowballEntity2 = CannonballEntity(world, player)
                setPropertiesTwo(snowballEntity2, player.pitch, player.yaw + one, 0.0f, 2.0f, 0.0f)
                snowballEntity2.setPosition(player.x, player.eyeY, player.z)
                world.spawnEntity(snowballEntity2)
                val snowballEntity3 = CannonballEntity(world, player)
                setPropertiesTwo(snowballEntity3, player.pitch, player.yaw + two, 0.0f, 2.0f, 0.0f)
                snowballEntity3.setPosition(player.x, player.eyeY, player.z)
                world.spawnEntity(snowballEntity3)
            }
            if (!player.isCreative) {
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 100)
            }
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.BLOCK_HEAVY_CORE_PLACE,
                SoundCategory.PLAYERS,
                1.0F,
                1.0f
            )
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