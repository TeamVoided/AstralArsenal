package org.teamvoided.astralarsenal.entity.starsludge

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.FlameShotEntity
import org.teamvoided.astralarsenal.entity.astralenemies.AstralSniperEntity
import org.teamvoided.astralarsenal.entity.astralenemies.AstralSniperEntity.Companion.SNIPE_TYPE
import org.teamvoided.astralarsenal.entity.nails.NailEntity
import org.teamvoided.astralarsenal.entity.nails.NailEntity.Companion.NAIL_TYPE
import org.teamvoided.astralarsenal.entity.nails.NailEntity.NailType
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.init.AstralItems

class StarSludgeProjectileEntity : ThrownItemEntity {

    constructor(entityType: EntityType<out StarSludgeProjectileEntity?>?, world: World) :
            super(entityType as EntityType<out ThrownItemEntity?>?, world)

    constructor(world: World, owner: LivingEntity?) :
            super(AstralEntities.SLUDGE_PROJ as EntityType<out ThrownItemEntity?>, owner, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.SLUDGE_PROJ as EntityType<out ThrownItemEntity?>, x, y, z, world)

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if (!entityHitResult.entity.isLiving) {
            return
        }
        val damage = when (sludge) {
            SludgeFlavour.UNASSIGNED -> 5f
            SludgeFlavour.STRONG -> 10f
            SludgeFlavour.FIRE -> 7.5f
            SludgeFlavour.ICE -> 7.5f
            SludgeFlavour.STATIC -> 1f
            SludgeFlavour.MAGIC -> 2.5f
        }
        val damageType = when (sludge) {
            SludgeFlavour.UNASSIGNED -> AstralDamageTypes.SLUDGED
            SludgeFlavour.STRONG -> AstralDamageTypes.SLUDGED
            SludgeFlavour.FIRE -> DamageTypes.IN_FIRE
            SludgeFlavour.ICE -> DamageTypes.FREEZE
            SludgeFlavour.STATIC -> AstralDamageTypes.ELECTROSTATICED
            SludgeFlavour.MAGIC -> DamageTypes.MAGIC
        }
        val hitEntity = entityHitResult.entity as LivingEntity

        hitEntity.customDamage(
            damageType, damage, owner, owner
        )

        when (sludge) {
            SludgeFlavour.FIRE -> hitEntity.setOnFireFor(200)
            SludgeFlavour.ICE -> if(hitEntity.frozenTicks < 200) hitEntity.frozenTicks = 200
            SludgeFlavour.STATIC -> hitEntity.addStatusEffect(
                StatusEffectInstance(
                    AstralEffects.STATICALLY_SLUDGED,
                    100, 1,
                    false, false, true
                )
            )

            SludgeFlavour.MAGIC -> {
                val effect = when (magic) {
                    MagicEffect.BLEED -> AstralEffects.BLEED
                    MagicEffect.POISON -> StatusEffects.POISON
                    MagicEffect.WITHER -> StatusEffects.WITHER
                }
                val duration = when (magic) {
                    MagicEffect.BLEED -> 100
                    MagicEffect.POISON -> 200
                    MagicEffect.WITHER -> 100
                }
                hitEntity.addStatusEffect(
                    StatusEffectInstance(
                        effect,
                        duration, 1,
                        false, false, true
                    )
                )
            }
            else -> {}
        }
        super.onEntityHit(entityHitResult)
    }

//    override fun onBlockHit(blockHitResult: BlockHitResult) {
//        if(!world.isClient && owner is LivingEntity){
//            val AOE = StarSludgeAOEEntity(world, owner as LivingEntity)
//            AOE.sludge = this.sludge
//            AOE.magic = this.magic
//            AOE.setPosition(this.pos)
//            world.spawnEntity(AOE)
//        }
//        super.onBlockHit(blockHitResult)
//    }

    override fun getDefaultItem(): Item {
        TODO("Not yet implemented")
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(SLUDGE_FLAVOUR, SludgeFlavour.UNASSIGNED.id)
        builder.add(MAGIC, MagicEffect.BLEED.id)
    }

    companion object {
        val SLUDGE_FLAVOUR: TrackedData<Int> =
            DataTracker.registerData(StarSludgeProjectileEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val MAGIC: TrackedData<Int> =
            DataTracker.registerData(StarSludgeProjectileEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }

    var sludge
        get() = SludgeFlavour.getById(dataTracker.get(SLUDGE_FLAVOUR))
        set(value) = dataTracker.set(SLUDGE_FLAVOUR, value.id)

    var magic
        get() = MagicEffect.getById(dataTracker.get(MAGIC))
        set(value) = dataTracker.set(MAGIC, value.id)

    enum class SludgeFlavour(val id: Int) {
        UNASSIGNED(0), FIRE(1), ICE(2), STATIC(3), MAGIC(4), STRONG(5);

        companion object {
            fun getById(id: Int): SludgeFlavour = entries.first { it.id == id }
        }
    }

    enum class MagicEffect(val id: Int) {
        WITHER(0), BLEED(1), POISON(2);

        companion object {
            fun getById(id: Int): MagicEffect = entries.first { it.id == id }
        }
    }
}