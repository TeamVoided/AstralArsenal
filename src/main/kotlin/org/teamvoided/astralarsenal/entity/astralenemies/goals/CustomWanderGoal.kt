package org.teamvoided.astralarsenal.entity.astralenemies.goals

import net.minecraft.entity.ai.NoPenaltyTargeting
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.util.math.Vec3d
import java.util.*

class CustomWanderGoal : Goal() {
    val DEFAULT_CHANCE: Int = 120
    var mob: PathAwareEntity? = null
    var targetX: Double = 0.0
    var targetY: Double = 0.0
    var targetZ: Double = 0.0
    var speed: Double = 0.0
    var chance: Int = 0
    var ignoringChance: Boolean = false
    var canDespawn = false

    fun CustomWanderGoal(mob: PathAwareEntity?, speed: Double) {
        CustomWanderGoal(mob, speed, 120)
    }

    fun CustomWanderGoal(mob: PathAwareEntity?, speed: Double, chance: Int) {
        CustomWanderGoal(mob, speed, chance, true)
    }

    fun CustomWanderGoal(entity: PathAwareEntity?, speed: Double, chance: Int, canDespawn: Boolean) {
        this.mob = entity
        this.speed = speed
        this.chance = chance
        this.canDespawn = canDespawn
        this.controls = EnumSet.of<Control>(Control.MOVE)
    }

    override fun canStart(): Boolean {
        if (mob!!.hasControllingPassenger()) {
            return false
        } else {
            if (!this.ignoringChance) {
                if (this.canDespawn && mob!!.despawnCounter >= 100) {
                    return false
                }

                if (mob!!.random.nextInt(toGoalTicks(this.chance)) != 0) {
                    return false
                }
            }

            val vec3d = this.getWanderTarget()
            if (vec3d == null) {
                return false
            } else {
                this.targetX = vec3d.x
                this.targetY = vec3d.y
                this.targetZ = vec3d.z
                this.ignoringChance = false
                return true
            }
        }
    }

    protected open fun getWanderTarget(): Vec3d? {
        return NoPenaltyTargeting.find(this.mob, 10, 7)
    }

    override fun shouldContinue(): Boolean {
        return !mob!!.navigation.isIdle && !mob!!.hasControllingPassenger()
    }

    override fun start() {
        mob!!.navigation.startMovingTo(this.targetX, this.targetY, this.targetZ, this.speed)
    }

    override fun stop() {
        mob!!.navigation.stop()
        super.stop()
    }

    fun ignoreChanceOnce() {
        this.ignoringChance = true
    }

    fun setChance(chance: Int) {
        this.chance = chance
    }
}