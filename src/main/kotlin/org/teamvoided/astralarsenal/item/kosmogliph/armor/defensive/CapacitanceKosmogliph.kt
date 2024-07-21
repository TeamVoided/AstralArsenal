package org.teamvoided.astralarsenal.item.kosmogliph.armor.defensive

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class CapacitanceKosmogliph (id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.CHESTPLATE
}) {
    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot
    ): Float {
        var outputDamage = damage
        if (source.isTypeIn(AstralDamageTypeTags.IS_PLASMA)){
            outputDamage = (outputDamage * 0.2).toFloat()
        }
        return super.modifyDamage(stack, entity, outputDamage, source, equipmentSlot)
    }
}