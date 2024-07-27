package org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams

import kotlinx.coroutines.delay
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.astralarsenal.coroutine.mcCoroutineTask
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import kotlin.time.Duration.Companion.seconds

class FlameThrowerKosmogliph(id: Identifier) : AbstractRailgunKosmogliph(id) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        mcCoroutineTask {
            val stack = player.getStackInHand(hand)
            for (i in 0..250) {
                if (stack != player.getStackInHand(hand)) return@mcCoroutineTask

                val raycast = raycast(
                    world,
                    player,
                    10.0,
                    ParticleTypes.FLAME,
                    1,
                    Vec3d(0.2, 0.2, 0.2),
                    0.0
                ) { _, _ ->
                    world.playSound(
                        null,
                        player.x,
                        player.y,
                        player.z,
                        SoundEvents.BLOCK_FIRE_AMBIENT,
                        SoundCategory.PLAYERS,
                        1.0F,
                        1.0f
                    )
                }

                raycast.first.damageAll(
                    DamageSource(
                        AstralDamageTypes.getHolder(world.registryManager, DamageTypes.IN_FIRE),
                        player,
                        player
                    ), 3f
                )

                delay((0.08).seconds)
            }
        }

        if (!player.isCreative) {
            player.itemCooldownManager.set(player.getStackInHand(hand).item, 400)
        }
    }
}