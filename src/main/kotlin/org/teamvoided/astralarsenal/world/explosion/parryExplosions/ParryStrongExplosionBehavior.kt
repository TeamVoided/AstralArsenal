package org.teamvoided.astralarsenal.world.explosion.parryExplosions

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.world.explosion.Explosion
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.world.explosion.OwnedExplosionBehavior

class ParryStrongExplosionBehavior(owner: Entity) : OwnedExplosionBehavior(owner) {
    override fun getKnockbackMultiplier(target: Entity): Float = 1.0f
    override fun calculateDamage(explosion: Explosion?, entity: Entity?): Float {
        if (entity is LivingEntity && entity != owner)
            entity.customDamage(AstralDamageTypes.PARRY, 15f, owner, owner)
        return 0f
    }
}