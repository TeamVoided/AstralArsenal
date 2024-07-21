package org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
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
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.AstralGreathammerItem
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class RancidBrewKosmogliph (id: Identifier) :
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
            if (!player.world.isClient) {
                val serverWorld = player.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.ENCHANT,
                    (lerp(player.eyePos.x, result.pos.x, i / interval)),
                    (lerp(player.eyePos.y, result.pos.y, i / interval)),
                    (lerp(player.eyePos.z, result.pos.z, i / interval)),
                    100,
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
            entity.damage(entity.damageSources.magic(),5f)
            if(entity is LivingEntity){
            entity.addStatusEffect(
                StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    300, 0,
                    false, true, true
                ))
                entity.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.WITHER,
                        300, 0,
                        false, true, true
                    ))
                entity.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.POISON,
                        300, 0,
                        false, true, true
                    ))
                entity.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.HUNGER,
                        300, 0,
                        false, true, true
                    ))
                entity.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.WEAKNESS,
                        300, 0,
                        false, true, true
                    ))
                entity.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.DARKNESS,
                        300, 0,
                        false, true, true
                    ))
                entity.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.UNLUCK,
                        300, 0,
                        false, true, true
                    ))}
        }
        if (!player.isCreative) {
            player.itemCooldownManager.set(player.getStackInHand(hand).item, 400)
        }
        player.addStatusEffect(
            StatusEffectInstance(
                StatusEffects.SLOWNESS,
                100, 0,
                false, true, true
            ))
        player.addStatusEffect(
            StatusEffectInstance(
                StatusEffects.WITHER,
                100, 1,
                false, true, true
            ))
        player.addStatusEffect(
            StatusEffectInstance(
                StatusEffects.HUNGER,
                100, 0,
                false, true, true
            ))
    }
}