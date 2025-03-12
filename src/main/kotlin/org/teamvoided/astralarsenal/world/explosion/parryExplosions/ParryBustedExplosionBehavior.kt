package org.teamvoided.astralarsenal.world.explosion.parryExplosions

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.world.explosion.Explosion
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.world.explosion.OwnedExplosionBehavior

class ParryBustedExplosionBehavior(owner: Entity) : OwnedExplosionBehavior(owner) {
    override fun getKnockbackMultiplier(target: Entity): Float = 1.5f
    override fun calculateDamage(explosion: Explosion?, entity: Entity?): Float {
        if (entity is LivingEntity && entity != owner)
            entity.customDamage(AstralDamageTypes.PARRY, 20f, owner, owner)
        return 0f
    }
}