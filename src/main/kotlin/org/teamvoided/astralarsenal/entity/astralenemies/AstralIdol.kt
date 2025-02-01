package org.teamvoided.astralarsenal.entity.astralenemies

import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.TargetGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.mob.ZombieEntity
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.init.AstralEffects
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class AstralIdol(
    entityType: EntityType<out ObeliskEnemyEntity>?,
    world: World?
) : ObeliskEnemyEntity(entityType, world), Monster {
    override fun initGoals() {
        targetSelector.add(
            1, TargetGoal(
                this, AstralFlyingEnemyEntity::
                class.java, 1, false, false
            ) { ((it.distanceTo(this)) <= 100) }
        )
        targetSelector.add(
            1, TargetGoal(
                this, AstralEnemyEntity::
                class.java, 1, false, false
            ) { ((it.distanceTo(this)) <= 100) }
        )
        targetSelector.add(
            1, TargetGoal(
                this, ZombieEntity::
                class.java, 1, false, false
            ) { ((it.distanceTo(this)) <= 100) }
        )
        super.initGoals()
    }

    override fun getDeathSound(): SoundEvent? {
        return SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value()
    }

    override fun tick() {
        if (this.target != null) {
            this.target!!.addStatusEffect(StatusEffectInstance(AstralEffects.IMMORTAL, 2, 0, false, true))
            this.target!!.addStatusEffect(StatusEffectInstance(StatusEffects.GLOWING, 2, 0, false, true))
            showTarget(this, ParticleTypes.END_ROD)
        }
        super.tick()
    }

    companion object {
        fun createMobAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 30.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 500.0)
                .add(EntityAttributes.GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE, 500.0)
        }
    }

    override fun applyEnchantmentsToDamage(source: DamageSource, amount: Float): Float {
        var outputDamage = amount
        outputDamage = if (source.isTypeIn(AstralDamageTypeTags.IS_MELEE)) {
            Float.POSITIVE_INFINITY
        } else 0f
        return outputDamage
    }

    override fun isInvulnerableTo(source: DamageSource): Boolean {
        if (!source.isTypeIn(AstralDamageTypeTags.IS_MELEE)) return true
        return super.isInvulnerableTo(source)
    }

    fun showTarget(entity: AstralIdol, particle: ParticleEffect) {
        entity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entity.target!!.pos)
        val distance = sqrt(
            sqrt((entity.eyePos.x - entity.target!!.x).pow(2) + (entity.eyePos.z - entity.target!!.z).pow(2)).pow(
                2
            ) + ((entity.eyePos.y - 0.5) - (entity.target!!.y.plus(entity.target!!.height.times(0.5)))).pow(
                2
            )
        )
        val interval = (distance.times(2))
        for (i in 0..interval.roundToInt()) {
            if (entity.world is ServerWorld) {
                val serverWorld = entity.world as ServerWorld
                serverWorld.spawnParticles(
                    particle,
                    (lerp(entity.eyePos.x, entity.target!!.x, i / interval)),
                    (lerp(entity.eyePos.y, entity.target!!.y.plus(entity.target!!.height.times(0.5)), i / interval)),
                    (lerp(entity.eyePos.z, entity.target!!.z, i / interval)),
                    1,
                    0.0,
                    0.0,
                    0.0,
                    0.0
                )
            }
        }
    }
}