package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.explosion.Explosion
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage

class StrongExplosionBehavior(owner: Entity) : OwnedExplosionBehavior(owner) {
    override fun getKnockbackMultiplier(target: Entity): Float = 3f
    override fun calculateDamage(explosion: Explosion?, entity: Entity?): Float {
        if (entity is LivingEntity)
            entity.customDamage(AstralDamageTypes.BOOM, if (entity is PlayerEntity) 15f else 30f, owner, owner)
        return 0f
    }
}