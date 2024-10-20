package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralEntities
import kotlin.math.roundToInt

class DeepWoundEntity : ThrownItemEntity {

    constructor(entityType: EntityType<out DeepWoundEntity?>?, world: World?) :
            super(entityType as EntityType<out ThrownItemEntity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.DEEP_WOUND_ENTITY as EntityType<out ThrownItemEntity?>, owner, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.DEEP_WOUND_ENTITY as EntityType<out ThrownItemEntity?>, x, y, z, world)

    override fun getDefaultItem(): Item {
        return Items.COD
    }

    companion object {
        private val time: TrackedData<Int> =
            DataTracker.registerData(DeepWoundEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(time, 0)
    }

    init {
        this.setNoGravity(true)
    }

    override fun tick() {
        this.setTime(this.getTime() + 1)
        if (this.getTime() > 30) {
            this.discard()
        }
        if (!this.world.isClient) {
            val serverWorld = this.world as ServerWorld
            serverWorld.spawnParticles(
                ParticleTypes.TRIAL_OMEN,
                this.x,
                this.y,
                this.z,
                1,
                this.world.random.nextDouble().minus(0.5).times(0.5),
                0.0,
                this.world.random.nextDouble().minus(0.5).times(0.5),
                0.0
            )
            super.tick()
        }
    }

    val unhealable = listOf(
        AstralEffects.UNHEALABLE_DAMAGE
    )
    val over = listOf(
        AstralEffects.OVERHEAL
    )

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if (entityHitResult.entity is LivingEntity) {
            val ent = entityHitResult.entity as LivingEntity
            val maxhp = ent.maxHealth
            val hp = ent.health
            val levels = ((maxhp - hp)).roundToInt()
            var hard_levels = levels
            var over_levels = levels
            val effects = ent.statusEffects.filter { unhealable.contains(it.effectType) }
            if (effects.isNotEmpty()) {
                effects.forEach {
                    val w = it.amplifier
                    hard_levels += w
                }
            }
            ent.addStatusEffect(
                StatusEffectInstance(
                    AstralEffects.UNHEALABLE_DAMAGE,
                    600, hard_levels,
                    false, true, true
                )
            )
            if (owner is LivingEntity) {
                val ow = owner as LivingEntity
                val effects_two = ow.statusEffects.filter { over.contains(it.effectType) }
                if (effects_two.isNotEmpty()) {
                    effects_two.forEach {
                        val w = it.amplifier
                        over_levels += w
                    }
                }
                ow.addStatusEffect(
                    StatusEffectInstance(
                        AstralEffects.OVERHEAL,
                        600, over_levels,
                        false, false, true
                    )
                )
                ow.absorptionAmount += (over_levels * 0.25f)
            }
        }

        super.onEntityHit(entityHitResult)
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        this.discard()
        super.onBlockHit(blockHitResult)
    }


    fun setTime(Time: Int) {
        dataTracker.set(time, Time)
    }

    fun getTime(): Int {
        return dataTracker.get(time) as Int
    }
}