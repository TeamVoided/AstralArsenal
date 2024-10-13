package org.teamvoided.astralarsenal.kosmogliph.ranged

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
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.logic.getProjectileEntity
import org.teamvoided.astralarsenal.kosmogliph.logic.setShootVelocity
import org.teamvoided.astralarsenal.util.hasMultiShot

class ShotgunKosmogliph(
    id: Identifier,
) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_SHOTGUN) }), RangedWeaponKosmogliph {
    override fun preUse(world: World, player: PlayerEntity, hand: Hand) {
        if (world.isClient) return

        val weapon = player.getStackInHand(hand)
        val chargedProjectiles = weapon.get(DataComponentTypes.CHARGED_PROJECTILES) ?: return
        if (chargedProjectiles.isEmpty) return

        val arrows = if (player.getStackInHand(hand).hasMultiShot()) 15 else 5
        for (i in 1..arrows) {
            val canPickUp = i == 1
            val projectile = world.getProjectileEntity(chargedProjectiles.projectiles[0], weapon, player, canPickUp)
            if (canPickUp) projectile.setProperties(player, player.pitch, player.yaw, 0.0f, 2.0f, 0.0f)
            else projectile.setShootVelocity(player.pitch, player.yaw, 0.0f, 2.0f, 10.0f)
            world.spawnEntity(projectile)
        }
        world.playSound(
            null, player.x, player.y, player.z,
            SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0f
        )
        weapon.damageEquipment(1, player, if (hand == Hand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND)
        weapon.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT)
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.PIERCING)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}