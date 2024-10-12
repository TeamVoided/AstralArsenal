package org.teamvoided.astralarsenal.init

import net.minecraft.util.math.*
import net.minecraft.entity.Entity
import net.minecraft.entity.ExperienceOrbEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.Holder
import net.minecraft.registry.Registries
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.coroutine.mcCoroutineTask
import org.teamvoided.astralarsenal.coroutine.ticks
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.effects.AstralStatusEffect
import org.teamvoided.astralarsenal.effects.BleedStatusEffect
import org.teamvoided.astralarsenal.effects.ParticleStatusEffect
import org.teamvoided.astralarsenal.util.registerHolder
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

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
    val BLEED = register(
        "bleed", BleedStatusEffect(0x660000)
    )
    val CONDUCTIVE = register(
        "conductive", ParticleStatusEffect(StatusEffectType.HARMFUL, 0x00a2ff, ParticleTypes.ELECTRIC_SPARK)
    )

    private fun register(id: String, entry: StatusEffect): Holder<StatusEffect> =
        Registries.STATUS_EFFECT.registerHolder(id(id), entry)

    val REDUCE_MULT = 0.05
    val reduce = listOf(
        REDUCE
    )
    val CONDUCTIVE_MULT = 0.01
    val CONDUCTIVE_MAX_TARGETS = 3
    val CONDUCTIVE_DAMAGE_SHARE = 0.1
    val conductive = listOf(
        CONDUCTIVE
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
            }
        }
        //conductive starts here
        val effects_conductive = entity.statusEffects.filter { conductive.contains(it.effectType) }
        if (effects_conductive.isNotEmpty() && source.isTypeIn(AstralDamageTypeTags.IS_PLASMA)) {
            var conductiveDamage = 0f
            effects_conductive.forEach { it ->
                val w = it.amplifier
                val levels = w + 1
                val mult = levels * CONDUCTIVE_MULT
                output = (output * (1 + mult)).toFloat()
                conductiveDamage = (output * (CONDUCTIVE_DAMAGE_SHARE * levels)).toFloat()
                entity.removeStatusEffect(CONDUCTIVE)
                if(levels > 5){
                    entity.addStatusEffect(
                        StatusEffectInstance(StatusEffectInstance(
                            CONDUCTIVE,
                            400, w - 5,
                            false, false, true
                ))
                    )
                }
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
                if (entities.isNotEmpty()) {
                    var count = 0
                    for (entiity in entities) {
                        if (count >= CONDUCTIVE_MAX_TARGETS) {
                            break
                        }
                        entiity.damage(
                            DamageSource(
                                AstralDamageTypes.getHolder(
                                    entity.world.registryManager,
                                    AstralDamageTypes.RICHOCHET
                                ),
                                source.attacker,
                                source.attacker,
                            ), conductiveDamage
                        )
                        if(entity.world is ServerWorld){
                        sillyLightningTime(entity.pos, entiity.pos, ((entity.world as ServerWorld)))}
                        entity.world.playSound(
                            null,
                            entiity.x,
                            entiity.y,
                            entiity.z,
                            SoundEvents.BLOCK_NETHERITE_BLOCK_PLACE,
                            SoundCategory.PLAYERS,
                            1.0F,
                            1.0f
                        )
                        count++
                    }
                }
            }
        }
        return output
    }

    fun sillyLightningTime(pos1: Vec3d, pos2: Vec3d, world: ServerWorld) {
        val bends = world.random.rangeInclusive(3, 5)
        val bendPos = mutableListOf<Vec3d>()
        bendPos.add(pos1)
        for (i in 0..<bends) {
            val distance = pos1.distanceTo(pos2)
            val maxlerp: Double = 1.0 / bends
            val xrand = (pos1.x - pos2.x) / (bends / 2)
            val yrand = (pos1.y - pos2.y) / (bends / 2)
            val zrand = (pos1.z - pos2.z) / (bends / 2)
            val xmin = ((pos1.x - pos2.x) / (bends)) * i
            val ymin = ((pos1.y - pos2.y) / (bends)) * i
            val zmin = ((pos1.z - pos2.z) / (bends)) * i
            bendPos.add(
                Vec3d(
                    lerp(pos1.x, pos2.x, maxlerp) + xmin + world.random.nextDouble()
                        .minus(0.5).times(xrand),
                    lerp(pos1.y, pos2.y, maxlerp) + ymin + world.random.nextDouble()
                        .minus(0.5).times(yrand),
                    lerp(pos1.z, pos2.z, maxlerp) + zmin + world.random.nextDouble()
                        .minus(0.5).times(zrand)
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
            println("from: " + bendPos[i])
            println("to: " + bendPos[i + 1])
            val b = bendPos[i + 1]
            val distance = a.distanceTo(b)
            val interval = (distance * 10)
            for (j in 0..interval.roundToInt()) {
                world.spawnParticles(
                    ParticleTypes.END_ROD,
                    (lerp(a.x, b.x, j / interval)),
                    (lerp(a.y + 1, b.y + 1, j / interval)),
                    (lerp(a.z, b.z, j / interval)),
                    1,
                    0.01,
                    0.01,
                    0.01,
                    0.0
                )
            }
        }
    }
}