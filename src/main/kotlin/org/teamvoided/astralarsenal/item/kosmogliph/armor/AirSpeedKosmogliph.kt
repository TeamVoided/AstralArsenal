package org.teamvoided.astralarsenal.item.kosmogliph.armor

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph

interface AirSpeedKosmogliph : Kosmogliph {
    companion object {
        val AIR_STRAFE_MODIFIER = 1.0f
        val TICKS_BEFORE_MODIFIED = 23

        val ticksMap = mutableMapOf<Entity, Int>()
    }

    var Entity.fallTime
        get() = ticksMap[this] ?: 0
        set(value) { ticksMap[this] = value }

    override fun modifyAirStrafeSpeed(entity: LivingEntity, speed: Float): Float {
        println(entity.fallTime)

        if(entity.fallTime > TICKS_BEFORE_MODIFIED)
            return entity.movementSpeed * AIR_STRAFE_MODIFIER
        return speed
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super.inventoryTick(stack, world, entity, slot, selected)

        if (entity.isOnGround)
            entity.fallTime = 0
        else
            entity.fallTime += 1
    }
}