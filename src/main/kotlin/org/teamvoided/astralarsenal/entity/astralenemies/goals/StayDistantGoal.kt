package org.teamvoided.astralarsenal.entity.astralenemies.goals

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ChargedProjectilesComponent
import net.minecraft.entity.CrossbowUser
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.RangedAttackMob
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.item.CrossbowItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.TimeHelper
import net.minecraft.util.math.int_provider.UniformIntProvider
import org.teamvoided.astralarsenal.entity.astralenemies.AstralEnemyEntity
import java.util.*

class StayDistantGoal<T: HostileEntity>(val entity: AstralEnemyEntity, val maxDistance: Int, val minDistance: Int) : Goal() {
    val COOLDOWN_RANGE: UniformIntProvider = TimeHelper.betweenSeconds(1, 2)
    private var actor: T? = null
    private var stage: Stage? = null
    private var speed = 0.0
    private var squaredRange = 0f
    private var seeingTargetTicker = 0
    private var chargedTicksLeft = 0
    private var cooldown = 0

    fun CrossbowAttackGoal(actor: T?, speed: Double, range: Float) {
        this.stage = Stage.UNCHARGED
        this.actor = actor
        this.speed = speed
        this.squaredRange = range * range
        this.controls =
            EnumSet.of(Control.MOVE, Control.LOOK)
    }

    override fun canStart(): Boolean {
        return this.hasAliveTarget() && this.isEntityHoldingCrossbow()
    }

    private fun isEntityHoldingCrossbow(): Boolean {
        return actor!!.isHolding(Items.CROSSBOW)
    }

    override fun shouldContinue(): Boolean {
        return this.hasAliveTarget() && (this.canStart() || !actor!!.getNavigation()
            .isIdle()) && this.isEntityHoldingCrossbow()
    }

    private fun hasAliveTarget(): Boolean {
        return actor!!.getTarget() != null && actor!!.getTarget()!!.isAlive()
    }

    override fun stop() {
        super.stop()
        actor!!.setAttacking(false)
        actor!!.setTarget(null as LivingEntity?)
        this.seeingTargetTicker = 0
        if (actor!!.isUsingItem()) {
            actor!!.clearActiveItem()
            (actor as CrossbowUser).setCharging(false)
            actor!!.getActiveItem().set<ChargedProjectilesComponent>(
                DataComponentTypes.CHARGED_PROJECTILES,
                ChargedProjectilesComponent.DEFAULT
            )
        }
    }

    override fun requiresUpdateEveryTick(): Boolean {
        return true
    }

    override fun tick() {
        val livingEntity: LivingEntity? = actor!!.getTarget()
        if (livingEntity != null) {
            val bl: Boolean = actor!!.getVisibilityCache().canSee(livingEntity)
            val bl2 = this.seeingTargetTicker > 0
            if (bl != bl2) {
                this.seeingTargetTicker = 0
            }

            if (bl) {
                ++this.seeingTargetTicker
            } else {
                --this.seeingTargetTicker
            }

            val d: Double = actor!!.squaredDistanceTo(livingEntity)
            val bl3 = (d > squaredRange.toDouble() || this.seeingTargetTicker < 5) && this.chargedTicksLeft == 0
            if (bl3) {
                --this.cooldown
                if (this.cooldown <= 0) {
                    actor!!.getNavigation()
                        .startMovingTo(livingEntity, if (this.isUncharged()) this.speed else this.speed * 0.5)
                    this.cooldown = COOLDOWN_RANGE[actor!!.getRandom()]
                }
            } else {
                this.cooldown = 0
                actor!!.getNavigation().stop()
            }

            actor!!.getLookControl().lookAt(livingEntity, 30.0f, 30.0f)
            if (this.stage == Stage.UNCHARGED) {
                if (!bl3) {
                    actor!!.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(this.actor, Items.CROSSBOW))
                    this.stage = Stage.CHARGING
                    (actor as CrossbowUser).setCharging(true)
                }
            } else if (this.stage == Stage.CHARGING) {
                if (!actor!!.isUsingItem()) {
                    this.stage = Stage.UNCHARGED
                }

                val i: Int = actor!!.getItemUseTime()
                val itemStack: ItemStack = actor!!.getActiveItem()
                if (i >= CrossbowItem.getLoadingTime(itemStack, this.actor)) {
                    actor!!.stopUsingItem()
                    this.stage = Stage.CHARGED
                    this.chargedTicksLeft = 20 + actor!!.getRandom().nextInt(20)
                    (actor as CrossbowUser).setCharging(false)
                }
            } else if (this.stage == Stage.CHARGED) {
                --this.chargedTicksLeft
                if (this.chargedTicksLeft == 0) {
                    this.stage = Stage.READY_TO_ATTACK
                }
            } else if (this.stage == Stage.READY_TO_ATTACK && bl) {
                (actor as RangedAttackMob).attack(livingEntity, 1.0f)
                this.stage = Stage.UNCHARGED
            }
        }
    }

    private fun isUncharged(): Boolean {
        return this.stage == Stage.UNCHARGED
    }

    enum class Stage {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK
    }
}