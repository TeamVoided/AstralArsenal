package org.teamvoided.astralarsenal.world.explosion.SludgeExplosions

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.astralarsenal.entity.starsludge.StarSludgeProjectileEntity
import org.teamvoided.astralarsenal.entity.starsludge.StarSludgeProjectileEntity.MagicEffect
import org.teamvoided.astralarsenal.entity.starsludge.StarSludgeProjectileEntity.SludgeFlavour
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEffects

class SludgeExplosionBehavior(causingEntity: Entity, sludgeType: StarSludgeProjectileEntity.SludgeFlavour, magicType: StarSludgeProjectileEntity.MagicEffect) : ExplosionBehavior() {
    val cause = causingEntity
    val type = sludgeType
    val magic = magicType
    override fun canDestroyBlock(
        explosion: Explosion,
        world: BlockView,
        pos: BlockPos,
        state: BlockState,
        power: Float
    ): Boolean {
        return false
    }

    override fun getKnockbackMultiplier(target: Entity): Float {
        return 1f
    }

    override fun calculateDamage(explosion: Explosion, entity: Entity): Float {
        if(entity !is LivingEntity){ return 0f}
        val dmg = when (type){
            SludgeFlavour.UNASSIGNED -> 5f
            SludgeFlavour.STRONG -> 10f
            SludgeFlavour.FIRE -> 7.5f
            SludgeFlavour.ICE -> 7.5f
            SludgeFlavour.STATIC -> 1f
            SludgeFlavour.MAGIC -> 2.5f
        }
        val dmgType = when(type){
            SludgeFlavour.FIRE -> AstralDamageTypes.FIREBOMBED
            SludgeFlavour.ICE -> AstralDamageTypes.ICE_SHRAPNEL
            SludgeFlavour.STATIC -> AstralDamageTypes.EMP
            SludgeFlavour.MAGIC -> AstralDamageTypes.MAGIC_BOMB
            else -> {AstralDamageTypes.SLUDGE_BOMB}
        }
        entity.customDamage(
            dmgType, dmg, cause, cause
        )
        when (type) {
            SludgeFlavour.FIRE -> entity.setOnFireFor(100)
            SludgeFlavour.ICE -> if(entity.frozenTicks < 200) entity.frozenTicks = 200
            SludgeFlavour.MAGIC -> {
                val effect = when (magic) {
                    MagicEffect.BLEED -> AstralEffects.BLEED
                    MagicEffect.POISON -> StatusEffects.POISON
                    MagicEffect.WITHER -> StatusEffects.WITHER
                }
                val duration = when (magic) {
                    MagicEffect.BLEED -> 50
                    MagicEffect.POISON -> 100
                    MagicEffect.WITHER -> 50
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
        return 0f
    }
}