package org.teamvoided.astralarsenal.entity.nails

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.init.AstralItems

open class BaseNailEntity : PersistentProjectileEntity {

    constructor(entityType: EntityType<out BaseNailEntity?>?, world: World?) :
            super(entityType as EntityType<out PersistentProjectileEntity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.NAIL_ENTITY as EntityType<out PersistentProjectileEntity?>, owner, world, Items.ARROW.defaultStack, AstralItems.NAILGUN.defaultStack)

    var fireNail = false
    var chargedNail = false
    var chargedDamage = 0.0
    val conductive = listOf(
        AstralEffects.CONDUCTIVE
    )

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if (entityHitResult.entity != owner && entityHitResult.entity is LivingEntity) {
            val hit = entityHitResult.entity as LivingEntity
            if (!this.fireNail && !this.chargedNail) {
                hit.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.NAILED),
                        owner,
                        owner
                    ), 2f
                )
                var bleed_levels = 0
                val effects_two = hit.statusEffects.filter { conductive.contains(it.effectType) }
                if (effects_two.isNotEmpty()) {
                    effects_two.forEach {
                        val w = it.amplifier
                        bleed_levels += w + 1
                    }
                }
                hit.addStatusEffect(
                    StatusEffectInstance(
                        AstralEffects.CONDUCTIVE,
                        400, bleed_levels,
                        false, false, true
                    )
                )
            } else if (this.fireNail) {
                hit.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.BURN),
                        owner,
                        owner
                    ), 1f
                )
                hit.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.NAILED),
                        owner,
                        owner
                    ), 1f
                )
                var bleed_levels = 0
                val effects_two = hit.statusEffects.filter { conductive.contains(it.effectType) }
                if (effects_two.isNotEmpty()) {
                    effects_two.forEach {
                        val w = it.amplifier
                        bleed_levels += w + 1
                    }
                }
                hit.addStatusEffect(
                    StatusEffectInstance(
                        AstralEffects.CONDUCTIVE,
                        200, bleed_levels,
                        false, true, true
                    )
                )
                hit.setOnFireFor(100)
            } else if (this.chargedNail) {
                hit.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.RICHOCHET),
                        owner,
                        owner
                    ), this.chargedDamage.toFloat()
                )
            }
        }
        this.world.playSound(
            null,
            this.x,
            this.y,
            this.z,
            SoundEvents.ITEM_TRIDENT_HIT,
            SoundCategory.PLAYERS,
            1.0F,
            1.0f
        )
        this.discard()
    }


    companion object {
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
    }

    override fun tick() {
        if(this.fireNail){
        world.addParticle(
            ParticleTypes.FLAME,
            true,
            this.x + random.rangeInclusive(-1, 1).times(0.1),
            this.y + random.rangeInclusive(-1, 1).times(0.1),
            this.z + random.rangeInclusive(-1, 1).times(0.1),
            random.nextDouble().times(2).minus(1).times(0.01),
            random.nextDouble().times(0.1),
            random.nextDouble().times(2).minus(1).times(0.01)
        )}
        else if(this.chargedNail){
            world.addParticle(
                ParticleTypes.ELECTRIC_SPARK,
                true,
                this.x + random.rangeInclusive(-1, 1).times(0.1),
                this.y + random.rangeInclusive(-1, 1).times(0.1),
                this.z + random.rangeInclusive(-1, 1).times(0.1),
                random.nextDouble().times(2).minus(1).times(0.01),
                random.nextDouble().times(0.1),
                random.nextDouble().times(2).minus(1).times(0.01)
            )
        }
        super.tick()
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        super.onBlockHit(blockHitResult)
    }

    override fun getDefaultItemStack(): ItemStack {
        return Items.AIR.defaultStack
    }

    override fun getHitSound(): SoundEvent {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND
    }

}