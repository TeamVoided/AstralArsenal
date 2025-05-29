package org.teamvoided.astralarsenal.init

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.Holder
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.registry.tag.DamageTypeTags.BYPASSES_INVULNERABILITY
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Vec3d
import org.joml.Math.*
import org.joml.Vector3f
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.effects.AstralStatusEffect
import org.teamvoided.astralarsenal.effects.BleedStatusEffect
import org.teamvoided.astralarsenal.effects.MagneticStatusEffect
import org.teamvoided.astralarsenal.effects.ParticleStatusEffect
import org.teamvoided.astralarsenal.entity.BeamRenderEntity
import org.teamvoided.astralarsenal.entity.ConductiveEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.util.applyHexes
import org.teamvoided.astralarsenal.util.registerHolder
import kotlin.math.min

object AstralEffects {
    fun init() = Unit
    val SLAM_JUMP = register(
        "slam_jump", AstralStatusEffect(StatusEffectType.BENEFICIAL, 6684672)
            .addAttributeModifier(
                EntityAttributes.GENERIC_JUMP_STRENGTH, id("effect.jump"),
                0.1, EntityAttributeModifier.Operation.ADD_VALUE
            )
    )
    val UNHEALABLE_DAMAGE = register(
        "unhealable_damage", AstralStatusEffect(StatusEffectType.NEUTRAL, 6684672)
            .addAttributeModifier(
                EntityAttributes.GENERIC_MAX_HEALTH, id("effect.unhealable"),
                -0.5, EntityAttributeModifier.Operation.ADD_VALUE
            )
    )
    val HARD_DAMAGE = register(
        "hard_damage", AstralStatusEffect(StatusEffectType.NEUTRAL, 6684672)
            .addAttributeModifier(
                EntityAttributes.GENERIC_MAX_HEALTH, id("effect.weak_hard"),
                -0.1, EntityAttributeModifier.Operation.ADD_VALUE
            )
    )
    val OVERHEAL = register(
        "overheal", AstralStatusEffect(StatusEffectType.BENEFICIAL, 6684672)
            .addAttributeModifier(
                EntityAttributes.GENERIC_MAX_ABSORPTION, id("effect.overheal"),
                0.2, EntityAttributeModifier.Operation.ADD_VALUE
            )
    )
    val REDUCE = register(
        "reduce", AstralStatusEffect(StatusEffectType.HARMFUL, 0x660000)
    )
    val IMMORTAL = register(
        "immortal", AstralStatusEffect(StatusEffectType.BENEFICIAL, 0xffffff)
            .addAttributeModifier(
                EntityAttributes.GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE, id("effect.immortal"),
                1.0, EntityAttributeModifier.Operation.ADD_VALUE
            )
            .addAttributeModifier(
                EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, id("effect.immortal"),
                1.0, EntityAttributeModifier.Operation.ADD_VALUE
            )
    )
    val BLEED = register(
        "bleed", BleedStatusEffect(0x660000)
    )
    val CONDUCTIVE = register(
        "conductive", ParticleStatusEffect(StatusEffectType.HARMFUL, 0x00a2ff, ParticleTypes.ELECTRIC_SPARK)
    )
    val IMPALED = register(
        "impaled", ParticleStatusEffect(StatusEffectType.HARMFUL, 0x590000, ParticleTypes.CRIMSON_SPORE)
    )
    val STATICALLY_SLUDGED = register(
        "statically_sludged",
        ParticleStatusEffect(StatusEffectType.HARMFUL, 0xb8f4ff, ParticleTypes.ELECTRIC_SPARK)
    )

    val BREACHING = register(
        "breaching",
        ParticleStatusEffect(StatusEffectType.BENEFICIAL, 0x009698, AstralParticles.TOME_RUNE_POOF)
    )

    val BREACHED = register(
        "breached",
        AstralStatusEffect(StatusEffectType.HARMFUL, 0xdaa520, true)
    )
    val DIMINISHING = register(
        "diminishing",
        ParticleStatusEffect(StatusEffectType.BENEFICIAL, 0x009698, AstralParticles.TOME_RUNE_POOF)
    )
    val DIMINISHED = register(
        "diminished",
        AstralStatusEffect(StatusEffectType.HARMFUL, 0xae0c00, true)
    )
    val WEAKENING = register(
        "weakening",
        ParticleStatusEffect(StatusEffectType.BENEFICIAL, 0x009698, AstralParticles.TOME_RUNE_POOF)
    )
    val WEAKENED = register(
        "weakened",
        AstralStatusEffect(StatusEffectType.HARMFUL, 0x1d2951, true)
    )
    val BLAZING = register(
        "blazing",
        ParticleStatusEffect(StatusEffectType.BENEFICIAL, 0x009698, AstralParticles.TOME_RUNE_POOF)
    )
    val BLAZED = register(
        "blazed",
        AstralStatusEffect(StatusEffectType.HARMFUL, 0xff4f00, true)
    )
    val CLEANSING = register(
        "cleansing",
        ParticleStatusEffect(StatusEffectType.BENEFICIAL, 0x009698, AstralParticles.TOME_RUNE_POOF)
    )
    val CLEANSED = register(
        "cleansed",
        AstralStatusEffect(StatusEffectType.HARMFUL, 0xd3d3d3, true)
    )
    val IMPEDING = register(
        "impeding",
        ParticleStatusEffect(StatusEffectType.BENEFICIAL, 0x009698, AstralParticles.TOME_RUNE_POOF)
    )
    val IMPEDED = register(
        "impeded",
        AstralStatusEffect(
            StatusEffectType.HARMFUL,
            0x191970, true
        ).addAttributeModifier(
            EntityAttributes.GENERIC_MOVEMENT_SPEED,
            id("effect.impeded"), -0.0125, EntityAttributeModifier.Operation.ADD_VALUE
        )
    )
    val MAGNETISING = register(
        "magnetising",
        ParticleStatusEffect(StatusEffectType.BENEFICIAL, 0x009698, AstralParticles.TOME_RUNE_POOF)
    )
    val MAGNETISED = register(
        "magnetised",
        MagneticStatusEffect(StatusEffectType.HARMFUL, 0x757575, true)
    )

    private fun register(id: String, entry: StatusEffect): Holder<StatusEffect> =
        Registries.STATUS_EFFECT.registerHolder(id(id), entry)


}