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
//        builder.add(SLUDGE_FLAVOUR, 0)
//        builder.add(MAGIC, 0)
    }

//    var age = 0
//    var owner: Entity? = null
//
//    override fun tick() {
//        age += 1
//        if (age > 200) this.discard()
//        val entities = world.getOtherEntities(
//            this, Box(
//                pos.x + 5,
//                pos.y + 0.1,
//                pos.z + 5,
//                pos.x + 5,
//                pos.y - 0.1,
//                pos.z + 5
//            )
//        ).filter { it is LivingEntity && this.distanceTo(it) < 5 }
//        val damage = when (sludge) {
//            StarSludgeProjectileEntity.SludgeFlavour.UNASSIGNED -> 0.1f
//            StarSludgeProjectileEntity.SludgeFlavour.STRONG -> 0.1f
//            StarSludgeProjectileEntity.SludgeFlavour.FIRE -> 0.1f
//            StarSludgeProjectileEntity.SludgeFlavour.ICE -> 0.1f
//            StarSludgeProjectileEntity.SludgeFlavour.STATIC -> 0.05f
//            StarSludgeProjectileEntity.SludgeFlavour.MAGIC -> 0.1f
//        }
//        val damageType = when (sludge) {
//            StarSludgeProjectileEntity.SludgeFlavour.UNASSIGNED -> AstralDamageTypes.SLUDGED
//            StarSludgeProjectileEntity.SludgeFlavour.STRONG -> AstralDamageTypes.SLUDGED
//            StarSludgeProjectileEntity.SludgeFlavour.FIRE -> DamageTypes.IN_FIRE
//            StarSludgeProjectileEntity.SludgeFlavour.ICE -> DamageTypes.FREEZE
//            StarSludgeProjectileEntity.SludgeFlavour.STATIC -> AstralDamageTypes.ELECTROSTATICED
//            StarSludgeProjectileEntity.SludgeFlavour.MAGIC -> DamageTypes.MAGIC
//        }
//        for (entity in entities) {
//            if (entity is LivingEntity) {
//                if (age % 20 == 0) {
//                    entity.customDamage(
//                        damageType, damage, owner, owner
//                    )
//                }
//                when (sludge) {
//                    StarSludgeProjectileEntity.SludgeFlavour.FIRE -> entity.setOnFireFor(100)
//                    StarSludgeProjectileEntity.SludgeFlavour.ICE -> if(entity.frozenTicks < 100) entity.frozenTicks = 100
//                    StarSludgeProjectileEntity.SludgeFlavour.MAGIC -> {
//                        val effect = when (magic) {
//                            StarSludgeProjectileEntity.MagicEffect.BLEED -> AstralEffects.BLEED
//                            StarSludgeProjectileEntity.MagicEffect.POISON -> StatusEffects.POISON
//                            StarSludgeProjectileEntity.MagicEffect.WITHER -> StatusEffects.WITHER
//                        }
//                        val duration = when (magic) {
//                            StarSludgeProjectileEntity.MagicEffect.BLEED -> 50
//                            StarSludgeProjectileEntity.MagicEffect.POISON -> 100
//                            StarSludgeProjectileEntity.MagicEffect.WITHER -> 50
//                        }
//                        entity.addStatusEffect(
//                            StatusEffectInstance(
//                                effect,
//                                duration, 1,
//                                false, false, true
//                            )
//                        )
//                    }
//                    else -> {}
//                }
//            }
//        }
//        if(world is ServerWorld){
//            (world as ServerWorld).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, this.x, this.y, this.z,
//                1,
//                5.0, 0.1, 5.0,
//                0.0)
//        }
//
//        super.tick()
//    }

//    companion object {
//        val SLUDGE_FLAVOUR: TrackedData<Int> =
//            DataTracker.registerData(StarSludgeAOEEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
//        val MAGIC: TrackedData<Int> =
//            DataTracker.registerData(StarSludgeAOEEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
//    }
//
//    var sludge
//        get() = StarSludgeProjectileEntity.SludgeFlavour.getById(this.dataTracker.get(SLUDGE_FLAVOUR))
//        set(value) = dataTracker.set(SLUDGE_FLAVOUR, value.id)
//
//    var magic
//        get() = StarSludgeProjectileEntity.MagicEffect.getById(this.dataTracker.get(MAGIC))
//        set(value) = dataTracker.set(MAGIC, value.id)

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
        TODO("Not yet implemented")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
        TODO("Not yet implemented")
    }

}