package org.teamvoided.astralarsenal.entity.astralenemies

import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.FlyingEntity
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags

open class AstralFlyingEnemyEntity(entityType: EntityType<out AstralFlyingEnemyEntity>, world: World) : FlyingEntity(entityType, world) {
    override fun getBaseXpDropped(): Int {
        val extra = random.rangeInclusive(0,10)
        return 20 + extra
    }
}
