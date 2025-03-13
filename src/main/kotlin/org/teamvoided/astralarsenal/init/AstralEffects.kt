package org.teamvoided.astralarsenal.init

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffect
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
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import org.joml.Math.*
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.effects.AstralStatusEffect
import org.teamvoided.astralarsenal.effects.BleedStatusEffect
import org.teamvoided.astralarsenal.effects.ParticleStatusEffect
import org.teamvoided.astralarsenal.entity.BeamRenderEntity
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
                -0.05, EntityAttributeModifier.Operation.ADD_VALUE
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

    private fun register(id: String, entry: StatusEffect): Holder<StatusEffect> =
        Registries.STATUS_EFFECT.registerHolder(id(id), entry)

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

    fun modifyDamage(entity: LivingEntity, damage: Float, source: DamageSource): Float {
        var output = damage
        val effects_two = entity.statusEffects.filter { reduce.contains(it.effectType) }
        if (effects_two.isNotEmpty()) {
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
                val entities = mutableListOf<Entity>()
                entities.addAll(
                    entity.world.getOtherEntities(
                        entity, Box(
                            entity.x + 10,
                            entity.y + 10,
                            entity.z + 10,
                            entity.x - 10,
                            entity.y - 10,
                            entity.z - 10
                        )
                    ).filter { it is LivingEntity && it != source.attacker && it != entity }
                )
                val targets = min(3 + (CONDUCTIVE_TARGETS_PER_LEVEL * levels), CONDUCTIVE_MAX_TARGETS)
                if (entities.isNotEmpty()) {
                    var count = 0
                    var tempDamage = conductiveDamage
                    for (entiity in entities) {
                        if (count >= targets) {
                            break
                        }
                        if (entiity is PlayerEntity && tempDamage >= 10) {
                            tempDamage = 10f
                        }
                        entiity.damage(
                            DamageSource(
                                AstralDamageTypes.getHolder(
                                    entity.world.registryManager,
                                    if (entiity is PlayerEntity) AstralDamageTypes.NON_RAILED else AstralDamageTypes.RICHOCHET
                                ),
                                source.attacker,
                                source.attacker,
                            ), tempDamage
                        )
                        if (entity.world is ServerWorld) {
                            sillyLightningTime(entity.pos, entiity.pos, ((entity.world as ServerWorld)), tempDamage)
                        }
                        count++
                    }
                    entity.world.playSound(
                        null,
                        entity.x,
                        entity.y,
                        entity.z,
                        SoundEvents.ITEM_TRIDENT_THUNDER.value(),
                        SoundCategory.PLAYERS,
                        1.0F,
                        1.6f
                    )
                }
            }
        }

        // Immortal Extra Check
        if (entity.hasStatusEffect(IMMORTAL) && !source.isTypeIn(BYPASSES_INVULNERABILITY) && source.attacker != entity)
            output = 0f

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
        return output
    }

    fun sillyLightningTime(pos1: Vec3d, pos2: Vec3d, world: ServerWorld, dmg: Float) {
        val size = clamp(0.1f, 0.2f, dmg / 50)
        val bends = world.random.rangeInclusive(3, 5)
        val bendPos = mutableListOf<Vec3d>()
        bendPos.add(pos1)
        for (i in 0..<bends) {
            val maxlerp: Double = (1.0 / bends) * i
            val xrand = Math.pow(-1.0, (i.toDouble().plus(world.random.rangeInclusive(0, 1)))).times(5)
            val yrand = Math.pow(-1.0, i.toDouble()).times(5)
            val zrand = Math.pow(-1.0, (i.toDouble().plus(world.random.rangeInclusive(0, 1)))).times(5)
            val ymin = ((pos1.y - pos2.y) / (bends)) * i
            bendPos.add(
                Vec3d(
                    (lerp(pos1.x, pos2.x, maxlerp) + world.random.nextDouble().minus(0.5).times(xrand)),
                    lerp(pos1.y, pos2.y, maxlerp) + ymin + world.random.nextDouble()
                        .minus(0.5).times(yrand),
                    lerp(pos1.z, pos2.z, maxlerp) + world.random.nextDouble().minus(0.5).times(zrand)
                )
            )
        }
        bendPos.add(pos2)
        var count = 0
        for (i in 0..<(bendPos.size - 1)) {
            if (count > bendPos.size) {
                break
            }
            count++
            val a = bendPos[i]
            val b = bendPos[i + 1]
            val distance = a.distanceTo(b)
            val beamRenderer = BeamRenderEntity(world, a.x, a.y + 1, a.z)
            beamRenderer.dataTracker.set(BeamRenderEntity.OuterColour, 0x00ababab)
            beamRenderer.dataTracker.set(BeamRenderEntity.InterColour, 0x00ababab)
            beamRenderer.dataTracker.set(BeamRenderEntity.LiveTime, 6)
            beamRenderer.dataTracker.set(BeamRenderEntity.ShrinkTime, 5)
            beamRenderer.dataTracker.set(BeamRenderEntity.TargetPos, Vec3d(b.x, b.y + 1, b.z).toVector3f())
            beamRenderer.dataTracker.set(BeamRenderEntity.OuterThickness, size)
            beamRenderer.dataTracker.set(BeamRenderEntity.MaxOuterThickness, size)
            beamRenderer.dataTracker.set(BeamRenderEntity.InnerCubes, 1)
            beamRenderer.setPosition(a.x, a.y + 1, a.z)
            world.spawnEntity(beamRenderer)
        }
    }

    fun cancelDamage(entity: LivingEntity, damage: Float, source: DamageSource): Boolean {
        if (entity.hasStatusEffect(IMMORTAL) && !source.isTypeIn(BYPASSES_INVULNERABILITY) && source.attacker != entity) {
            if (!(source.isTypeIn(DamageTypeTags.IS_FIRE) && entity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)))
                entity.world.playSoundFromEntity(
                    null, entity, SoundEvents.BLOCK_AMETHYST_BLOCK_FALL, SoundCategory.NEUTRAL,
                    1.0f, 0.8f
                )
            return true
        }
        return false
    }
}