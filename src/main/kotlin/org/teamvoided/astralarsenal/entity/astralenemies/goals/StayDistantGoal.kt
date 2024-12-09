package org.teamvoided.astralarsenal.entity.astralenemies.goals

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.RangedAttackMob
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.item.BowItem
import net.minecraft.item.Items
import org.teamvoided.astralarsenal.entity.astralenemies.AstralEnemyEntity
import java.util.*

class StayDistantGoal(val entity: AstralEnemyEntity, val maxDistance: Int, val minDistance: Int) : Goal() {
    private var speed = 0.0
    private var attackInterval = 0
    private var squaredRange = 0f
    private var cooldown = -1
    private var targetSeeingTicker = 0
    private var movingToLeft = false
    private var backward = false
    private var combatTicks = -1

    override fun canStart(): Boolean {
        return entity.target != null
    }

    override fun shouldContinue(): Boolean {
        return (this.canStart())
    }

    override fun start() {
        entity.usingSpecialMovement = true
        super.start()
    }

    override fun stop() {
        super.stop()
        this.targetSeeingTicker = 0
        this.cooldown = -1
        entity.usingSpecialMovement = false
    }

    override fun requiresUpdateEveryTick(): Boolean {
        return true
    }

    override fun tick() {
        val livingEntity: LivingEntity? = entity.target
        if (livingEntity != null) {
            val d: Double = entity.squaredDistanceTo(livingEntity.x, livingEntity.y, livingEntity.z)
            val bl: Boolean = entity.visibilityCache.canSee(livingEntity)
            val bl2 = this.targetSeeingTicker > 0
            if (bl != bl2) {
                this.targetSeeingTicker = 0
            }

            if (bl) {
                ++this.targetSeeingTicker
            } else {
                --this.targetSeeingTicker
            }

            if (!(d > squaredRange.toDouble()) && this.targetSeeingTicker >= 20) {
                entity.navigation.stop()
                ++this.combatTicks
            } else {
                entity.navigation.startMovingTo(livingEntity, this.speed)
                this.combatTicks = -1
            }

            if (this.combatTicks >= 20) {
                if (entity.random.nextFloat().toDouble() < 0.3) {
                    this.movingToLeft = !this.movingToLeft
                }

                if (entity.random.nextFloat().toDouble() < 0.3) {
                    this.backward = !this.backward
                }

                this.combatTicks = 0
            }

            if (this.combatTicks > -1) {
                if (d > (maxDistance * maxDistance).toDouble()) {
                    this.backward = false
                } else if (d < (minDistance * minDistance).toDouble()) {
                    this.backward = true
                }

                entity.moveControl.strafeTo(if (this.backward) -0.5f else 0.5f, if (this.movingToLeft) 0.5f else -0.5f)
            }
        }
    }
}