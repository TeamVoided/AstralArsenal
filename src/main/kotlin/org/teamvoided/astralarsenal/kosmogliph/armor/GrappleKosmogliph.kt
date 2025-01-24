package org.teamvoided.astralarsenal.kosmogliph.armor

import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.components.GrappleData
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

class GrappleKosmogliph(id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.HELMET
}) {
    //im removing this shit - astra
    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (world is ServerWorld) println("1")
        if (slot == 3) {
            if (world is ServerWorld) println("2")
            val data = stack.get(AstralDataComponents.GRAPPLE_DATA)
                ?: throw IllegalStateException("Erm, how the fuck did you manage this")
            var negateFallDamage = data.negateFallDamage
            if (!entity.isOnGround && entity.horizontalCollision) {
                if (world.isClient) {
                    entity.setVelocity(
                        entity.movement.x,
                        (entity.movement.y).coerceAtLeast(-0.1),
                        entity.movement.z
                    )
                    entity.velocityModified = true
                    println("velocity modified")
                } else {
                    println("fall set to 0")
                    entity.fallDistance = 0f
                }
            }
            if (entity.velocity.y > -0.1) {
                entity.resetFallDistance()
            }
            stack.set(AstralDataComponents.GRAPPLE_DATA, GrappleData(0, 0, negateFallDamage))
        }
    }

    override fun shouldNegateDamage(
        stack: ItemStack,
        entity: LivingEntity,
        source: DamageSource,
        equipmentSlot: EquipmentSlot
    ): Boolean {
        val data = stack.get(AstralDataComponents.GRAPPLE_DATA) ?: return false

        if (data.negateFallDamage && source.isType(DamageTypes.FALL)) {
            stack.set(AstralDataComponents.GRAPPLE_DATA, GrappleData(data.jumps, data.timer, false))
            return true
        }

        return false
    }

}