package org.teamvoided.astralarsenal.entity.astralenemies

import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.control.MoveControl
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.goal.TargetGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.FlyingEntity
import net.minecraft.entity.mob.GhastEntity
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.joml.Math.clamp
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.entity.BeamOfLightEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage
import java.util.*
import java.util.function.Predicate

class AstralStrikerEntityBeta(entityType: EntityType<out FlyingEntity>?, world: World?) : FlyingEntity(
    entityType,
    world
), Monster {

    //this is a beta version of the future mob, when implementing properly to the game, with proper classes,
    //please rename this to a better name, thx <3
    //Yes this is all in 1 class, this is just because it's easier to make from other minecraft classes.
    //once it is made into other classes, please do seperate it out.
    var Enraged = false
    var cooldown = 0
    var strikesOnTarget = 0

    init {
        this.experiencePoints = 20
        this.moveControl = AstralStrikerBetaMoveControl(this)
    }

    fun modifyDamage(
        stack: ItemStack, entity: LivingEntity, damage: Float, source: DamageSource,
        equipmentSlot: EquipmentSlot, stage: DamageModificationStage
    ): Float {
        var outputDamage = damage
        if (stage != DamageModificationStage.POST_ARMOR) return damage
        if(source.isTypeIn(AstralDamageTypeTags.IS_PLASMA)){
            outputDamage *= 0
        }
        if(source.isTypeIn(AstralDamageTypeTags.IS_MELEE)){
            outputDamage *= 1.5f
        }
        return outputDamage
    }

    class AstralStrikerBetaMoveControl(private val ghast: AstralStrikerEntityBeta) : MoveControl(ghast) {
        private var collisionCheckCooldown = 0

        override fun tick() {
            if (this.state == State.MOVE_TO) {
                if (collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += ghast.random.nextInt(5) + 2
                    var vec3d = Vec3d(this.targetX - ghast.x, this.targetY - ghast.y, this.targetZ - ghast.z)
                    val d = vec3d.length()
                    vec3d = vec3d.normalize()
                    if (this.willCollide(vec3d, MathHelper.ceil(d))) {
                        ghast.velocity = ghast.velocity.add(vec3d.multiply(0.1))
                        ghast.velocityDirty
                    } else {
                        this.state = State.WAIT
                    }
                }
            }
        }

        private fun willCollide(direction: Vec3d, steps: Int): Boolean {
            var box = ghast.bounds

            for (i in 1 until steps) {
                box = box.offset(direction)
                if (!ghast.world.isSpaceEmpty(this.ghast, box)) {
                    return false
                }
            }

            return true
        }
    }

    override fun initGoals() {
        goalSelector.add(5, HoverRandomlyGoal(this))
        goalSelector.add(2, StrikeGoal(this))
        goalSelector.add(2, LookAtTargetGoal(this))
        targetSelector.add(
            1, TargetGoal(
                this, HostileEntity::
                class.java, 10, true, false, Predicate { ((it.pos.y - this.pos.y) <= 10) }
            )
        )
    }

    override fun tick() {
        if (this.target == null) {
            this.cooldown = 0
            this.strikesOnTarget = 0
            this.Enraged = false
        }
        if (this.Enraged && this.world is ServerWorld) {
            val serverWorld = this.world as ServerWorld
            serverWorld.spawnParticles(
                ParticleTypes.END_ROD,
                this.x,
                this.y,
                this.z,
                1,
                random.nextDouble().minus(0.5).times(2),
                random.nextDouble().minus(0.5).times(2),
                random.nextDouble().minus(0.5).times(2),
                0.0
            )
            if ((serverWorld.time % 5).toDouble() == 0.0)
                serverWorld.playSoundFromEntity(
                    null,
                    this,
                    SoundEvents.BLOCK_ANCIENT_DEBRIS_BREAK,
                    SoundCategory.HOSTILE,
                    1.0f,
                    0.3f
                )
        }
        super.tick()
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
            var maxX = 16f
            var minX = -16f
            var maxY = 16f
            var minY = -16f
            var maxZ = 16f
            var minZ = -16f
            if (entity.target != null) {
                val distanceX = (entity.x - entity.target!!.x).toFloat()
                if((distanceX < 10 && distanceX > 0) || distanceX < -30) minX = 0f
                if(distanceX > 30 || (distanceX > -10 && distanceX < 0)) maxX = 0f
                val distanceY = (entity.y - entity.target!!.y).toFloat()
                if((distanceY < 10 && distanceY > 0) || distanceY < -30) minY = 0f
                if(distanceY > 30 || (distanceY > -10 && distanceY < 0)) maxY = 0f
                val distanceZ = (entity.z - entity.target!!.z).toFloat()
                if((distanceZ < 10 && distanceZ > 0) || distanceZ < -30) minY = 0f
                if(distanceZ > 30 || (distanceZ > -10 && distanceZ < 0)) maxY = 0f
            }
            val d = entity.x + clamp(maxX, minX, (randomGenerator.nextFloat() * 2f - 1f) * 16f).toDouble()
            val e = entity.y + clamp(maxY, minY, (randomGenerator.nextFloat() * 2f - 1f) * 16f).toDouble()
            val f = entity.z + clamp(maxZ, minZ, (randomGenerator.nextFloat() * 2f - 1f) * 16f).toDouble()
            entity.moveControl.moveTo(d, e, f, 5.0)
        }
    }

    class StrikeGoal(val entity: AstralStrikerEntityBeta) : Goal() {

        override fun canStart(): Boolean {
            return (entity.target != null && entity.distanceTo(entity.target) <= 50)
        }

        override fun start() {
            super.start()
        }

        override fun stop() {
            entity.Enraged = false
            super.stop()
        }

        override fun shouldContinue(): Boolean {
            return (entity.target != null && !entity.target!!.isAlive && entity.distanceTo(entity.target) <= 50)
        }

        override fun tick() {
            if (entity.cooldown <= 0) {
                val random = entity.world.random.rangeInclusive(1, 3)
                entity.cooldown = 150
                when (random) {
                    1 -> singleStrike(entity, entity.strikesOnTarget)
                    2 -> trippleStrike(entity, entity.strikesOnTarget)
                    3 -> largeStrike(entity, entity.strikesOnTarget)
                }
                entity.strikesOnTarget++
                if (entity.strikesOnTarget >= 5) {
                    entity.Enraged = true
                }
            } else {
                entity.cooldown--
            }
            super.tick()
        }

        fun singleStrike(entity: AstralStrikerEntityBeta, strikes: Int) {
            if (entity.target != null) {
                val target = entity.target
                val beam = BeamOfLightEntity(entity.world, entity)
                beam.setPosition(target!!.pos)
                beam.WINDUP = 60
                beam.TIMEACTIVE = 90
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
                    beam.TIMEACTIVE = 130 - (i * 10)
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
                beam.TIMEACTIVE = 50
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

    companion object {
        fun createMobAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0)
                .add(EntityAttributes.GENERIC_ARMOR, 5.0)
        }
    }
}