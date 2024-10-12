package org.teamvoided.astralarsenal.item.kosmogliph.armor

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.SoundPlayS2CPacket
import net.minecraft.registry.Holder
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph
import kotlin.math.min

interface AirSpeedKosmogliph : Kosmogliph {
    companion object {
        val AIR_STRAFE_MODIFIER = 1.0f
        val TICKS_BEFORE_MODIFIED = 5
        val TICKS_FOR_FULL = 40

        val ticksMap = mutableMapOf<Entity, Int>()
    }

    var Entity.fallTime
        get() = ticksMap[this] ?: 0
        set(value) {
            ticksMap[this] = value
        }

    override fun modifyAirStrafeSpeed(entity: LivingEntity, speed: Float): Float {
        if (entity.fallTime > TICKS_BEFORE_MODIFIED) {
            return entity.movementSpeed * (AIR_STRAFE_MODIFIER * (1f/ TICKS_FOR_FULL * min(TICKS_FOR_FULL, entity.fallTime)))
        }
        return speed
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super.inventoryTick(stack, world, entity, slot, selected)

        if (entity.isOnGround || entity.isInFluid || (entity is LivingEntity && entity.isClimbing))
            entity.fallTime = 0
        else {
            entity.fallTime += 1
//            if (entity.fallTime == TICKS_BEFORE_MODIFIED && entity is ServerPlayerEntity) {
//                entity.networkHandler.send(
//                    SoundPlayS2CPacket(
//                        Holder.createDirect(SoundEvents.ENTITY_BREEZE_SLIDE),
//                        SoundCategory.PLAYERS,
//                        entity.x,
//                        entity.y,
//                        entity.z,
//                        1.0F,
//                        1.0F,
//                        world.getRandom().nextLong()
//                    )
//                )
//            }
        }
    }
}