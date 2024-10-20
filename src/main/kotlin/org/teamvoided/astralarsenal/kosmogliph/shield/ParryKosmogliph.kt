package org.teamvoided.astralarsenal.kosmogliph.shield

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.*
import net.minecraft.entity.projectile.thrown.PotionEntity
import net.minecraft.entity.projectile.thrown.SnowballEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.world.World
import net.minecraft.world.explosion.ExplosionBehavior
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.data.tags.AstralEntityTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.entity.FlameShotEntity
import org.teamvoided.astralarsenal.entity.FreezeShotEntity
import org.teamvoided.astralarsenal.entity.MortarEntity
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.mixin.PersistentProjectileEntityAccessor
import org.teamvoided.astralarsenal.world.explosion.parryExplosions.*
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt


class ParryKosmogliph(id: Identifier) :
    SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_PARRY) }) {
    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        if (user.itemUseTime < 6) {
            val result = user.raycast(1.0, 1f, false)
            val parried = mutableListOf<Entity>()
            parried.addAll(
                world.getOtherEntities(
                    user, Box(
                        result.pos.x + 0.5,
                        result.pos.y + 0.5,
                        result.pos.z + 0.5,
                        result.pos.x - 0.5,
                        result.pos.y - 0.5,
                        result.pos.z - 0.5
                    )
                )
            )
            if (user is PlayerEntity) {
                if (parried.isNotEmpty()) {
                    for (entity in parried) {
                        if (entity is ProjectileEntity) {
                            //will have to be changed to use some tags
                            if (entity is ArrowEntity) {
                                if (!(entity as PersistentProjectileEntityAccessor).getInGround()) {
                                    entity.discard()
                                    if (entity.isCritical) blowTheFuckUp(
                                        ParryStrongExplosionBehavior(entity),
                                        ParryBustedExplosionBehavior(entity),
                                        1f,
                                        user,
                                        world
                                    )
                                    else blowTheFuckUp(
                                        ParryWeakExplosionBehavior(entity),
                                        ParryStrongExplosionBehavior(entity),
                                        1f,
                                        user,
                                        world
                                    )
                                    break
                                }
                            } else if (entity is CannonballEntity || entity is MortarEntity || entity is FireworkRocketEntity) {
                                entity.discard()
                                blowTheFuckUp(
                                    ParryStrongExplosionBehavior(entity),
                                    ParryBustedExplosionBehavior(entity),
                                    2f,
                                    user,
                                    world
                                )
                                break
                            } else if (entity is SnowballEntity || entity is FreezeShotEntity) {
                                entity.discard()
                                blowTheFuckUp(
                                    ParryIceExplosionBehavior(entity),
                                    ParryIceExplosionBehavior(entity),
                                    1f,
                                    user,
                                    world
                                )
                                break
                            } else if (entity is FireballEntity || entity is SmallFireballEntity || entity is FlameShotEntity) {
                                entity.discard()
                                blowTheFuckUp(
                                    ParryFireExplosionBehavior(entity),
                                    ParryFireExplosionBehavior(entity),
                                    1f,
                                    user,
                                    world
                                )
                                break
                            } else if (entity is ShulkerBulletEntity || entity is PotionEntity || entity is WitherSkullEntity || entity is DragonFireballEntity) {
                                entity.discard()
                                blowTheFuckUp(
                                    ParryBrewExplosionBehavior(entity),
                                    ParryBrewExplosionBehavior(entity),
                                    1f,
                                    user,
                                    world
                                )
                                break
                            } else if (entity is LlamaSpitEntity) {
                                entity.discard()
                                blowTheFuckUp(
                                    ParryBustedExplosionBehavior(entity),
                                    ParryBustedExplosionBehavior(entity),
                                    3f,
                                    user,
                                    world
                                )
                                break
                            } else if (entity.type.isIn(AstralEntityTags.PROTECTED_FROM_DEL)) {//prevents certain projectiles being destroyed from parrying
                            } else {
                                entity.discard()
                                blowTheFuckUp(
                                    ParryWeakExplosionBehavior(entity),
                                    ParryStrongExplosionBehavior(entity),
                                    1f,
                                    user,
                                    world
                                )
                                break
                            }
                        }
                    }
                }
            }
        }
        super.usageTick(world, user, stack, remainingUseTicks)
    }

    fun blowTheFuckUp(
        explosionBehavior: ExplosionBehavior,
        strongExplosionBehavior: ExplosionBehavior,
        power: Float,
        player: PlayerEntity,
        world: World
    ) {
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
                    1,
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
            SoundEvents.ITEM_SHIELD_BREAK,
            SoundCategory.PLAYERS,
            1.0F,
            1.0f
        )
        world.playSound(
            null,
            player.x,
            player.y,
            player.z,
            SoundEvents.BLOCK_ANVIL_PLACE,
            SoundCategory.PLAYERS,
            1.0F,
            1.0f
        )
        for (entity in entities) {
            if (entity is CannonballEntity || entity is MortarEntity) {
                world.createExplosion(
                    entity,
                    entity.damageSources.explosion(entity, player),
                    strongExplosionBehavior,
                    entity.x,
                    entity.y,
                    entity.z,
                    power + 1.0f,
                    false,
                    World.ExplosionSourceType.TNT
                )
                entity.discard()
                break
            } else {
                world.createExplosion(
                    null,
                    entity.damageSources.explosion(null, player),
                    explosionBehavior,
                    entity.x,
                    entity.y,
                    entity.z,
                    power + 0.0f,
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
                        50,
                        1.0,
                        1.0,
                        1.0,
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
                explosionBehavior,
                result.pos.x,
                result.pos.y,
                result.pos.z,
                power + 0.0f,
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
                    50,
                    1.0,
                    1.0,
                    1.0,
                    0.0
                )
            }
        }
    }
}