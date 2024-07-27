package org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams

import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.astralarsenal.coroutine.mcCoroutineTask
import org.teamvoided.astralarsenal.coroutine.ticks
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralSounds

class SnipeKosmogliph(id: Identifier) : AbstractRailgunKosmogliph(id) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        snipe(world, player)
        mcCoroutineTask(delay = 20.ticks) { snipe(world, player) }

        if (!player.isCreative) {
            player.itemCooldownManager.set(player.getStackInHand(hand).item, 400)
        }
    }

    fun snipe(world: World, player: PlayerEntity) {
        val raycast = raycast(world, player, 100.0, ParticleTypes.END_ROD, 2, Vec3d(0.2, 0.2, 0.2), 0.0)
        raycast.first.damageAll(
            DamageSource(
                AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.BEAM_OF_LIGHT),
                player,
                player
            ), 7.5f
        )

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
    }
}