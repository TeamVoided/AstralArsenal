package org.teamvoided.astralarsenal.entity.astralenemies

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.TargetGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.entity.astralenemies.goals.CustomLookAtTargetGoal
import org.teamvoided.astralarsenal.entity.astralenemies.goals.StrikeGoal

class AstralStrikerEntity(entityType: EntityType<out AstralStrikerEntity>,
                          world: World
) : AstralFlyingEnemyEntity(entityType, world), Monster {
    var enraged = false
    var cooldown = 0
    var strikesOnTarget = 0
    var passedTarget: LivingEntity? = null
    var owner: LivingEntity? = null

    override fun cannotDespawn(): Boolean {
        return true
    }

    fun getStrikesBeforeEnrage(world: World): Int{
        return 5
    }
    fun getTimeStrikeLasts(world: World): Int{
        return 40
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
        goalSelector.add(2, StrikeGoal(this))
        goalSelector.add(2, CustomLookAtTargetGoal(this))
        targetSelector.add(
            1, TargetGoal(
                this, PlayerEntity::
                class.java, 1, false, false
            ) { ((it.distanceTo(this)) <= 100) && (owner == null || it != owner)}
        )
        targetSelector.add(
            1, TargetGoal(
                this, PlayerEntity::
                class.java, 1, false, false
            ) { ((it.distanceTo(this)) <= 100) }
        )
    }

    override fun tick() {
        if(this.passedTarget != null && this.passedTarget!!.distanceTo(this) < 100){
            this.target = passedTarget
        }
        if (this.target == null) {
            this.cooldown = 0
            this.strikesOnTarget = 0
            this.enraged = false
        }
        super.tick()
    }

    companion object {
        fun createMobAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 100.0)
                .add(EntityAttributes.GENERIC_ARMOR, 5.0)
        }
    }

}