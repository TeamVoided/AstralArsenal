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

class CustomExplosionBehavior(
    val shouldDamage: ShouldDamage, val knockbackMultiplier: KnockbackMultiplier, val postDamage: PostDamage
) : BlockSafeExplosionBehavior() {
    override fun getKnockbackMultiplier(target: Entity?): Float =
        if (target != null) knockbackMultiplier(target) else super.getKnockbackMultiplier(target)

    override fun shouldDamage(explosion: Explosion?, entity: Entity?): Boolean =
        if (explosion != null && entity != null) shouldDamage.invoke(explosion, entity) else super.shouldDamage(
            explosion,
            entity
        )

    override fun calculateDamage(explosion: Explosion?, entity: Entity?): Float {
        if (explosion != null && entity != null) postDamage(explosion, entity)
        return 0f
    }
}

fun noDamageExplosion(
    knockback: Float = DEFAULT_KNOCKBACK, postDamage: (explosion: Explosion, entity: LivingEntity) -> Unit
) = CustomExplosionBehavior(
    AstralExplosions::livingPredicate, { knockback },
    { exp, entity -> if (livingPredicate(exp, entity)) postDamage(exp, entity as LivingEntity) }
)

internal fun LivingEntity.addEffect(type: Holder<StatusEffect>, duration: Int = 600, amplifier: Int = 0) =
    this.addStatusEffect(
        StatusEffectInstance(type, duration, amplifier, false, true, true)
    )


abstract class OwnedExplosionBehavior(val owner: Entity) : BlockSafeExplosionBehavior()
