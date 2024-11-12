package org.teamvoided.astralarsenal.entity.astralenemies

import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.TargetGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.FlyingEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.world.Difficulty
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.entity.astralenemies.goals.CustomLookAtTargetGoal
import org.teamvoided.astralarsenal.entity.astralenemies.goals.HoverRandomlyGoal
import org.teamvoided.astralarsenal.entity.astralenemies.goals.StrikeGoal
import org.teamvoided.astralarsenal.entity.astralenemies.movecontrol.AstralFlyingEntityMoveControl

class AstralStrikerEntity(entityType: EntityType<out FlyingEntity>?,
                          world: World?
) : AstralFlyingEnemyEntity(entityType, world), Monster {
    var enraged = false
    var cooldown = 0
    var strikesOnTarget = 0

    init {
        this.moveControl = AstralFlyingEntityMoveControl(this, 10.0)
    }

    fun getStrikesBeforeEnrage(world: World): Int{
        return if(world.difficulty == Difficulty.HARD) 1 else if(world.difficulty == Difficulty.NORMAL) 3 else 8
    }
    fun getTimeStrikeLasts(world: World): Int{
        return if(world.difficulty == Difficulty.HARD) 300 else if(world.difficulty == Difficulty.NORMAL) 80 else 20
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
        goalSelector.add(5, HoverRandomlyGoal(this, 10.0))
        goalSelector.add(2, StrikeGoal(this))
        goalSelector.add(2, CustomLookAtTargetGoal(this))
        targetSelector.add(
            1, TargetGoal(
                this, PlayerEntity::
                class.java, 1, false, false
            ) { ((it.distanceTo(this)) <= 100) }
        )
    }

    override fun tick() {
        if (this.target == null) {
            this.cooldown = 0
            this.strikesOnTarget = 0
            this.enraged = false
        }
        if (this.enraged && this.world is ServerWorld) {
            val serverWorld = this.world as ServerWorld
            serverWorld.spawnParticles(
                ParticleTypes.END_ROD,
                this.x,
                this.y,
                this.z,
                1,
                random.nextDouble().minus(0.5).times(2),
                random.nextDouble().minus(0.5).times(2),
                random.nextDouble().minus(0.5).times(2),
                0.0
            )
            if ((serverWorld.time % 5).toDouble() == 0.0)
                serverWorld.playSoundFromEntity(
                    null,
                    this,
                    SoundEvents.BLOCK_BASALT_PLACE,
                    SoundCategory.HOSTILE,
                    1.0f,
                    0.3f
                )
        }
        super.tick()
    }

    companion object {
        fun createMobAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 100.0)
                .add(EntityAttributes.GENERIC_ARMOR, 5.0)
        }
    }

}