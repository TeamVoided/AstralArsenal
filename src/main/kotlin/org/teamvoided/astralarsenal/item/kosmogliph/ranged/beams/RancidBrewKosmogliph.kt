package org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.Holder
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.RailgunItem

class RancidBrewKosmogliph(id: Identifier) : AbstractRailgunKosmogliph(id, { it.item is RailgunItem }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        val raycast = raycast(world, player, 100.0, ParticleTypes.ENCHANT, 3, Vec3d(0.2, 0.2, 0.2), 0.0)

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

        raycast.first.damageAll(
            DamageSource(
                AstralDamageTypes.getHolder(world.registryManager, DamageTypes.MAGIC),
                player,
                player
            ), 5f
        )
        raycast.first.forEach { entity ->
            if (entity !is LivingEntity) return@forEach

            entity.addStatusEffects(
                300, 1, false, true, true, player,
                StatusEffects.SLOWNESS, StatusEffects.WITHER, StatusEffects.POISON,
                StatusEffects.HUNGER, StatusEffects.WEAKNESS, StatusEffects.DARKNESS,
                StatusEffects.UNLUCK
            )
        }

        player.addStatusEffect(
            StatusEffectInstance(
                StatusEffects.WITHER,
                100, 1,
                false, true, true
            ), player
        )

        if (!player.isCreative) {
            player.itemCooldownManager.set(player.getStackInHand(hand).item, 400)
        }
    }

    fun LivingEntity.addStatusEffects(
        duration: Int,
        amplifier: Int,
        ambient: Boolean,
        showParticle: Boolean,
        showIcon: Boolean,
        source: Entity,
        vararg effects: Holder<StatusEffect>
    ) {
        effects.forEach { effect ->
            addStatusEffect(StatusEffectInstance(effect, duration, amplifier, ambient, showParticle, showIcon), source)
        }
    }
}