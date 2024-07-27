package org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams

import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.RailgunItem

class RayofFrostKosmogliph(id: Identifier) : AbstractRailgunKosmogliph(id, { it.item is RailgunItem }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        val raycast = raycast(world, player, 100.0, ParticleTypes.SNOWFLAKE, 3, Vec3d(0.2, 0.2, 0.2), 0.0)
        raycast.first.damageAll(
            DamageSource(
                AstralDamageTypes.getHolder(world.registryManager, DamageTypes.FREEZE),
                player,
                player
            ), 5f
        )

        raycast.first.forEach { entity ->
            entity.frozenTicks += 300
        }

        world.playSound(
            null,
            player.x,
            player.y,
            player.z,
            AstralSounds.RAILGUN,
            SoundCategory.PLAYERS,
            1.0F,
            1.0f
        )

        if (!player.isCreative) {
            player.itemCooldownManager.set(player.getStackInHand(hand).item, 400)
        }
    }
}