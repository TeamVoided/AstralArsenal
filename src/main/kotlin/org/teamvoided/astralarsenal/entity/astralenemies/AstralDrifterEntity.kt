package org.teamvoided.astralarsenal.entity.astralenemies

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.TargetGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Box
import net.minecraft.world.Difficulty
import net.minecraft.world.World
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.entity.astralenemies.goals.CustomLookAtTargetGoal
import org.teamvoided.astralarsenal.entity.astralenemies.goals.HoverRandomlyGoal
import org.teamvoided.astralarsenal.entity.astralenemies.goals.ShootGoal
import org.teamvoided.astralarsenal.entity.astralenemies.goals.StrikeGoal
import org.teamvoided.astralarsenal.entity.astralenemies.movecontrol.AstralFlyingEntityMoveControl

class AstralDrifterEntity (entityType: EntityType<out AstralDrifterEntity>,
                           world: World
) : AstralFlyingEnemyEntity(entityType, world), Monster {
    var enraged = false
    var cooldown = 0
    override fun cannotDespawn(): Boolean {
        return true
    }

    init {
        this.moveControl = AstralFlyingEntityMoveControl(this, 5.0)
    }

    fun getAttackCooldown(world: World): Int{
        return if(world.difficulty == Difficulty.HARD) 40 else if(world.difficulty == Difficulty.NORMAL) 60 else 100
    }
    fun getFiredProjectiles(world: World): Int{
        return if(world.difficulty == Difficulty.HARD) 5 else if(world.difficulty == Difficulty.NORMAL) 3 else 1
    }

    override fun applyEnchantmentsToDamage(source: DamageSource, amount: Float): Float {
        var outputDamage = amount
        if(source.isTypeIn(AstralDamageTypeTags.IS_PLASMA)) outputDamage *= 0f
        if(source.isTypeIn(AstralDamageTypeTags.IS_MELEE)) outputDamage *= 1.5f
        return outputDamage
    }

    override fun isInvulnerableTo(source: DamageSource): Boolean {
        if(source.isTypeIn(AstralDamageTypeTags.IS_PLASMA)) return true
        return super.isInvulnerableTo(source)
    }

    override fun initGoals() {
        goalSelector.add(5, HoverRandomlyGoal(this, 5.0, 0.0))
        goalSelector.add(2, ShootGoal(this))
        goalSelector.add(2, CustomLookAtTargetGoal(this))
        targetSelector.add(
            1, TargetGoal(
                this, PlayerEntity::
                class.java, 1, false, false
            ) { ((it.distanceTo(this)) <= 25) }
        )
    }

    override fun tick() {
        if (this.target == null) {
            this.cooldown = 0
            this.enraged = false
        }
        val entities = mutableListOf<Entity>()
        entities.addAll(
            world.getOtherEntities(
                this, Box(
                    this.x + 40,
                    this.y + 40,
                    this.z + 40,
                    this.x - 40,
                    this.y - 40,
                    this.z - 40
                )
            ).filter { it is AstralDrifterEntity && !it.isAlive}
        )
        if (entities.isNotEmpty()) this.enraged = true
        super.tick()
    }

    companion object {
        fun createMobAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 100.0)
                .add(EntityAttributes.GENERIC_ARMOR, 10.0)
        }
    }

}