package org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams

import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageSource
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
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.AstralGreathammerItem
import org.teamvoided.astralarsenal.item.RailgunItem
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class SnipeKosmogliph (id: Identifier) :
    SimpleKosmogliph(id, {it.item is RailgunItem}) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        var result = player.raycast(100.0, 1f, false)
        var distance = sqrt(
            sqrt((player.eyePos.x - result.pos.x).pow(2) + (player.eyePos.z - result.pos.z).pow(2)).pow(2) + ((player.eyePos.y- 0.5) - result.pos.y).pow(
                2
            )
        )
        var entities = mutableListOf<Entity>()
        var interval = (distance.times(2))
        for (i in 0..interval.roundToInt()) {
            entities.addAll(
                world.getOtherEntities(
                    player, Box(
                        (lerp(player.eyePos.x, result.pos.x, i / interval)) + 0.5,
                        (lerp(player.eyePos.y- 0.5, result.pos.y, i / interval)) + 0.5,
                        (lerp(player.eyePos.z, result.pos.z, i / interval)) + 0.5,
                        (lerp(player.eyePos.x, result.pos.x, i / interval)) - 0.5,
                        (lerp(player.eyePos.y- 0.5, result.pos.y, i / interval)) - 0.5,
                        (lerp(player.eyePos.z, result.pos.z, i / interval)) - 0.5
                    )
                )
            )
            if (!player.world.isClient) {
                val serverWorld = player.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.END_ROD,
                    (lerp(player.eyePos.x, result.pos.x, i / interval)),
                    (lerp(player.eyePos.y- 0.5, result.pos.y, i / interval)),
                    (lerp(player.eyePos.z, result.pos.z, i / interval)),
                    2,
                    0.2,
                    0.2,
                    0.2,
                    0.0
                )

            }
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
        for (entity in entities) {
            val random = world.random.range(1, 10)
            if(random == 1){
                entity.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.RAILED),
                        player,
                        player
                    ), 7.5f)}
            else{
                entity.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.NON_RAILED),
                        player,
                        player
                    ), 7.5f)
            }
        }
        if (!player.isCreative) {
            player.itemCooldownManager.set(player.getStackInHand(hand).item, 400)
        }
        if (!world.isClient) {
        mcCoroutineTask(delay = 20.ticks) {
            result = player.raycast(100.0, 1f, false)
            distance = sqrt(
                sqrt((player.eyePos.x - result.pos.x).pow(2) + (player.eyePos.z - result.pos.z).pow(2)).pow(2) + ((player.eyePos.y- 0.5) - result.pos.y).pow(
                    2
                )
            )
            entities = mutableListOf<Entity>()
            interval = (distance.times(2))
            for (i in 0..interval.roundToInt()) {
                entities.addAll(
                    world.getOtherEntities(
                        player, Box(
                            (lerp(player.eyePos.x, result.pos.x, i / interval)) + 0.5,
                            (lerp(player.eyePos.y- 0.5, result.pos.y, i / interval)) + 0.5,
                            (lerp(player.eyePos.z, result.pos.z, i / interval)) + 0.5,
                            (lerp(player.eyePos.x, result.pos.x, i / interval)) - 0.5,
                            (lerp(player.eyePos.y- 0.5, result.pos.y, i / interval)) - 0.5,
                            (lerp(player.eyePos.z, result.pos.z, i / interval)) - 0.5
                        )
                    )
                )
                if (!player.world.isClient) {
                    val serverWorld = player.world as ServerWorld
                    serverWorld.spawnParticles(
                        ParticleTypes.END_ROD,
                        (lerp(player.eyePos.x, result.pos.x, i / interval)),
                        (lerp(player.eyePos.y- 0.5, result.pos.y, i / interval)),
                        (lerp(player.eyePos.z, result.pos.z, i / interval)),
                        5,
                        0.2,
                        0.2,
                        0.2,
                        0.0
                    )

                }
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
            for (entity in entities) {
                val random = world.random.range(1, 10)
                if(random == 1){
                    entity.damage(
                        DamageSource(
                            AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.RAILED),
                            player,
                            player
                        ), 7.5f)}
                else{
                    entity.damage(
                        DamageSource(
                            AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.NON_RAILED),
                            player,
                            player
                        ), 7.5f)
                }
            }
        }
            if (!player.isCreative) {
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 400)
            }
        }
    }
}