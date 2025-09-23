package org.teamvoided.astralarsenal.kosmogliph.shield

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.explosion.ExplosionBehavior
import org.joml.Math.lerp
import org.joml.Vector3f
import org.teamvoided.astralarsenal.data.tags.AstralEntityTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.BeamRenderEntity
import org.teamvoided.astralarsenal.entity.Projectiles.CannonballEntity
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.mixin.PersistentProjectileEntityAccessor
import org.teamvoided.astralarsenal.world.explosion.parryExplosions.ParryBustedExplosionBehavior
import org.teamvoided.astralarsenal.world.explosion.parryExplosions.ParryStrongExplosionBehavior
import org.teamvoided.astralarsenal.world.explosion.parryExplosions.ParryWeakExplosionBehavior
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt


class ParryKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_PARRY) }) {
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
                    .filter {
                        it is ProjectileEntity && !it.type.isIn(AstralEntityTags.PROTECTED_FROM_DEL)
                                && (it.owner != user || it.age > 10)
                                && (it !is ArrowEntity || !(it as PersistentProjectileEntityAccessor).getInGround())
                    }
            )
            if (user is PlayerEntity) {
                if (parried.isNotEmpty()) {
                    for (entity in parried) {
                        if (entity is ProjectileEntity && entity.owner != user) {
                            user.heal(2f)
                        }
                        //will have to be changed to use some tags
                        if (entity.type.isIn(AstralEntityTags.WEAK_PARRYABLES)) {
                            entity.discard()
                            blowTheFuckUp(
                                ParryWeakExplosionBehavior(entity),
                                ParryStrongExplosionBehavior(entity),
                                2f, user, world
                            )
                            break
                        } else if (entity.type.isIn(AstralEntityTags.STRONG_PARRYABLES)) {
                            entity.discard()
                            blowTheFuckUp(
                                ParryStrongExplosionBehavior(entity),
                                ParryBustedExplosionBehavior(entity),
                                2f, user, world
                            )
                            break
                        } else if (entity.type.isIn(AstralEntityTags.VERY_STRONG_PARRYABLES)) {
                            entity.discard()
                            blowTheFuckUp(
                                ParryBustedExplosionBehavior(entity),
                                ParryBustedExplosionBehavior(entity),
                                2f, user, world
                            )
                            break
                        } else {
                            entity.discard()
                            blowTheFuckUp(
                                ParryWeakExplosionBehavior(entity),
                                ParryStrongExplosionBehavior(entity),
                                1f, user, world
                            )
                            break
                        }
                    }
                    user.itemCooldownManager.set(stack.item, 10)
                    user.stopUsingItem()
                }
            }
        } else {
            if (user is PlayerEntity) {
                user.itemCooldownManager.set(stack.item, 100)
                user.stopUsingItem()
            }
        }
        super.usageTick(world, user, stack, remainingUseTicks)
    }

    fun blowTheFuckUp(
        explosionBehavior: ExplosionBehavior, strongExplosionBehavior: ExplosionBehavior,
        power: Float, player: PlayerEntity, world: World,
    ) {
        val result = player.raycast(100.0, 1f, false)
        val distance = sqrt(
            sqrt((player.eyePos.x - result.pos.x).pow(2) + (player.eyePos.z - result.pos.z).pow(2)).pow(2) + ((player.eyePos.y - 0.5) - result.pos.y).pow(
                2
            )
        )
        val entities = mutableListOf<Entity>()
        val interval = (distance.times(2))
        var finalPosition: Vec3d? = null
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
                finalPosition = Vec3d(
                    (lerp(player.eyePos.x, result.pos.x, i / interval)),
                    (lerp(player.eyePos.y - 0.5, result.pos.y, i / interval)),
                    (lerp(player.eyePos.z, result.pos.z, i / interval))
                )
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
            if (entity is CannonballEntity) {
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
            finalPosition = result.pos
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
        if (world is ServerWorld) {
            val beamRenderer = BeamRenderEntity(world, player.x, player.y + 1, player.z)
            beamRenderer.dataTracker.set(BeamRenderEntity.OuterColour, 0x00630000)
            beamRenderer.dataTracker.set(BeamRenderEntity.InterColour, 0x00d69600)
            beamRenderer.dataTracker.set(BeamRenderEntity.LiveTime, 6)
            beamRenderer.dataTracker.set(BeamRenderEntity.ShrinkTime, 5)
            beamRenderer.dataTracker.set(BeamRenderEntity.TargetPos, finalPosition!!.toVector3f())
            beamRenderer.dataTracker.set(BeamRenderEntity.OriginPos, Vector3f(player.x.toFloat(), (player.y + 1).toFloat(), player.z.toFloat()))
            beamRenderer.dataTracker.set(BeamRenderEntity.OuterThickness, 0.3f)
            beamRenderer.dataTracker.set(BeamRenderEntity.MaxOuterThickness, 0.3f)
            beamRenderer.dataTracker.set(BeamRenderEntity.InnerCubes, 4)
            beamRenderer.setPosition(player.x, player.y + 1, player.z)
            world.spawnEntity(beamRenderer)
        }
    }
}