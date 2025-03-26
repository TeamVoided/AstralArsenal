package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.entity.Entity
import net.minecraft.world.explosion.Explosion

class FrostExplosionBehavior : BlockSafeExplosionBehavior() {
    override fun getKnockbackMultiplier(target: Entity): Float = 2.5f
    override fun calculateDamage(explosion: Explosion?, entity: Entity?): Float {
        entity?.let { if (it.frozenTicks < 500) it.frozenTicks = 500 }
        return 0f
    }
}