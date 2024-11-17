package org.teamvoided.astralarsenal.entity.astralenemies

import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.TargetGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.entity.astralenemies.AstralSniperEntity.Companion
import org.teamvoided.astralarsenal.entity.astralenemies.AstralSniperEntity.SnipeType
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralEffects

class AstralIdol(
    entityType: EntityType<out AstralEnemyEntity>?,
    world: World?
) : AstralEnemyEntity(entityType, world), Monster {
    override fun initGoals() {
        targetSelector.add(
            1, TargetGoal(
                this, AstralFlyingEnemyEntity::
                class.java, 1, false, false
            ) { ((it.distanceTo(this)) <= 100) }
        )
        targetSelector.add(
            1, TargetGoal(
                this, AstralEnemyEntity::
                class.java, 1, false, false
            ) { ((it.distanceTo(this)) <= 100) }
        )
        super.initGoals()
    }

    override fun tick() {
        if (this.target != null && this.target is AstralIdol){
            this.target = null
        }
        if(this.target != null){
            this.target!!.addStatusEffect(StatusEffectInstance(AstralEffects.IMMORTAL, 2, 0, false, true))
            this.target!!.addStatusEffect(StatusEffectInstance(StatusEffects.GLOWING, 2, 0, false, true))
        }
        super.tick()
    }

    companion object {
        fun createMobAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 30.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
        }
    }
    override fun applyEnchantmentsToDamage(source: DamageSource, amount: Float): Float {
        var outputDamage = amount
        outputDamage *= if (source.isTypeIn(AstralDamageTypeTags.IS_MELEE)) 5000f
        else 0f
        return outputDamage
    }

    override fun isInvulnerableTo(source: DamageSource): Boolean {
        if (!source.isTypeIn(AstralDamageTypeTags.IS_MELEE)) return true
        return super.isInvulnerableTo(source)
    }
}