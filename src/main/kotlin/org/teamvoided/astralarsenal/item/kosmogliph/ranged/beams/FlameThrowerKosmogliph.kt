package org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams

import kotlinx.coroutines.delay
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.coroutine.mcCoroutineTask
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.item.RailgunItem
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds

class FlameThrowerKosmogliph(id: Identifier) :
    SimpleKosmogliph(id, { it.item is RailgunItem }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        if (!world.isClient) {
            mcCoroutineTask {
                val stack = player.getStackInHand(hand)
                for (i in 0..63) {
                    if (stack != player.getStackInHand(hand)) return@mcCoroutineTask

                    val result = player.raycast(10.0, 1f, false)
                    val distance = player.eyePos.distanceTo(result.pos)
                    val entities = mutableListOf<Entity>()

                    val interval = (distance * 2).roundToInt()
                    (0..interval).forEach { j ->
                        val pos = player.eyePos.lerp(result.pos, j / interval.toDouble())
                        entities.addAll(world.getOtherEntities(player, Box(pos, pos)))
                    }

                    if (!world.isClient && world is ServerWorld) {
                        for (j in 0..interval) {
                            world.spawnParticles(
                                ParticleTypes.FLAME,
                                (lerp(player.eyePos.x, result.pos.x, j / interval.toDouble())),
                                (lerp(player.eyePos.y - 0.5, result.pos.y, j / interval.toDouble())),
                                (lerp(player.eyePos.z, result.pos.z, j / interval.toDouble())),
                                1,
                                0.2,
                                0.2,
                                0.2,
                                0.0
                            )
                            if (i % 30 == 0) {
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
                        }
                    }
                    for (entity in entities) {
                        if (entity is LivingEntity) {
                            entity.damage(
                                DamageSource(
                                    AstralDamageTypes.getHolder(world.registryManager, DamageTypes.IN_FIRE),
                                    player,
                                    player
                                ), 5f
                            )
                            entity.setOnFireFor(200)
                        }
                    }

                    delay((0.08).seconds)
                }
            }

            if (!player.isCreative) {
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 400)
            }
        }
    }
}