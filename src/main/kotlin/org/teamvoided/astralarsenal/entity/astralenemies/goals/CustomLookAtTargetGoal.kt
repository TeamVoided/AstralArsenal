package org.teamvoided.astralarsenal.entity.astralenemies.goals

import net.minecraft.entity.ai.goal.Goal
import net.minecraft.util.math.MathHelper
import org.teamvoided.astralarsenal.entity.astralenemies.AstralFlyingEnemyEntity
import java.util.*

class CustomLookAtTargetGoal(private val entity: AstralFlyingEnemyEntity) : Goal() {
    //The other one is private im pretty sure
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