package org.teamvoided.astralarsenal.item.kosmogliph.armor.defensive

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.ArmorItem
import net.minecraft.item.ElytraItem
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class ThermalKosmogliph (id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    (item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.CHESTPLATE) || item is ElytraItem
}) {
    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot
    ): Float {
        var outputDamage = damage
        if (source.isTypeIn(AstralDamageTypeTags.IS_ICE)){
            outputDamage = (outputDamage * 0.2).toFloat()
        }
        return super.modifyDamage(stack, entity, outputDamage, source, equipmentSlot)
    }
    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (slot == 2){
        if(entity is LivingEntity) {
            if (!entity.world.isClient) {
                val y = entity.statusEffects.filter { it.effectType == StatusEffects.SLOWNESS }
                if (y.isNotEmpty()) {
                    for (t in y) {
                        entity.statusEffects.remove(t)
                    }
                }
            }
        }
        entity.frozenTicks = 0}
        super.inventoryTick(stack, world, entity, slot, selected)
    }
    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

}