package org.teamvoided.astralarsenal.kosmogliph.armor.defensive

import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

class ThermalKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_THERMAL) }) {
    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot,
        stage: DamageModificationStage
    ): Float {
        if (stage != DamageModificationStage.POST_ARMOR) return super.modifyDamage(
            stack,
            entity,
            damage,
            source,
            equipmentSlot,
            stage
        )

        var outputDamage = damage
        if (source.isTypeIn(AstralDamageTypeTags.IS_ICE) || source.isTypeIn(AstralDamageTypeTags.IS_FIRE)) {
            outputDamage = (outputDamage * 0.3).toFloat()
        }
        return outputDamage
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (slot == 2) {
            if(entity.frozenTicks > 0){
                entity.frozenTicks = 0
            }
            if(entity.fireTicks > 0){
                entity.fireTicks = 0
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected)
    }

}