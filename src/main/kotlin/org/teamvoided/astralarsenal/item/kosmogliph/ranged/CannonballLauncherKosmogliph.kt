package org.teamvoided.astralarsenal.item.kosmogliph.ranged

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ChargedProjectilesComponent
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.CrossbowItem
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class CannonballLauncherKosmogliph(
    id: Identifier,
) : SimpleKosmogliph(id, { it.item is CrossbowItem }), RangedWeaponKosmogliph {
    override fun preUse(world: World, player: PlayerEntity, hand: Hand) {
        if (world.isClient) return

        val stack = player.getStackInHand(hand)
        val chargedProjectiles = stack.get(DataComponentTypes.CHARGED_PROJECTILES)
        val fireBall = chargedProjectiles != null && !chargedProjectiles.isEmpty
        if (fireBall) {
            stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT)
            val snowballEntity = CannonballEntity(world, player)
            snowballEntity.setProperties(player, player.pitch, player.yaw, 0.0f, 3.0f, 0.0f)
            snowballEntity.addVelocity(0.0, 0.0, 0.0)
            world.spawnEntity(snowballEntity)
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
            if (hand == Hand.MAIN_HAND){
                stack.damageEquipment(1, player, EquipmentSlot.MAINHAND)}
            else if (hand == Hand.OFF_HAND){
                stack.damageEquipment(1, player, EquipmentSlot.OFFHAND)
            }
    }
}
}