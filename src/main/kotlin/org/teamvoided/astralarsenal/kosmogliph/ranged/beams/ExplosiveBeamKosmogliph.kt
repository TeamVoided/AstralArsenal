package org.teamvoided.astralarsenal.kosmogliph.ranged.beams

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.entity.MortarEntity
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.world.explosion.PenopticonExplosionBehavior
import org.teamvoided.astralarsenal.world.explosion.StrongExplosionBehavior
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class ExplosiveBeamKosmogliph(id: Identifier) :
    SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_EXPLOSIVE_BEAM) }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack>? {
        val result = player.raycast(100.0, 1f, false)
        val distance = sqrt(
            sqrt((player.eyePos.x - result.pos.x).pow(2) + (player.eyePos.z - result.pos.z).pow(2)).pow(2) + ((player.eyePos.y - 0.5) - result.pos.y).pow(
                2
            )
        )
        val entities = mutableListOf<Entity>()
        val interval = (distance.times(2))
        for (i in 0..interval.roundToInt()) {
            entities.addAll(
                world.getOtherEntities(
                    player, Box(
                        (lerp(player.eyePos.x, result.pos.x, i / interval)) + 0.5,
                        (lerp(player.eyePos.y - 0.5, result.pos.y, i / interval)) + 0.5,
                        (lerp(player.eyePos.z, result.pos.z, i / interval)) + 0.5,
                        (lerp(player.eyePos.x, result.pos.x, i / interval)) - 0.5,
                        (lerp(player.eyePos.y - 0.5, result.pos.y, i / interval)) - 0.5,
                        (lerp(player.eyePos.z, result.pos.z, i / interval)) - 0.5
                    )
                ).filter { it !is ProjectileEntity }
            )
            if (entities.isNotEmpty()) {
                break
            }
            if (!player.world.isClient) {
                val serverWorld = player.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.FLAME,
                    (lerp(player.eyePos.x, result.pos.x, i / interval)),
                    (lerp(player.eyePos.y - 0.5, result.pos.y, i / interval)),
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
            if (entity is CannonballEntity || entity is MortarEntity) {
                world.createExplosion(
                    entity,
                    entity.damageSources.explosion(entity, player),
                    PenopticonExplosionBehavior(),
                    entity.x,
                    entity.y,
                    entity.z,
                    3.0f,
                    false,
                    World.ExplosionSourceType.TNT
                )
                entity.discard()
                break
            } else {
                world.createExplosion(
                    null,
                    entity.damageSources.explosion(null, player),
                    StrongExplosionBehavior(player),
                    entity.x,
                    entity.y,
                    entity.z,
                    2.0f,
                    false,
                    World.ExplosionSourceType.TNT
                )
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
                break
            }
        }
        if (entities.isEmpty()) {
            world.createExplosion(
                null,
                player.damageSources.explosion(null, player),
                StrongExplosionBehavior(player),
                result.pos.x,
                result.pos.y,
                result.pos.z,
                2.0f,
                false,
                World.ExplosionSourceType.TNT
            )
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
            player.itemCooldownManager.set(player.getStackInHand(hand).item, 600)
        }
        return null
    }
}