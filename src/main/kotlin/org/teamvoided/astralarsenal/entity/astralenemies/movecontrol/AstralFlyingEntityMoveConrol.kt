package org.teamvoided.astralarsenal.entity.astralenemies.movecontrol

import net.minecraft.entity.ai.control.MoveControl
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.teamvoided.astralarsenal.entity.astralenemies.AstralFlyingEnemyEntity

class AstralFlyingEntityMoveControl (private val astralEntity: AstralFlyingEnemyEntity, val maxDistance: Double) : MoveControl(astralEntity) {
    private var collisionCheckCooldown = 0

    override fun tick() {
        if (this.state == State.MOVE_TO) {
            if (collisionCheckCooldown-- <= 0) {
                this.collisionCheckCooldown += astralEntity.random.nextInt(5) + 2
                val vec3d = Vec3d(this.targetX, this.targetY, this.targetZ)
                val d = vec3d.length()
//                var bool = false
//                if(astralEntity.target != null){
//                    val currentDistance = astralEntity.squaredDistanceTo(astralEntity.target!!)
//                    val predictedDistance = astralEntity.target!!.squaredDistanceTo(vec3d)
//                    if(predictedDistance > currentDistance && predictedDistance >= maxDistance) bool = true
//                }
                if (this.willCollide(vec3d, MathHelper.ceil(d))) {
                    this.astralEntity.setVelocity(this.astralEntity.getVelocity().add(vec3d.multiply(0.02)))
                } else {
                    this.state = State.WAIT
                }
            }
        }
    }

    private fun willCollide(direction: Vec3d, steps: Int): Boolean {
        var box = astralEntity.bounds

        for (i in 1 until steps) {
            box = box.offset(direction)
            if (!astralEntity.world.isSpaceEmpty(this.astralEntity, box)) {
                return false
            }
        }

        return true
    }
}