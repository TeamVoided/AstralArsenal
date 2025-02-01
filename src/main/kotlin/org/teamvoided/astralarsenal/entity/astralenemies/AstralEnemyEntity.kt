package org.teamvoided.astralarsenal.entity.astralenemies

import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.world.World

open class AstralEnemyEntity(entityType: EntityType<out HostileEntity>?, world: World?) :
    HostileEntity(entityType, world), Monster {
    var usingSpecialMovement = false
    var cantMove = false
}