package org.teamvoided.astralarsenal.kosmogliph.melee

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.Projectiles.CannonballEntity
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

class CannonballKosmogliph(id: Identifier) : SimpleKosmogliph(id, AstralItemTags.SUPPORTS_CANNONBALL) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack>? {
        if (!world.isClient) {
            CannonballEntity(world, player).apply {
                setProperties(player, player.pitch, player.yaw, 0.0f, 0.1f, 0.0f)
                addVelocity(0.0, 0.1, 0.0)
                world.spawnEntity(this)
            }

            if (!player.isCreative) {
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 40)
            }
            player.getStackInHand(hand).damageEquipment(
                20, player,
                if (hand == Hand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND
            )
        }
        return null
    }
}