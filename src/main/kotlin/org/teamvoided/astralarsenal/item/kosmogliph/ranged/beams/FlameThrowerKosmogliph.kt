package org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.SwordItem
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.coroutine.mcCoroutineTask
import org.teamvoided.astralarsenal.coroutine.ticks
import org.teamvoided.astralarsenal.entity.BoomEntity
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.AstralGreathammerItem
import org.teamvoided.astralarsenal.item.RailgunItem
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.seconds

class FlameThrowerKosmogliph(id: Identifier) :
    SimpleKosmogliph(id, { it.item is RailgunItem }){
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        for(i in 0..500) {
            mcCoroutineTask(delay = (i * 0.005).seconds) {
                val result = player.raycast(10.0, 1f, false)
                val distance = sqrt(
                    sqrt((player.eyePos.x - result.pos.x).pow(2) + (player.eyePos.z - result.pos.z).pow(2)).pow(2) + ((player.eyePos.y- 0.5) - result.pos.y).pow(
                        2
                    )
                )
                val entities = mutableListOf<Entity>()
                val interval = (distance.times(2))
                for (j in 0..interval.roundToInt()) {
                    entities.addAll(
                        world.getOtherEntities(
                            player, Box(
                                (lerp(player.eyePos.x, result.pos.x, j / interval)) + 0.5,
                                (lerp(player.eyePos.y- 0.5, result.pos.y, j / interval)) + 0.5,
                                (lerp(player.eyePos.z, result.pos.z, j / interval)) + 0.5,
                                (lerp(player.eyePos.x, result.pos.x, j / interval)) - 0.5,
                                (lerp(player.eyePos.y- 0.5, result.pos.y, j / interval)) - 0.5,
                                (lerp(player.eyePos.z, result.pos.z, j / interval)) - 0.5
                            )
                        )
                    )
                    if (!player.world.isClient) {
                        val serverWorld = player.world as ServerWorld
                        serverWorld.spawnParticles(
                            ParticleTypes.FLAME,
                            (lerp(player.eyePos.x, result.pos.x, j / interval)),
                            (lerp(player.eyePos.y- 0.5, result.pos.y, j / interval)),
                            (lerp(player.eyePos.z, result.pos.z, j / interval)),
                            1,
                            0.2,
                            0.2,
                            0.2,
                            0.0
                        )

                    }
                }
                for (entity in entities) {
                    entity.damage(entity.damageSources.inFire(), 3f)
                    entity.setOnFireFor(200)
                }
            }
        }
        if (!player.isCreative) {
            player.itemCooldownManager.set(player.getStackInHand(hand).item, 400)
        }
    }
}