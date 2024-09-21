package org.teamvoided.astralarsenal.item.kosmogliph.armor.defensive

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.item.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class HeavyKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_HEAVY) }) {
    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot,
        stage: DamageModificationStage
    ): Float {
        if (stage != DamageModificationStage.POST_EFFECT) return super.modifyDamage(stack, entity, damage, source, equipmentSlot, stage)

        var outputDamage = damage
        if (source.isTypeIn(AstralDamageTypeTags.IS_EXPLOSION)) {
            outputDamage = (outputDamage * 0.1).toFloat()
        }
        return super.modifyDamage(stack, entity, outputDamage, source, equipmentSlot, stage)
    }
}