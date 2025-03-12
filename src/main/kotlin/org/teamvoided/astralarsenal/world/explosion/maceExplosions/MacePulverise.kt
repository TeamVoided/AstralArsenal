package org.teamvoided.astralarsenal.world.explosion.maceExplosions

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.world.explosion.Explosion
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.world.explosion.OwnedExplosionBehavior

class MacePulverise(owner: Entity) : OwnedExplosionBehavior(owner) {
    override fun getKnockbackMultiplier(target: Entity): Float = 2.5f
    override fun calculateDamage(explosion: Explosion?, entity: Entity?): Float {
        if (entity is LivingEntity) entity.customDamage(AstralDamageTypes.PULVERISED, 15f, owner, owner)
        return 0f
    }
}