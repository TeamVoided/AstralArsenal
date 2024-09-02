package org.teamvoided.astralarsenal.item.kosmogliph.ranged

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ChargedProjectilesComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.FireworkRocketEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKey
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.util.hasMultiShot

class ShotgunKosmogliph(
    id: Identifier,
) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_SHOTGUN) }), RangedWeaponKosmogliph {
    override fun preUse(world: World, player: PlayerEntity, hand: Hand) {
        if (world.isClient) return

        val stack = player.getStackInHand(hand)
        val chargedProjectiles = stack.get(DataComponentTypes.CHARGED_PROJECTILES)
        if (chargedProjectiles != null && !chargedProjectiles.isEmpty) {
            var arrows = 5
            if (player.getStackInHand(hand).hasMultiShot()) {
                arrows = 15
            }
            for (i in 1..arrows) {
                var snowballEntity: ProjectileEntity
                if (chargedProjectiles.projectiles[0].item == Items.FIREWORK_ROCKET) {
                    snowballEntity = FireworkRocketEntity(
                        world,
                        chargedProjectiles.projectiles[0],
                        player,
                        player.getX(),
                        player.getEyeY() - 0.15f.toDouble(),
                        player.getZ(),
                        true
                    );
                } else {
                    snowballEntity = ArrowEntity(world, player, chargedProjectiles.projectiles[0], stack)
                    snowballEntity.isCritical = true
                    if (i == 1) {
                        snowballEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED
                    } else {
                        snowballEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY
                    }
                }
                if (i == 1) {
                    snowballEntity.setProperties(
                        player, player.pitch, player.yaw,
                        0.0f, 2.0f, 0.0f
                    )
                } else {
                    setPropertiesTwo(
                        snowballEntity,
                        player.pitch,
                        player.yaw,
                        0.0f, 2.0f, 10.0f
                    )
                }
                world.spawnEntity(snowballEntity)
            }
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.ITEM_CROSSBOW_SHOOT,
                SoundCategory.PLAYERS,
                1.0F,
                1.0f
            )
            if (hand == Hand.MAIN_HAND) {
                stack.damageEquipment(1, player, EquipmentSlot.MAINHAND)
            } else if (hand == Hand.OFF_HAND) {
                stack.damageEquipment(1, player, EquipmentSlot.OFFHAND)
            }
            stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT)
        }
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.PIERCING)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}