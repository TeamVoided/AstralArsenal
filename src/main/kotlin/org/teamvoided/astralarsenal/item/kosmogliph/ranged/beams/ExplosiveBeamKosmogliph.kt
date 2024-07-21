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
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.AstralGreathammerItem
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.world.explosion.WeakExplosionBehavior
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class ExplosiveBeamKosmogliph (id: Identifier) :
    SimpleKosmogliph(id, { it.item is SwordItem && it.item !is AstralGreathammerItem }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        val result = player.raycast(100.0, 1f, false)
        val distance = sqrt(sqrt((player.eyePos.x - result.pos.x).pow(2) + (player.eyePos.z - result.pos.z).pow(2)).pow(2) + (player.eyePos.y - result.pos.y).pow(2))
        val entities = mutableListOf<Entity>()
        val interval = (distance.times(2))
        for (i in 0..interval.roundToInt()) {
            entities.addAll(
                world.getOtherEntities(
                    player, Box(
                        (lerp(player.eyePos.x, result.pos.x, i / interval)) + 0.5,
                        (lerp(player.eyePos.y, result.pos.y, i / interval)) + 0.5,
                        (lerp(player.eyePos.z, result.pos.z, i / interval)) + 0.5,
                        (lerp(player.eyePos.x, result.pos.x, i / interval)) - 0.5,
                        (lerp(player.eyePos.y, result.pos.y, i / interval)) - 0.5,
                        (lerp(player.eyePos.z, result.pos.z, i / interval)) - 0.5
                    )
                )
            )
            if (entities.isNotEmpty()){
                break
            }
            if (!player.world.isClient) {
                val serverWorld = player.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.FLAME,
                    (lerp(player.eyePos.x, result.pos.x, i / interval)),
                    (lerp(player.eyePos.y, result.pos.y, i / interval)),
                    (lerp(player.eyePos.z, result.pos.z, i / interval)),
                    50,
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
            world.createExplosion(
                player,
                entity.damageSources.explosion(player,player),
                WeakExplosionBehavior(),
                entity.x,
                entity.y,
                entity.z,
                2.0f,
                false,
                World.
                ExplosionSourceType.TNT)
            if (!player.world.isClient) {
                val serverWorld = player.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.FLAME,
                    entity.x,
                    entity.y,
                    entity.z,
                    500,
                    1.5,
                    1.5,
                    1.5,
                    0.0
                )
            }
        }
        if(entities.isEmpty()){
            world.createExplosion(
                player,
                player.damageSources.explosion(player,player),
                WeakExplosionBehavior(),
                result.pos.x,
                result.pos.y,
                result.pos.z,
                2.0f,
                false,
                World.
                ExplosionSourceType.TNT)
            if (!player.world.isClient) {
                val serverWorld = player.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.FLAME,
                    result.pos.x,
                    result.pos.y,
                    result.pos.z,
                    500,
                    1.5,
                    1.5,
                    1.5,
                    0.0
                )
            }
        }
        if (!player.isCreative) {
            player.itemCooldownManager.set(player.getStackInHand(hand).item, 400)
        }
    }
}