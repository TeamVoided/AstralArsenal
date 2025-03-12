package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.entity.Entity
import net.minecraft.world.explosion.Explosion
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage

class KnockbackExplosionBehavior(owner: Entity) : OwnedExplosionBehavior(owner) {
    override fun shouldDamage(explosion: Explosion, entity: Entity): Boolean = owner == entity
    override fun getKnockbackMultiplier(target: Entity): Float = 2.5f

    override fun calculateDamage(explosion: Explosion?, entity: Entity?): Float {
        if (owner == entity) entity.customDamage(AstralDamageTypes.BOOM, 5f, owner, owner)
        return 0f
    }
}