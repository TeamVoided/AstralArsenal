package org.teamvoided.astralarsenal.item.kosmogliph.ranged

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ChargedProjectilesComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.CrossbowItem
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class CannonballLauncherKosmogliph(
    id: Identifier,
) : SimpleKosmogliph(id, { AstralArsenal.LOGGER.info("{}", it.item is CrossbowItem); it.item is CrossbowItem }), RangedWeaponKosmogliph {
    override fun preUse(world: World, player: PlayerEntity, hand: Hand) {
        if (world.isClient) return

        val stack = player.getStackInHand(hand)
        val chargedProjectiles = stack.get(DataComponentTypes.CHARGED_PROJECTILES)
        val fireBall = chargedProjectiles != null && !chargedProjectiles.isEmpty
        if (fireBall) {
            stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT)
            val snowballEntity = CannonballEntity(world, player)
            snowballEntity.setProperties(player, player.pitch, player.yaw, 0.0f, 1.0f, 0.0f)
            snowballEntity.addVelocity(0.0, 0.0, 0.0)
            world.spawnEntity(snowballEntity)
            if (!player.isCreative) {
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 100)
        }
    }
}
}