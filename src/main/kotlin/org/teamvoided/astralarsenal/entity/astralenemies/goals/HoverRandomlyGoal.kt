package org.teamvoided.astralarsenal.entity.astralenemies.goals

import net.minecraft.entity.ai.control.MoveControl
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.util.math.Vec3d
import org.joml.Math.clamp
import org.teamvoided.astralarsenal.entity.astralenemies.AstralFlyingEnemyEntity

import java.util.*

class HoverRandomlyGoal(private val entity: AstralFlyingEnemyEntity, val maxDistance: Double) : Goal() {
    init {
        this.controls = EnumSet.of(Control.MOVE)
    }

    override fun canStart(): Boolean {
        val moveControl: MoveControl = this.entity.getMoveControl()
        if (!moveControl.isMoving) {
            return true
        } else {
            val d: Double = moveControl.targetX - this.entity.getX()
            val e: Double = moveControl.targetY - this.entity.getY()
            val f: Double = moveControl.targetZ - this.entity.getZ()
            val g = d * d + e * e + f * f
            return (g < 1.0 || g > 3600.0)
        }
    }

    override fun shouldContinue(): Boolean {
        return false
    }

    override fun start() {
        val randomGenerator = entity.random
        var maxX = 3.0
        var minX = -3.0
        var maxY = 2.0
        var minY = -2.0
        var maxZ = 3.0
        var minZ = -3.0
        var d = clamp(minX, maxX, (randomGenerator.nextDouble() * 2f - 1f) * 3f).toDouble()
        var e = clamp(minY, maxY, (randomGenerator.nextDouble() * 2f - 1f) * 2f).toDouble()
        var f = clamp(minZ, maxZ, (randomGenerator.nextDouble() * 2f - 1f) * 3f).toDouble()
        var bool = false
        if(entity.target != null){
            val vec3d = Vec3d(d,e,f)
            val currentDistance = entity.squaredDistanceTo(entity.target!!)
            val predictedDistance = entity.target!!.squaredDistanceTo((vec3d.add(entity.pos)))
            if(predictedDistance > currentDistance && predictedDistance >= maxDistance) bool = true
        }
        if(bool){
            d *= 0.0f
            e *= 0.0f
            f *= 0.0f
        }
        entity.moveControl.moveTo(d, e, f, 0.3)
    }
}