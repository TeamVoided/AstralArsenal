package org.teamvoided.astralarsenal.util

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.registry.tag.DamageTypeTags.BYPASSES_INVULNERABILITY
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import org.joml.Math.max
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.entity.Arrows.AntiphaseArrow
import org.teamvoided.astralarsenal.entity.Arrows.AntiphaseSpectralArrow
import org.teamvoided.astralarsenal.entity.Arrows.MagneticArrow
import org.teamvoided.astralarsenal.entity.Arrows.MagneticSpectralArrow
import org.teamvoided.astralarsenal.entity.entitiesThatAreJustHereCauseOtherShitDontWork.ConductiveEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralEffects.BLAZED
import org.teamvoided.astralarsenal.init.AstralEffects.CONDUCTIVE
import org.teamvoided.astralarsenal.init.AstralEffects.HARD_DAMAGE
import org.teamvoided.astralarsenal.init.AstralEffects.IMMORTAL
import org.teamvoided.astralarsenal.init.AstralEffects.IMPALED
import org.teamvoided.astralarsenal.init.AstralEffects.REDUCE
import org.teamvoided.astralarsenal.init.AstralEffects.WEAKENED
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage
import kotlin.math.min

val REDUCE_MULT = 0.05
val reduce = listOf(
    REDUCE
)
val CONDUCTIVE_MULT = 0.05
val CONDUCTIVE_MAX_TARGETS = 10.0

// Note that if this is lower than 1 it will act as if it is 1, if it is negative then wtf are you doing?
val CONDUCTIVE_TARGETS_PER_LEVEL = 0.2
val CONDUCTIVE_DAMAGE_SHARE = 1.0
val CONDUCTIVE_DAMAGE_SHARE_HARD = 0.5
val CONDUCTIVE_DAMAGE_SHARE_SOFT = 2.0
val conductive = listOf(
    CONDUCTIVE
)
val impaled = listOf(
    IMPALED
)
val weakened = listOf(
    WEAKENED
)
val blazed = listOf(
    BLAZED
)

fun modifyDamage(entity: LivingEntity, damage: Float, source: DamageSource): Float {
    var output = damage
    val effects_two = entity.statusEffects.filter { reduce.contains(it.effectType) }
    if (effects_two.isNotEmpty() && !source.isTypeIn(AstralDamageTypeTags.IS_PLASMA) && !source.isTypeIn(
            AstralDamageTypeTags.IS_MAGIC
        )
    ) {
        effects_two.forEach {
            val w = it.amplifier
            val levels = w + 1
            val mult = levels * REDUCE_MULT
            output = (output * (1 + mult)).toFloat()
            if (entity is PlayerEntity && output > 15f) {
                output = max(damage, 15f)
            }
        }
    }
    //conductive starts here
    val effects_conductive = entity.statusEffects.filter { conductive.contains(it.effectType) }
    if (effects_conductive.isNotEmpty() && source.isTypeIn(AstralDamageTypeTags.IS_PLASMA)) {
        var conductiveDamage = 0f
        effects_conductive.forEach { it ->

            val w = it.amplifier
            val levels = w + 1
            if (entity !is PlayerEntity) {
                val mult = levels * CONDUCTIVE_MULT
                output = (output * (1 + mult)).toFloat()
            }
            val shareMult =
                if (damage > 10) CONDUCTIVE_DAMAGE_SHARE_HARD else if (damage < 5) CONDUCTIVE_DAMAGE_SHARE_SOFT else CONDUCTIVE_DAMAGE_SHARE
            conductiveDamage = (output * (shareMult)).toFloat()
            entity.removeStatusEffect(CONDUCTIVE)
            val targets = min(3 + (CONDUCTIVE_TARGETS_PER_LEVEL * levels), CONDUCTIVE_MAX_TARGETS)
            val conductivityEngine =
                ConductiveEntity(entity.world, entity.x, entity.y + (entity.height / 2), entity.z)
            conductivityEngine.setPosition(entity.x, entity.y + (entity.height / 2), entity.z)
            conductivityEngine.maxTargets = targets
            conductivityEngine.origin = entity
            conductivityEngine.owner = source.attacker
            conductivityEngine.dmg = conductiveDamage
            conductivityEngine.cooldown = 6
            entity.world.spawnEntity(conductivityEngine)
        }
    }
    if (source.attacker is LivingEntity) {
        val attacker = source.attacker as LivingEntity
        val weakened = attacker.statusEffects.filter { weakened.contains(it.effectType) }
        if (weakened.isNotEmpty()) {
            for (effect in weakened) {
                val amplifier = effect.amplifier + 1
                output *= max(1 - (0.1f * amplifier), 0f)
            }
        }
    }

    if (source.isType(DamageTypes.ON_FIRE)) {
        val blazed = entity.statusEffects.filter { blazed.contains(it.effectType) }
        for (effect in blazed) {
            output = 0f
            val dmg = 1f + (0.5f * (effect.amplifier + 1))
            entity.customDamage(AstralDamageTypes.INCINERATED, dmg)
        }
    }

    if (source.isType(DamageTypes.FREEZE)) {
        val blazed = entity.statusEffects.filter { blazed.contains(it.effectType) }
        for (effect in blazed) {
            output = 0f
            val dmg = 1f + (0.5f * (effect.amplifier + 1))
            entity.customDamage(AstralDamageTypes.FROZEN, dmg)
        }
    }

    //Impaled starts here
    val effects_impaled = entity.statusEffects.filter { impaled.contains(it.effectType) }
    if (effects_impaled.isNotEmpty() && source.isTypeIn(AstralDamageTypeTags.IS_MELEE)) {
        for (e in effects_impaled) {
            output += min((0.5f * (e.amplifier + 1)), 15f)
            entity.world.playSoundFromEntity(
                null, entity,
                SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.PLAYERS,
                1.0F, 1.6f
            )
            entity.removeStatusEffect(e.effectType)
        }
    }

    //arrow against mob buff to compensate for power nerf
    if (source.isType(DamageTypes.ARROW) && entity !is PlayerEntity) {
        output *= 1.5f
    }
    if (source.source is AntiphaseArrow || source.source is AntiphaseSpectralArrow) {
        output *= 0.9f
    } else if (source.source is MagneticArrow || source.source is MagneticSpectralArrow) {
        output *= 0.75f
    }

    return output
}

fun effectCancelDamage(entity: LivingEntity, damage: Float, source: DamageSource): Boolean {
    if (entity.hasStatusEffect(IMMORTAL) && !source.isTypeIn(BYPASSES_INVULNERABILITY)) {
        if (!source.isType(AstralDamageTypes.DEMOLISHED) || source.source != entity) {
            if (!(source.isTypeIn(DamageTypeTags.IS_FIRE) && entity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)))
                entity.world.playSoundFromEntity(
                    null, entity, SoundEvents.BLOCK_AMETHYST_BLOCK_FALL, SoundCategory.NEUTRAL,
                    1.0f, 0.8f
                )
            return true
        }
    }
    return false
}

fun cancelTomeOnHit(player: LivingEntity, source: DamageSource) {
    if (source.attacker is LivingEntity && player.isUsingItem && player.activeItem.item == AstralItems.TOME_OF_HEXES && player is PlayerEntity) {
        player.stopUsingItem()
    }
}