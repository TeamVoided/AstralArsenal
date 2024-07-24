package org.teamvoided.astralarsenal.item.kosmogliph.ranged

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ChargedProjectilesComponent
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.CrossbowItem
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class ShotgunKosmogliph (
    id: Identifier,
) : SimpleKosmogliph(id, { it.item is CrossbowItem }), RangedWeaponKosmogliph {
    override fun preUse(world: World, player: PlayerEntity, hand: Hand) {
        if (world.isClient) return

        val stack = player.getStackInHand(hand)
        val chargedProjectiles = stack.get(DataComponentTypes.CHARGED_PROJECTILES)
        if (chargedProjectiles != null && !chargedProjectiles.isEmpty) {
            repeat(10){
                val snowballEntity = ArrowEntity(world, player, chargedProjectiles.projectiles[0], stack)
                snowballEntity.setProperties(player,
                player.pitch + world.random.nextDouble().minus(0.5).times(15).toFloat(),
                player.yaw + world.random.nextDouble().minus(0.5).times(15).toFloat(),
                0.0f, 3.0f, 0.0f)
                snowballEntity.addVelocity(0.0, 0.0, 0.0)
                snowballEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY
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
            if (hand == Hand.MAIN_HAND){
            stack.damageEquipment(1, player, EquipmentSlot.MAINHAND)}
            else if (hand == Hand.OFF_HAND){
                stack.damageEquipment(1, player, EquipmentSlot.OFFHAND)
            }
            stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT)
        }
    }
}