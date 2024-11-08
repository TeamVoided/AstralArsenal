package org.teamvoided.astralarsenal.entity.astralenemies

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.goal.TargetGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.FlyingEntity
import net.minecraft.entity.mob.GhastEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.BeamOfLightEntity
import java.util.*
import java.util.function.Predicate

class AstralStrikerEntityBeta(entityType: EntityType<out FlyingEntity>?, world: World?) : FlyingEntity(
    entityType,
    world
) {
    //this is a beta version of the future mob, when implementing properly to the game, with proper classes,
    //please rename this to a better name, thx <3

    var Enraged: Boolean = false

    fun createAttributes(): DefaultAttributeContainer.Builder {
        return MobEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0)
            .add(EntityAttributes.GENERIC_ARMOR, 5.0)
    }

    override fun initGoals() {
        goalSelector.add(5, HoverRandomlyGoal(this))
        goalSelector.add(2, StrikeGoal(this))
        goalSelector.add(2, LookAtTargetGoal(this))
        targetSelector.add(
            1, TargetGoal(
                this, PlayerEntity::
                class.java, 10, true, false, Predicate { ((it.pos.y - this.pos.y) <= 10) }
            )
        )
    }

    class HoverRandomlyGoal(private val entity: AstralStrikerEntityBeta) : Goal() {
        init {
            this.controls = EnumSet.of(Control.MOVE)
        }

        override fun canStart(): Boolean {
            val moveControl = entity.moveControl
            if (!moveControl.isMoving) {
                return true
            } else {
                val d = moveControl.targetX - entity.x
                val e = moveControl.targetY - entity.y
                val f = moveControl.targetZ - entity.z
                val g = d * d + e * e + f * f
                return g < 1.0 || g > 3600.0
            }
        }

        override fun shouldContinue(): Boolean {
            return false
        }

        override fun start() {
            val randomGenerator = entity.random
            val d = entity.x + ((randomGenerator.nextFloat() * 2.0f - 1.0f) * 16.0f).toDouble()
            val e = entity.y + ((randomGenerator.nextFloat() * 2.0f - 1.0f) * 16.0f).toDouble()
            val f = entity.z + ((randomGenerator.nextFloat() * 2.0f - 1.0f) * 16.0f).toDouble()
            entity.moveControl.moveTo(d, e, f, 1.0)
        }
    }

    class StrikeGoal(entity: AstralStrikerEntityBeta) : Goal() {
        private var entity: AstralStrikerEntityBeta? = null
        var StrikesSoFar = 0
        var CooldownTicks = 0
        fun StrikeGoal(entity: AstralStrikerEntityBeta?) {
            this.entity = entity
        }

        override fun canStart(): Boolean {
            return (this.entity != null && this.entity?.target != null)
        }

        override fun start() {
            StrikesSoFar = 0
            CooldownTicks = 0
            super.start()
        }

        override fun shouldContinue(): Boolean {
            return ((entity?.target != null && !entity?.target!!.isAlive) || (entity != null && (entity!!.distanceTo(
                entity!!.target
            ) >= 50)))
        }

        override fun tick() {
            if (entity != null && CooldownTicks <= 0) {
                val random = entity!!.world.random.rangeInclusive(1, 3)
                CooldownTicks = 150
                when (random) {
                    1 -> singleStrike(entity!!, StrikesSoFar)
                    2 -> trippleStrike(entity!!, StrikesSoFar)
                    3 -> largeStrike(entity!!, StrikesSoFar)
                }
                StrikesSoFar++
                if(StrikesSoFar >= 5){
                    entity!!.Enraged = true
                }
            } else {
                CooldownTicks--
            }
            super.tick()
        }

        fun singleStrike(entity: AstralStrikerEntityBeta, strikes: Int) {
            if (entity.target != null) {
                val target = entity.target
                val beam = BeamOfLightEntity(entity.world, entity)
                beam.setPosition(target!!.pos)
                beam.WINDUP = 60
                beam.TIMEACTIVE = 20
                beam.side = 2
                beam.THRUST = 1.0
                beam.targetEntity = target
                beam.DOT = false
                beam.DMG = 5
                beam.trackTime = 50
                beam.hard_damage = 0
                beam.enraged = (strikes >= 5)
                entity.world.spawnEntity(beam)
            }
        }

        fun trippleStrike(entity: AstralStrikerEntityBeta, strikes: Int) {
            if (entity.target != null) {
                for (i in 1..3) {
                    val target = entity.target
                    val beam = BeamOfLightEntity(entity.world, entity)
                    beam.setPosition(target!!.pos)
                    beam.WINDUP = (i * 10) + 20
                    beam.TIMEACTIVE = 5
                    beam.side = 1
                    beam.THRUST = 0.3
                    beam.targetEntity = target
                    beam.DOT = false
                    beam.DMG = 3
                    beam.trackTime = (i * 10) + 10
                    beam.hard_damage = 0
                    beam.enraged = (strikes >= 5)
                    entity.world.spawnEntity(beam)
                }
            }
        }

        fun largeStrike(entity: AstralStrikerEntityBeta, strikes: Int) {
            if (entity.target != null) {
                val target = entity.target
                val beam = BeamOfLightEntity(entity.world, entity)
                beam.setPosition(target!!.pos)
                beam.WINDUP = 100
                beam.TIMEACTIVE = 40
                beam.side = 5
                beam.THRUST = 2.0
                beam.targetEntity = target
                beam.DOT = false
                beam.DMG = 15
                beam.trackTime = 75
                beam.hard_damage = 0
                beam.enraged = (strikes >= 5)
                entity.world.spawnEntity(beam)
            }
        }
    }
    class LookAtTargetGoal(private val entity: AstralStrikerEntityBeta) : Goal() {
        init {
            this.controls = EnumSet.of(Control.LOOK)
        }

        override fun canStart(): Boolean {
            return true
        }

        override fun requiresUpdateEveryTick(): Boolean {
            return true
        }

        override fun tick() {
            if (entity.target == null) {
                val vec3d = entity.velocity
                entity.yaw = -(MathHelper.atan2(vec3d.x, vec3d.z).toFloat()) * 57.295776f
                entity.bodyYaw = entity.yaw
            } else {
                val livingEntity = entity.target
                val d = 64.0
                if (livingEntity!!.squaredDistanceTo(this.entity) < 4096.0) {
                    val e = livingEntity.x - entity.x
                    val f = livingEntity.z - entity.z
                    entity.yaw = -(MathHelper.atan2(e, f).toFloat()) * 57.295776f
                    entity.bodyYaw = entity.yaw
                }
            }
        }
    }
}