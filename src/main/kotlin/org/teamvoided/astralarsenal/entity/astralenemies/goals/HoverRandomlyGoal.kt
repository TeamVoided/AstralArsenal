package org.teamvoided.astralarsenal.entity.astralenemies.goals

import net.minecraft.entity.ai.control.MoveControl
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.util.math.Vec3d
import org.joml.Math.clamp
import org.teamvoided.astralarsenal.entity.astralenemies.AstralFlyingEnemyEntity
import org.teamvoided.astralarsenal.entity.astralenemies.AstralSniperEntity
import org.teamvoided.astralarsenal.entity.astralenemies.AstralStrikerEntity

import java.util.*

class HoverRandomlyGoal(private val entity: AstralFlyingEnemyEntity, val maxDistance: Double, val minDistance: Double) : Goal() {
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
        val maxX = 3.0
        val minX = -3.0
        val maxY = 2.0
        val minY = -2.0
        val maxZ = 3.0
        val minZ = -3.0
        val d = clamp(minX, maxX, (randomGenerator.nextDouble() * 2f - 1f) * 3f).toDouble()
        val e = clamp(minY, maxY, (randomGenerator.nextDouble() * 2f - 1f) * 2f).toDouble()
        val f = clamp(minZ, maxZ, (randomGenerator.nextDouble() * 2f - 1f) * 3f).toDouble()
        var bool = false
        if(entity.target != null){
            val vec3d = Vec3d(d,e,f)
            val currentDistance = entity.squaredDistanceTo(entity.target!!)
            val predictedDistance = entity.target!!.squaredDistanceTo((vec3d.add(entity.pos)))
            if(predictedDistance > currentDistance && predictedDistance >= maxDistance) bool = true
            else if(currentDistance > predictedDistance && predictedDistance <= minDistance) bool = true
            if (entity is AstralSniperEntity && entity.isShooting) bool = true
        }
        if(!bool){
            entity.moveControl.moveTo(d, e, f, 0.3)
        }
    }
}