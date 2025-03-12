package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageType
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.registry.RegistryKey
import net.minecraft.world.explosion.Explosion
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.util.DEFAULT_KNOCKBACK

@Suppress("UNUSED_PARAMETER")
object AstralExplosions {
    val FROST = noDamageExplosion { _, it -> it.frozenTicks += 1000 }
    val RANCID = noDamageExplosion { _, it ->
        it.addEffect(StatusEffects.SLOWNESS)
        it.addEffect(StatusEffects.POISON)
        it.addEffect(StatusEffects.WEAKNESS)
        it.addEffect(AstralEffects.REDUCE)
        it.addEffect(AstralEffects.BLEED)
    }

    fun damageExplosion(
        type: RegistryKey<DamageType>, amount: Float, owner: Entity, knockback: Float = DEFAULT_KNOCKBACK
    ) = CustomExplosionBehavior(
        ::livingPredicate, { knockback }, { _, it -> it.customDamage(type, amount, owner, owner) }
    )

    fun livingPredicate(explosion: Explosion, entity: Entity) = entity is LivingEntity
}