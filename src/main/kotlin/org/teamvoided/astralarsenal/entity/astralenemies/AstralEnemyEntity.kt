package org.teamvoided.astralarsenal.entity.astralenemies

import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags

open class AstralEnemyEntity(entityType: EntityType<out HostileEntity>?, world: World?) : HostileEntity(entityType, world), Monster {
    var usingSpecialMovement = false
    var cantMove = false
}