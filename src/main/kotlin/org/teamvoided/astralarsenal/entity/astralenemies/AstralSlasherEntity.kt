package org.teamvoided.astralarsenal.entity.astralenemies

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.Difficulty
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.entity.astralenemies.goals.CustomMeleeAttackGoal
import org.teamvoided.astralarsenal.entity.astralenemies.goals.CustomWanderGoal
import org.teamvoided.astralarsenal.entity.astralenemies.goals.SlashAtTargetGoal
import org.teamvoided.astralarsenal.init.AstralDamageTypes

class AstralSlasherEntity(
    entityType: EntityType<out AstralEnemyEntity>?,
    world: World?
) : AstralEnemyEntity(entityType, world), Monster {

    var shielding = false
    var slashing = false
    var stunTicks = 0
    // attackingTicks is number of ticks between attacks, attackTicks are the number of ticks the attack is active
    // indicateTicks is the number of ticks that the entity shows it's going to attack before it attacks
    var attackingTicks = 20
    var attackTicks = 5
    var attacks = 0
    var hasBeenHit = false
    var enraged = false
    var enragedTicks = 0
    var stunnedTicks = 0
    var attackCooldown = 0


    override fun cannotDespawn(): Boolean {
        return true
    }

    fun getEnrageTicks(world: World) : Int{
        return if(world.difficulty == Difficulty.HARD) -5 else if(world.difficulty == Difficulty.NORMAL) 600 else 200
    }
    fun getStunTicks(world: World) : Int{
        return if(world.difficulty == Difficulty.HARD) 20 else if(world.difficulty == Difficulty.NORMAL) 40 else 60
    }
    companion object {
        val SNIPE_TYPE: TrackedData<Int> =
            DataTracker.registerData(AstralSniperEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        fun createMobAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 30.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
        }
    }

    override fun initGoals() {
        targetSelector.add(
            1, TargetGoal(
                this, PlayerEntity::
                class.java, 1, false, false
            ) { ((it.distanceTo(this)) <= 50) }
        )
        goalSelector.add(4, CustomMeleeAttackGoal(this, 1.0, false))
        goalSelector.add(5, CustomWanderGoal(this, 0.8))
        goalSelector.add(6, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.add(1, SlashAtTargetGoal(this))
        super.initGoals()
    }

    override fun tick() {
        if (this.target != null && !this.slashing && stunnedTicks <= 0) {
            this.shielding = true
        }
        else{
            shielding = false
        }
        if(enragedTicks > 0){
            enragedTicks--
        }
        else if(enragedTicks != -5){
            enraged = false
            enragedTicks = 0
        }
        if(stunnedTicks >0){
            stunnedTicks--
        }
        if(attackCooldown > 0) attackCooldown--
        super.tick()
    }
    override fun tryAttack(target: Entity?): Boolean {
        return true
    }

    override fun applyEnchantmentsToDamage(source: DamageSource, amount: Float): Float {
        var outputDamage = amount
        if (source.isTypeIn(AstralDamageTypeTags.IS_PLASMA)) outputDamage *= 0.1f
        if (source.isTypeIn(AstralDamageTypeTags.IS_MAGIC)) outputDamage *= 0.1f
        if(slashing && !enraged){
            hasBeenHit = true
            stunnedTicks = getStunTicks(this.world)
            if(source.isType(AstralDamageTypes.PARRY)){
                enraged = true
                enragedTicks = getEnrageTicks(this.world)
            }
        }
        return outputDamage
    }

    override fun isInvulnerableTo(source: DamageSource): Boolean {
        if (this.shielding
            && !source.isTypeIn(AstralDamageTypeTags.IS_PLASMA)
            && !source.isTypeIn(AstralDamageTypeTags.IS_MAGIC)) return true
        return super.isInvulnerableTo(source)
    }
}