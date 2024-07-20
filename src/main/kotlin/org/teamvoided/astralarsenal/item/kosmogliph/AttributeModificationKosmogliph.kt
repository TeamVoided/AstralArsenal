package org.teamvoided.astralarsenal.item.kosmogliph

import arrow.core.Predicate
import net.minecraft.entity.Entity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeInstance
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.Holder
import net.minecraft.util.Identifier
import net.minecraft.world.World

class AttributeModificationKosmogliph(
    id: Identifier,
    applicationPredicate: Predicate<ItemStack>,
    val attribute: Holder<EntityAttribute>,
    val modifierId: Identifier = id,
    val modifierValue: Double,
    val modifierOperation: EntityAttributeModifier.Operation,
    val isArmor: Boolean = false,
    val slotId: Int
) : SimpleKosmogliph(id, applicationPredicate) {
    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (entity !is PlayerEntity) return
        val attributeInst = entity.attributes.getInstance(attribute) ?: return

        if (isArmor && entity.inventory.armor.contains(stack) && entity.inventory.armor.indexOf(stack) == slotId) {
            applyModifier(attributeInst)
        } else if (slot == slotId) {
            applyModifier(attributeInst)
        } else removeModifier(attributeInst)
    }

    fun applyModifier(attributeInst: EntityAttributeInstance) {
        if (attributeInst.hasModifier(modifierId)) return
        attributeInst.addTemporaryModifier(EntityAttributeModifier(modifierId, modifierValue, modifierOperation))
    }

    fun removeModifier(attributeInst: EntityAttributeInstance) {
        val mod = attributeInst.getModifier(modifierId) ?: return
        attributeInst.removeModifier(mod)
    }
}