package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.registry.Holder
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.astralarsenal.util.DEFAULT_KNOCKBACK
import org.teamvoided.astralarsenal.world.explosion.AstralExplosions.livingPredicate


typealias ShouldDamage = (explosion: Explosion, entity: Entity) -> Boolean
typealias PostDamage = (explosion: Explosion, entity: Entity) -> Unit
typealias KnockbackMultiplier = (target: Entity) -> Float

abstract class BlockSafeExplosionBehavior : ExplosionBehavior() {
    override fun canDestroyBlock(e: Explosion, w: BlockView, p: BlockPos, s: BlockState, pow: Float): Boolean = false
}

open class CustomExplosionBehavior(
    val dealDamage: ShouldDamage, val knockbackMultiplier: KnockbackMultiplier,
) : BlockSafeExplosionBehavior() {
    constructor(knockbackMultiplier: Float = DEFAULT_KNOCKBACK) : this(::livingPredicate, { knockbackMultiplier })

    override fun getKnockbackMultiplier(target: Entity?): Float =
        if (target != null) knockbackMultiplier(target) else super.getKnockbackMultiplier(null)

    override fun shouldDamage(explosion: Explosion?, entity: Entity?): Boolean =
        if (explosion != null && entity != null) dealDamage(explosion, entity)
        else super.shouldDamage(explosion, entity)
}

class PostDamageExplosionBehavior(
    shouldDamage: ShouldDamage, knockbackMultiplier: KnockbackMultiplier, val postDamage: PostDamage
) : CustomExplosionBehavior(shouldDamage, knockbackMultiplier) {
    constructor(knockbackMultiplier: Float = DEFAULT_KNOCKBACK, postDamage: PostDamage) :
            this(::livingPredicate, { knockbackMultiplier }, postDamage)

    override fun calculateDamage(explosion: Explosion?, entity: Entity?): Float {
        if (explosion != null && entity != null && shouldDamage(explosion, entity)) postDamage(explosion, entity)
        return super.calculateDamage(explosion, entity)
    }
}

internal fun LivingEntity.addEffect(type: Holder<StatusEffect>, duration: Int = 600, amplifier: Int = 0) =
    this.addStatusEffect(
        StatusEffectInstance(type, duration, amplifier, false, true, true)
    )

// delete this when done
abstract class OwnedExplosionBehavior(val owner: Entity) : BlockSafeExplosionBehavior()
