package org.teamvoided.astralarsenal.entity.astralenemies.goals

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.pathing.Path
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.util.Hand
import org.teamvoided.astralarsenal.entity.astralenemies.AstralSlasherEntity
import java.util.*
import kotlin.math.max

class CustomMeleeAttackGoal(
    protected val mob: PathAwareEntity,
    private val speed: Double,
    private val pauseWhenMobIdle: Boolean
) : Goal() {
    private var path: Path? = null
    private var targetX = 0.0
    private var targetY = 0.0
    private var targetZ = 0.0
    private var updateCountdownTicks = 0
    protected var cooldown: Int = 0
        private set
    private val attackIntervalTicks = 20
    private var lastUpdateTime: Long = 0

    init {
        this.controls =
            EnumSet.of(Control.MOVE, Control.LOOK)
    }

    override fun canStart(): Boolean {
        if(mob is AstralSlasherEntity && (mob as AstralSlasherEntity).slashing) return false
        val l = mob.world.time
        if (l - this.lastUpdateTime < 20L) {
            return false
        } else {
            this.lastUpdateTime = l
            val livingEntity = mob.target
            if (livingEntity == null) {
                return false
            } else if (!livingEntity.isAlive) {
                return false
            } else {
                this.path = mob.navigation.findPathTo(livingEntity, 0)
                return if (this.path != null) {
                    true
                } else {
                    mob.isIntersecting(livingEntity)
                }
            }
        }
    }

    override fun shouldContinue(): Boolean {
        if(mob is AstralSlasherEntity && (mob as AstralSlasherEntity).slashing) return false
        val livingEntity = mob.target
        return if (livingEntity == null) {
            false
        } else if (!livingEntity.isAlive) {
            false
        } else if (!this.pauseWhenMobIdle) {
            !mob.navigation.isIdle
        } else if (!mob.isInWalkTargetRange(livingEntity.blockPos)) {
            false
        } else {
            livingEntity !is PlayerEntity || !livingEntity.isSpectator() && !livingEntity.isCreative
        }
    }

    override fun start() {
        mob.navigation.startMovingAlong(this.path, this.speed)
        mob.isAttacking = true
        this.updateCountdownTicks = 0
        this.cooldown = 0
    }

    override fun stop() {
        val livingEntity = mob.target
        if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
            mob.target = null as LivingEntity?
        }

        mob.isAttacking = false
        mob.navigation.stop()
    }

    override fun requiresUpdateEveryTick(): Boolean {
        return true
    }

    override fun tick() {
        val livingEntity = mob.target
        if (livingEntity != null) {
            mob.lookControl.lookAt(livingEntity, 30.0f, 30.0f)
            this.updateCountdownTicks =
                max((this.updateCountdownTicks - 1).toDouble(), 0.0).toInt()
            if ((this.pauseWhenMobIdle || mob.visibilityCache.canSee(livingEntity)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || livingEntity.squaredDistanceTo(
                    this.targetX,
                    this.targetY,
                    this.targetZ
                ) >= 1.0 || mob.random.nextFloat() < 0.05f)
            ) {
                this.targetX = livingEntity.x
                this.targetY = livingEntity.y
                this.targetZ = livingEntity.z
                this.updateCountdownTicks = 4 + mob.random.nextInt(7)
                val d = mob.squaredDistanceTo(livingEntity)
                if (d > 1024.0) {
                    this.updateCountdownTicks += 10
                } else if (d > 256.0) {
                    this.updateCountdownTicks += 5
                }

                if (!mob.navigation.startMovingTo(livingEntity, this.speed)) {
                    this.updateCountdownTicks += 15
                }

                this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks)
            }

            this.cooldown = max((this.cooldown - 1).toDouble(), 0.0).toInt()
        }
    }
}

