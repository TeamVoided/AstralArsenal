package org.teamvoided.astralarsenal.item.kosmogliph.ranged

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ChargedProjectilesComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.registry.RegistryKey
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.joml.Quaternionf
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.util.hasMultiShot
import org.teamvoided.astralarsenal.util.setVelocity

class CannonballLauncherKosmogliph(
    id: Identifier,
) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_CANNONBALL_LAUNCHER) }), RangedWeaponKosmogliph {
    override fun preUse(world: World, player: PlayerEntity, hand: Hand) {
        if (world.isClient) return

        val stack = player.getStackInHand(hand)
        val chargedProjectiles = stack.get(DataComponentTypes.CHARGED_PROJECTILES) ?: return

        if (!chargedProjectiles.isEmpty) {
            stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT)

            val count = if (stack.hasMultiShot()) 3 else 1
            for (ball in 0..<count) {
                val offset = listOf(0, 15, -15)
                val cannonball = CannonballEntity(world, player)
                setPropertiesTwo(cannonball, player.pitch, player.yaw + offset[ball], 0.0f, 3.0f, 0.0f)
                world.spawnEntity(cannonball)
            }

            if (!player.isCreative) player.itemCooldownManager.set(stack.item, 100)
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

            if (hand == Hand.MAIN_HAND) stack.damageEquipment(count, player, EquipmentSlot.MAINHAND)
            else if (hand == Hand.OFF_HAND) stack.damageEquipment(count, player, EquipmentSlot.OFFHAND)
        }
    }

    fun setVelocity(
        projectile: ProjectileEntity, shooter: PlayerEntity, speed: Float, yawOffset: Float, divergence: Float
    ) {
        val invRotVec = shooter.getOppositeRotationVector(1.0f)
        val rotQat = Quaternionf().setAngleAxis(yawOffset * 0.017453292, invRotVec.x, invRotVec.y, invRotVec.z)
        val rotVec = shooter.getRotationVec(1.0f)
        val output = rotVec.toVector3f().rotate(rotQat)

        projectile.setVelocity(output, speed, divergence)
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.PIERCING, Enchantments.MULTISHOT)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}