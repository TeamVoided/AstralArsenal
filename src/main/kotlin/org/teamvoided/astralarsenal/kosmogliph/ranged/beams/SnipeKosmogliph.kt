package org.teamvoided.astralarsenal.kosmogliph.ranged.beams

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
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
import org.joml.Vector3f
import org.teamvoided.astralarsenal.components.SnipeDataV1
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.BeamRenderEntity
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.kosmogliph.KosmogliphWithData
import org.teamvoided.astralarsenal.world.explosion.WeakExplosionBehavior
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class SnipeKosmogliph(id: Identifier) :
    KosmogliphWithData(id, AstralDataComponents.SNIPE_DATA_V1, AstralItemTags.SUPPORTS_SNIPE) {
    val unhealable = listOf(
        AstralEffects.UNHEALABLE_DAMAGE
    )

    override fun onUse(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack>? {
        val stack = player.getStackInHand(hand)
        val data = stack.getOrDefault(AstralDataComponents.SNIPE_DATA_V1, SnipeDataV1.DEFAULT)
        var loaded = data.loaded
        var ticks = data.ticks
        if (!data.loaded) {
            shot(world, player)
            if (!player.isCreative) {
                selfDamage(world, player)
                loaded = true
                ticks = 40
                player.itemCooldownManager.set(stack.item, 10)
            }
        } else {
            shot(world, player)
            loaded = false
            ticks = 0
            if (!player.isCreative) {
                player.itemCooldownManager.set(stack.item, 300)
            }
        }
        stack.set(AstralDataComponents.SNIPE_DATA_V1, SnipeDataV1(ticks, loaded))
        return null
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val data = stack.getOrDefault(AstralDataComponents.SNIPE_DATA_V1, SnipeDataV1.DEFAULT)
        var ticks = data.ticks
        var loaded = data.loaded
        if (ticks > 0) ticks--
        else if (loaded) {
            ticks = 0
            loaded = false
            if (entity is PlayerEntity && !entity.isCreative) {
                entity.itemCooldownManager.set(stack.item, 300)
            }
        }
        stack.set(AstralDataComponents.SNIPE_DATA_V1, SnipeDataV1(ticks, loaded))
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    fun selfDamage(world: World, player: PlayerEntity) {
        if (!player.isCreative) {
            player.damage(
                DamageSource(
                    AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.DRAIN),
                    player,
                    player
                ), 5f
            )
        }
        var hard_levels = 10
        val effects = player.statusEffects.filter { unhealable.contains(it.effectType) }
        if (effects.isNotEmpty()) {
            effects.forEach {
                val w = it.amplifier
                hard_levels += w
            }
        }
        player.addStatusEffect(
            StatusEffectInstance(
                AstralEffects.UNHEALABLE_DAMAGE,
                400, hard_levels,
                false, true, true
            )
        )
    }

    fun shot(world: World, player: PlayerEntity) {
        val result = player.raycast(100.0, 1f, false)
        val distance = sqrt(
            sqrt((player.eyePos.x - result.pos.x).pow(2) + (player.eyePos.z - result.pos.z).pow(2)).pow(2) + ((player.eyePos.y - 0.5) - result.pos.y).pow(
                2
            )
        )
        if (world is ServerWorld) {
            val beamRenderer = BeamRenderEntity(world, player.x, player.y + 1, player.z)
            beamRenderer.dataTracker.set(BeamRenderEntity.OuterColour, 0x007df9ff.toInt())
            beamRenderer.dataTracker.set(BeamRenderEntity.InterColour, 0x00ababab.toInt())
            beamRenderer.dataTracker.set(BeamRenderEntity.LiveTime, 6)
            beamRenderer.dataTracker.set(BeamRenderEntity.ShrinkTime, 5)
            beamRenderer.dataTracker.set(BeamRenderEntity.TargetPos, result.pos.toVector3f())
            beamRenderer.dataTracker.set(BeamRenderEntity.OriginPos, Vector3f(player.x.toFloat(), (player.y + 1).toFloat(), player.z.toFloat()))
            beamRenderer.dataTracker.set(BeamRenderEntity.OuterThickness, 0.5f)
            beamRenderer.dataTracker.set(BeamRenderEntity.MaxOuterThickness, 0.5f)
            beamRenderer.dataTracker.set(BeamRenderEntity.InnerCubes, 4)
            beamRenderer.setPosition(player.x, player.y + 1, player.z)
            world.spawnEntity(beamRenderer)
        }
        val entities = mutableListOf<Entity>()
        val hitEntities = mutableListOf<Entity>()
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
                )
            )
            if (!player.world.isClient) {
                val serverWorld = player.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.END_ROD,
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
            AstralSounds.RAILGUN,
            SoundCategory.PLAYERS,
            1.0F,
            1.0f
        )
        for (entity in entities) {
            if (!hitEntities.contains(entity)) {
                if (entity is LivingEntity || entity is CannonballEntity) {
                    if (entity is CannonballEntity) {
                        world.createExplosion(
                            entity,
                            entity.damageSources.explosion(entity, player),
                            WeakExplosionBehavior(player),
                            entity.x,
                            entity.y,
                            entity.z,
                            2.0f,
                            false,
                            World.ExplosionSourceType.TNT
                        )
                        entity.discard()
                    } else if (entity is PlayerEntity) {
                        val rand = world.random.rangeInclusive(1, 10)
                        if (rand == 1) {
                            entity.damage(
                                DamageSource(
                                    AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.RAILED),
                                    player,
                                    player
                                ), 7.5f
                            )
                        } else {
                            entity.damage(
                                DamageSource(
                                    AstralDamageTypes.getHolder(
                                        world.registryManager,
                                        AstralDamageTypes.NON_RAILED
                                    ),
                                    player,
                                    player
                                ), 7.5f
                            )
                        }
                    } else if (entity is LivingEntity) {
                        entity.addStatusEffect(StatusEffectInstance(AstralEffects.CONDUCTIVE, 20, 19))
                        entity.damage(
                            DamageSource(
                                AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.RAILED),
                                player,
                                player
                            ), 15f
                        )
                    }
                }
                hitEntities.add(entity)
            }
        }
    }

}