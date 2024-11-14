package org.teamvoided.astralarsenal.entity.astralenemies.goals

import net.minecraft.entity.ai.FuzzyTargeting
import net.minecraft.entity.ai.goal.WanderAroundGoal
import net.minecraft.util.math.Vec3d
import org.teamvoided.astralarsenal.entity.astralenemies.AstralSlasherEntity

class CustomWanderGoal(mob: AstralSlasherEntity?, speed: Double, protected val probability: Float) :
    WanderAroundGoal(mob, speed) {
    constructor(pathAwareEntity: AstralSlasherEntity?, d: Double) : this(pathAwareEntity, d, 0.001f)

    override fun canStart(): Boolean {
        if(mob != null && mob is AstralSlasherEntity && (mob as AstralSlasherEntity).slashing) return false
        return super.canStart()
    }

    override fun shouldContinue(): Boolean {
        if(mob != null && mob is AstralSlasherEntity && (mob as AstralSlasherEntity).slashing) return false
        return super.shouldContinue()
    }

    override fun getWanderTarget(): Vec3d? {
        if (mob.isInsideWaterOrBubbleColumn) {
            val vec3d = FuzzyTargeting.find(this.mob, 15, 7)
            return vec3d ?: super.getWanderTarget()
        } else {
            return if (mob.random.nextFloat() >= this.probability) FuzzyTargeting.find(
                this.mob,
                10,
                7
            ) else super.getWanderTarget()
        }
    }

    companion object {
        const val CHANCE: Float = 0.001f
    }
}

