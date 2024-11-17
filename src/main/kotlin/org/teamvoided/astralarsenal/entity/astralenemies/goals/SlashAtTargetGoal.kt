package org.teamvoided.astralarsenal.entity.astralenemies.goals

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Box
import org.teamvoided.astralarsenal.entity.astralenemies.AstralSlasherEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage

class SlashAtTargetGoal(val entity: AstralSlasherEntity) : Goal() {
    override fun canStart(): Boolean {
        return (entity.target != null && entity.distanceTo(entity.target) <= 3 && entity.attackCooldown <= 0 && !entity.slashing)
    }

    override fun stop() {
        entity.hasBeenHit = false
        entity.attacks = 0
        entity.attackTicks = 5
        entity.attackingTicks = 20
        entity.attackCooldown = 1000
        entity.slashing = false
        super.stop()
    }

    override fun shouldContinue(): Boolean {
        return (entity.attacks > 2 || (!entity.enraged && entity.hasBeenHit) || entity.target == null)
    }

    override fun tick() {
        entity.slashing = true
        if(entity.attackingTicks > 0){
            if(entity.attackingTicks == 20 && !entity.enraged){
                entity.world.playSoundFromEntity(
                    null,
                    entity,
                    SoundEvents.BLOCK_TRIAL_SPAWNER_DETECT_PLAYER,
                    SoundCategory.PLAYERS,
                    1.0f,
                    2.0f
                )
            }
            entity.attackingTicks--
        }
        else if(entity.attackTicks > 0){
            println("Fuck")
            when(entity.attacks){
                2 ->{
                    if(entity.attackTicks == 10){
                        val boost = entity.rotationVector.multiply(1.0, 0.0, 1.0).normalize().multiply(1.0)
                        entity.setVelocity(entity.velocity.x + boost.x, 0.0, entity.velocity.z + boost.z)
                        entity.velocityModified = true
                    }
                    val result = entity.raycast(1.0, 1f, false)
                    val hit = mutableListOf<Entity>()
                    val shift = entity.rotationVector.normalize().multiply(2.0,1.0,1.5)
                    hit.addAll(
                        entity.world.getOtherEntities(
                            entity, Box(
                                result.pos.x + (0.5 * shift.x),
                                result.pos.y + (0.5 * shift.y),
                                result.pos.z + (0.5 * shift.z),
                                result.pos.x - (0.5 * shift.x),
                                result.pos.y - (0.5 * shift.y),
                                result.pos.z - (0.5 * shift.z)
                            )
                        ).filterIsInstance<LivingEntity>()
                    )
                    for(victim in hit){
                        victim.customDamage(DamageTypes.MOB_ATTACK, 10f,entity,entity)
                    }
                }
                else -> {
                    if(entity.attackTicks == 5){
                        val boost = entity.rotationVector.multiply(1.0, 0.0, 1.0).normalize().multiply(0.2)
                        entity.setVelocity(entity.velocity.x + boost.x, 0.0, entity.velocity.z + boost.z)
                        entity.velocityModified = true
                    }
                    val result = entity.raycast(1.0, 1f, false)
                    val hit = mutableListOf<Entity>()
                    val shift = entity.rotationVector.normalize().multiply(2.0,1.0,1.5)
                    hit.addAll(
                        entity.world.getOtherEntities(
                            entity, Box(
                                result.pos.x + 1.0,
                                result.pos.y + 1.0,
                                result.pos.z + 1.0,
                                result.pos.x - 1.0,
                                result.pos.y - 1.0,
                                result.pos.z - 1.0
                            )
                        ).filterIsInstance<LivingEntity>()
                    )
                    for(victim in hit){
                        victim.customDamage(DamageTypes.MOB_ATTACK, 15f, entity, entity)
                    }
                }
            }
            entity.attackTicks--
        }
        else if(entity.attackTicks <= 0){
            when (entity.attacks){
                0 -> {
                    entity.attackingTicks = 20
                    entity.attackTicks = 5
                }
                1 -> {
                    entity.attackingTicks = 30
                    entity.attackTicks = 10
                }
                else -> {}
            }
            entity.attacks++
        }
        super.tick()
    }
}