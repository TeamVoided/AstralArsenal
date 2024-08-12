package org.teamvoided.astralarsenal.item.kosmogliph.armor

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class GrappleKosmogliph(id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.HELMET
}) {
    //im removing this shit - astra
    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (slot == 3) {
            val data = stack.get(AstralItemComponents.GRAPPLE_DATA)
                ?: throw IllegalStateException("Erm, how the fuck did you manage this")
            var negateFallDamage = data.negateFallDamage
            if (entity.isOnGround) {

                if (negateFallDamage) negateFallDamage = false
            }
            if (!entity.isOnGround && entity.horizontalCollision && entity.velocity.y < -0.1) {
                entity.setVelocity(
                    entity.movement.x,
                    (entity.movement.y - 0.006).coerceAtLeast(-0.1),
                    entity.movement.z
                )
                entity.velocityModified = true
                entity.resetFallDistance()
                negateFallDamage = true
            }
            stack.set(AstralItemComponents.GRAPPLE_DATA, Data(0, 0, negateFallDamage))
        }
    }

    override fun shouldNegateDamage(
        stack: ItemStack,
        entity: LivingEntity,
        source: DamageSource,
        equipmentSlot: EquipmentSlot
    ): Boolean {
        val data = stack.get(AstralItemComponents.GRAPPLE_DATA) ?: return false

        if (data.negateFallDamage) {
            stack.set(AstralItemComponents.GRAPPLE_DATA, Data(data.jumps, data.timer, false))
            return true
        }

        return false
    }

    data class Data(
        val jumps: Int,
        val timer: Int,
        val negateFallDamage: Boolean
    ) {
        companion object {
            val CODEC: Codec<Data> = RecordCodecBuilder.create { builder ->
                val group = builder.group(
                    Codec.INT.fieldOf("jumps").orElse(0).forGetter(Data::jumps),
                    Codec.INT.fieldOf("timer").orElse(0).forGetter(Data::timer),
                    Codec.BOOL.fieldOf("negateFallDamage").orElse(false).forGetter(Data::negateFallDamage)
                )

                group.apply(builder, ::Data)
            }
        }
    }
}