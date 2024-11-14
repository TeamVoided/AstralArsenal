package org.teamvoided.astralarsenal.entity.astralenemies.goals

import net.minecraft.entity.ai.goal.Goal
import net.minecraft.util.math.MathHelper
import org.teamvoided.astralarsenal.entity.astralenemies.AstralSniperEntity
import java.util.*

class LookAtPointGoal(private val entity: AstralSniperEntity) : Goal() {
    init {
        this.controls = EnumSet.of(Control.LOOK)
    }

    override fun canStart(): Boolean {
        return true
    }

    override fun requiresUpdateEveryTick(): Boolean {
        return true
    }

    override fun shouldContinue(): Boolean {
        return true
    }

    override fun tick() {
        if (entity.target == null) {
            val vec3d = entity.velocity
            entity.yaw = -(MathHelper.atan2(vec3d.x, vec3d.z).toFloat()) * 57.295776f
            entity.bodyYaw = entity.yaw
        }
    }
}