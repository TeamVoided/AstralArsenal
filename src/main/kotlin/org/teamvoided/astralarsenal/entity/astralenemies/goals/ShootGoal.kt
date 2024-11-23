package org.teamvoided.astralarsenal.entity.astralenemies.goals

import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.util.math.MathHelper
import org.teamvoided.astralarsenal.entity.HomingBulletEntity
import org.teamvoided.astralarsenal.entity.SlowBulletEntity
import org.teamvoided.astralarsenal.entity.astralenemies.AstralDrifterEntity

class ShootGoal(val entity: AstralDrifterEntity) : Goal() {
    override fun canStart(): Boolean {
        return entity.target != null
    }

    override fun shouldContinue(): Boolean {
        return (entity.target != null && !entity.target!!.isAlive && entity.distanceTo(entity.target) <= 100)
    }

    override fun tick() {
        if (entity.cooldown > 0) {
            entity.cooldown--
        } else {
            if (entity.enraged){
                entity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entity.target!!.eyePos)
                shootHoming(entity)
            }
            else{
                entity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entity.target!!.eyePos)
                shootNormal(entity)
            }
            entity.cooldown = entity.getAttackCooldown(entity.world)
        }
        super.tick()
    }

    fun shootNormal(entity: AstralDrifterEntity) {
        val shots = entity.getFiredProjectiles(entity.world)
        val deviance = 5
        var rotation = 0
        var distance = 1
        //1 is left, 2 is right, 3 is down, 4 is up
        repeat(shots) {
            var p = 0
            var y = 0
            if (rotation == 1) {
                y = -1 * deviance * distance
                rotation = 2
            } else if (rotation == 2) {
                y = deviance * distance
                rotation = 3
            } else if (rotation == 3) {
                p = -1 * deviance * distance
                rotation = 4
            } else if (rotation == 4) {
                p = deviance * distance
                rotation = 1
                distance++
            } else {
                rotation = 1
            }
            val bullet = SlowBulletEntity(entity.world, entity)
            bullet.setDmg(8f)
            setPropertiesTwo(bullet, entity.pitch + p, entity.yaw + y, 0.0f, 0.5f, (0.1f * distance))
            entity.world.spawnEntity(bullet)
        }
    }

    fun shootHoming(entity: AstralDrifterEntity) {
        val shots = entity.getFiredProjectiles(entity.world)
        val deviance = 20
        var rotation = 0
        var distance = 1
        //1 is left, 2 is right, 3 is down, 4 is up
        repeat(shots) {
            var p = 0
            var y = 0
            if (rotation == 1) {
                y = -1 * deviance * distance
                rotation = 2
            } else if (rotation == 2) {
                y = deviance * distance
                rotation = 3
            } else if (rotation == 3) {
                p = -1 * deviance * distance
                rotation = 4
            } else if (rotation == 4) {
                p = deviance * distance
                rotation = 1
                distance++
            } else {
                rotation = 1
            }
            val bullet = HomingBulletEntity(entity.world, entity)
            bullet.setDmg(6f)
            bullet.target = entity.target!!
            setPropertiesTwo(bullet, entity.pitch + p, entity.yaw + y, 0.0f, 0.2f, (0.1f * distance))
            entity.world.spawnEntity(bullet)
        }
    }


    fun setPropertiesTwo(
        entity: ProjectileEntity, pitch: Float, yaw: Float, roll: Float, speed: Float, modifierXYZ: Float
    ) {
        val f = -MathHelper.sin(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180))
        val g = -MathHelper.sin((pitch + roll) * (Math.PI.toFloat() / 180))
        val h = MathHelper.cos(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180))
        entity.setVelocity(f.toDouble(), g.toDouble(), h.toDouble(), speed, modifierXYZ)
    }
}