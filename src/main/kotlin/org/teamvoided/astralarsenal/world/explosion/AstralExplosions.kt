package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageType
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.registry.RegistryKey
import net.minecraft.world.explosion.Explosion
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.util.DEFAULT_KNOCKBACK

@Suppress("UNUSED_PARAMETER")
object AstralExplosions {
    val FROST = PostDamageExplosionBehavior { _, it -> it.frozenTicks += 1000 }
    val RANCID = PostDamageExplosionBehavior { _, it ->
        if (it is LivingEntity) {
            it.addEffect(StatusEffects.SLOWNESS)
            it.addEffect(StatusEffects.POISON)
            it.addEffect(StatusEffects.WEAKNESS)
            it.addEffect(AstralEffects.REDUCE)
            it.addEffect(AstralEffects.BLEED)
        }
    }


    fun boomExplosion_DELETE_THIS(owner: Entity?) = PostDamageExplosionBehavior(
        { _, it -> it == owner },
        { DEFAULT_KNOCKBACK },
        { _, it -> if (it == owner) it.customDamage(AstralDamageTypes.BOOM, 5f, owner, owner) }
    )

    fun explosion_DELETE_THIS(
        type: RegistryKey<DamageType>, amount: Float, owner: Entity? = null, knockback: Float = DEFAULT_KNOCKBACK
    ) = PostDamageExplosionBehavior(
        ::livingPredicate, { knockback }, { _, it -> it.customDamage(type, amount, owner, owner) }
    )

    fun livingPredicate(explosion: Explosion, entity: Entity) = entity is LivingEntity
}