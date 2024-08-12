package org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.entity.MortarEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.world.explosion.RancidExplosionBehavior
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class RancidBrewKosmogliph(id: Identifier) :
    SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_RANCID_BREW) }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
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
                )
            )
            if (!player.world.isClient) {
                val serverWorld = player.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.ENCHANT,
                    (lerp(player.eyePos.x, result.pos.x, i / interval)),
                    (lerp(player.eyePos.y - 0.5, result.pos.y, i / interval)),
                    (lerp(player.eyePos.z, result.pos.z, i / interval)),
                    10,
                    0.2,
                    0.2,
                    0.2,
                    0.0001
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
            if (entity is LivingEntity || entity is CannonballEntity || entity is MortarEntity) {
                entity.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(world.registryManager, DamageTypes.MAGIC),
                        player,
                        player
                    ), 5f
                )
                if (entity is CannonballEntity || entity is MortarEntity) {
                    world.createExplosion(
                        entity,
                        entity.damageSources.explosion(entity, player),
                        RancidExplosionBehavior(),
                        entity.x,
                        entity.y,
                        entity.z,
                        2.0f,
                        false,
                        World.ExplosionSourceType.TNT
                    )
                    entity.discard()
                }
                if (entity is LivingEntity) {
                    entity.addStatusEffect(
                        StatusEffectInstance(
                            StatusEffects.SLOWNESS,
                            300, 0,
                            false, true, true
                        )
                    )
                    entity.addStatusEffect(
                        StatusEffectInstance(
                            StatusEffects.POISON,
                            300, 0,
                            false, true, true
                        )
                    )
                    entity.addStatusEffect(
                        StatusEffectInstance(
                            StatusEffects.WEAKNESS,
                            300, 0,
                            false, true, true
                        )
                    )
                    entity.addStatusEffect(
                        StatusEffectInstance(
                            AstralEffects.REDUCE,
                            300, 0,
                            false, true, true
                        )
                    )
                    entity.addStatusEffect(
                        StatusEffectInstance(
                            AstralEffects.BLEED,
                            300, 0,
                            false, true, true
                        )
                    )
                }
            }
        }
        if (!player.isCreative) {
            player.itemCooldownManager.set(player.getStackInHand(hand).item, 400)
        }
        player.addStatusEffect(
            StatusEffectInstance(
                StatusEffects.SLOWNESS,
                100, 0,
                false, true, true
            )
        )
        player.addStatusEffect(
            StatusEffectInstance(
                AstralEffects.REDUCE,
                100, 1,
                false, true, true
            )
        )
        player.addStatusEffect(
            StatusEffectInstance(
                StatusEffects.HUNGER,
                100, 0,
                false, true, true
            )
        )
    }
}