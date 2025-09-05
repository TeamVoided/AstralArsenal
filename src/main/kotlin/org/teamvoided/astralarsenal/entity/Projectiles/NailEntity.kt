package org.teamvoided.astralarsenal.entity.Projectiles

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.mob.EndermanEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.mixin.PersistentProjectileEntityAccessor
import org.teamvoided.astralarsenal.util.playSound

open class NailEntity : PersistentProjectileEntity {

    constructor(entityType: EntityType<out NailEntity>, world: World) :
            super(entityType, world)

    constructor(world: World, owner: LivingEntity) : super(
        AstralEntities.NAIL_ENTITY, owner, world, Items.ARROW.defaultStack, AstralItems.NAILCANNON.defaultStack
    )

    constructor(x: Double, y: Double, z: Double, world: World) : super(
        AstralEntities.NAIL_ENTITY, x, y, z, world, Items.ARROW.defaultStack, AstralItems.NAILCANNON.defaultStack
    )

    var nailType
        get() = NailType.getById(dataTracker.get(NAIL_TYPE))
        set(value) = dataTracker.set(NAIL_TYPE, value.id)

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if (entityHitResult.entity is LivingEntity) {
            val hit = entityHitResult.entity as LivingEntity
            val mult = if (hit is PlayerEntity) 1f else 4f
            hit.customDamage(
                AstralDamageTypes.NAILED,
                if (nailType == NailType.FIRE) (0.25f * mult) else if (nailType == NailType.CHARGED) 0.0f else (0.5f * mult),
                owner,
                owner
            )
            if (hit is EndermanEntity) return
            if (nailType != NailType.CHARGED && nailType != NailType.IMPALE) {
                var effectLevel = 0
                val currentEffect = hit.statusEffects.find { it.effectType == AstralEffects.CONDUCTIVE }
                currentEffect?.let { effectLevel = it.amplifier + 1 }
                if (hit.world is ServerWorld) {
                    hit.removeStatusEffect(AstralEffects.CONDUCTIVE)
                    hit.addStatusEffect(
                        StatusEffectInstance(
                            AstralEffects.CONDUCTIVE,
                            400, effectLevel,
                            false, false, true
                        )
                    )
                }
            } else if (nailType == NailType.IMPALE) {
                var effectLevel = 0
                val currentEffect = hit.statusEffects.find { it.effectType == AstralEffects.IMPALED }
                currentEffect?.let { effectLevel = it.amplifier + 1 }

                hit.removeStatusEffect(AstralEffects.IMPALED)
                hit.addStatusEffect(
                    StatusEffectInstance(
                        AstralEffects.IMPALED,
                        400, effectLevel,
                        false, false, true
                    )
                )
            }

            when (nailType) {
                NailType.BASE -> Unit
                NailType.FIRE -> {
                    hit.customDamage(AstralDamageTypes.BURN, 0.25f * mult, owner, owner)
                    hit.setOnFireFor(50)
                }

                NailType.CHARGED ->
                    hit.customDamage(AstralDamageTypes.RICHOCHET, 0.5f, owner, owner)

                NailType.IMPALE -> Unit
            }
            this.world.playSound(this.pos, SoundEvents.ITEM_TRIDENT_HIT, SoundCategory.PLAYERS, 1.0F, 1.0f)
            this.discard()
        }
    }

    override fun age() {
        super.age()
        if ((this as PersistentProjectileEntityAccessor).life() >= 300) {
            this.discard()
        }
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(NAIL_TYPE, NailType.BASE.id)
        super.initDataTracker(builder)
    }

    override fun tick() {
        val (particle, chance) = when (nailType) {
            NailType.BASE -> null to 0
            NailType.FIRE -> ParticleTypes.FLAME to 10
            NailType.CHARGED -> ParticleTypes.ELECTRIC_SPARK to 4
            NailType.IMPALE -> ParticleTypes.CRIMSON_SPORE to 20
        }
        if (particle != null && (world.time + blockPos.asLong()) % chance == 0L) {
            if (world is ServerWorld) (world as ServerWorld).spawnParticles(
                particle, this.x, this.y, this.z,
                1,
                0.1, 0.1, 0.1,
                0.0
            )

        }
        super.tick()
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        this.sound = getHitSound()
        this.shake = 0
    }

    override fun getDefaultItemStack(): ItemStack = Items.AIR.defaultStack
    override fun getHitSound(): SoundEvent = SoundEvents.ITEM_TRIDENT_HIT_GROUND

    companion object {
        val NAIL_TYPE: TrackedData<Int> =
            DataTracker.registerData(NailEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }

    enum class NailType(val id: Int) {
        BASE(0), FIRE(1), CHARGED(2), IMPALE(3);

        companion object {
            fun getById(id: Int): NailType = entries.getOrNull(id) ?: BASE
        }
    }
}