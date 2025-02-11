package org.teamvoided.astralarsenal.entity.starsludge

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralEntities

class StarSludgeAOEEntity : Entity {

    constructor(entityType: EntityType<out StarSludgeAOEEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.SLUDGE_AOE as EntityType<out Entity?>, world) {
    }

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.SLUDGE_AOE as EntityType<out Entity?>, world)

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(SLUDGE_FLAVOUR, StarSludgeProjectileEntity.SludgeFlavour.UNASSIGNED.id)
        builder.add(MAGIC, StarSludgeProjectileEntity.MagicEffect.BLEED.id)
//        super.initDataTracker(builder)
    }

    var age = 0
    var owner: Entity? = null

    override fun tick() {
        age += 1
        if (age > 200) this.discard()
        val entities = world.getOtherEntities(
            this, Box(
                this.pos.x + 2.5,
                this.pos.y + 0.5,
                this.pos.z + 2.5,
                this.pos.x - 2.5,
                this.pos.y - 0.5,
                this.pos.z - 2.5
            )
        ).filterIsInstance<LivingEntity>().filter {this.distanceTo(it) < 2.5}

        val damage = when (sludge) {
            StarSludgeProjectileEntity.SludgeFlavour.UNASSIGNED -> 1f
            StarSludgeProjectileEntity.SludgeFlavour.STRONG -> 1f
            StarSludgeProjectileEntity.SludgeFlavour.FIRE -> 1f
            StarSludgeProjectileEntity.SludgeFlavour.ICE -> 1f
            StarSludgeProjectileEntity.SludgeFlavour.STATIC -> 0.5f
            StarSludgeProjectileEntity.SludgeFlavour.MAGIC -> 1f
        }
        val damageType = when (sludge) {
            StarSludgeProjectileEntity.SludgeFlavour.UNASSIGNED -> AstralDamageTypes.SLUDGE_AOE
            StarSludgeProjectileEntity.SludgeFlavour.STRONG -> AstralDamageTypes.SLUDGE_AOE
            StarSludgeProjectileEntity.SludgeFlavour.FIRE -> DamageTypes.IN_FIRE
            StarSludgeProjectileEntity.SludgeFlavour.ICE -> DamageTypes.FREEZE
            StarSludgeProjectileEntity.SludgeFlavour.STATIC -> AstralDamageTypes.RICHOCHET
            StarSludgeProjectileEntity.SludgeFlavour.MAGIC -> DamageTypes.MAGIC
        }
        for (entity in entities) {

            if (age % 20 == 0) {
                entity.customDamage(
                    damageType, damage, owner, owner
                )
            }
            when (sludge) {
                StarSludgeProjectileEntity.SludgeFlavour.FIRE -> entity.setOnFireFor(100)
                StarSludgeProjectileEntity.SludgeFlavour.ICE -> if (entity.frozenTicks < 200) entity.frozenTicks = 200
                StarSludgeProjectileEntity.SludgeFlavour.MAGIC -> {
                    val effect = when (magic) {
                        StarSludgeProjectileEntity.MagicEffect.BLEED -> AstralEffects.BLEED
                        StarSludgeProjectileEntity.MagicEffect.POISON -> StatusEffects.POISON
                        StarSludgeProjectileEntity.MagicEffect.WITHER -> StatusEffects.WITHER
                    }
                    val duration = when (magic) {
                        StarSludgeProjectileEntity.MagicEffect.BLEED -> 50
                        StarSludgeProjectileEntity.MagicEffect.POISON -> 100
                        StarSludgeProjectileEntity.MagicEffect.WITHER -> 50
                    }
                    entity.addStatusEffect(
                        StatusEffectInstance(
                            effect,
                            duration, 1,
                            false, false, true
                        )
                    )
                }

                else -> {}
            }
        }
        if (world is ServerWorld) {
            (world as ServerWorld).spawnParticles(
                ParticleTypes.TOTEM_OF_UNDYING, this.x, this.y, this.z,
                1,
                1.0, 0.0, 1.0,
                0.0
            )
        }
        super.tick()
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }

    companion object {
        val SLUDGE_FLAVOUR: TrackedData<Int> =
            DataTracker.registerData(StarSludgeAOEEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val MAGIC: TrackedData<Int> =
            DataTracker.registerData(StarSludgeAOEEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }

    var sludge
        get() = StarSludgeProjectileEntity.SludgeFlavour.getById(dataTracker.get(SLUDGE_FLAVOUR))
        set(value) = dataTracker.set(SLUDGE_FLAVOUR, value.id)

    var magic
        get() = StarSludgeProjectileEntity.MagicEffect.getById(dataTracker.get(MAGIC))
        set(value) = dataTracker.set(MAGIC, value.id)


}