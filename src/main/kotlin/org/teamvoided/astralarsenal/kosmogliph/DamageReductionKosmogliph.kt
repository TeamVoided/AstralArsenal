package org.teamvoided.astralarsenal.kosmogliph

import arrow.core.Predicate
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageType
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class DamageReductionKosmogliph(
    id: Identifier,
    val multiplier: Float,
    applicationPredicate: Predicate<ItemStack>,
    vararg val tags: TagKey<DamageType>
) : SimpleKosmogliph(id, applicationPredicate) {
    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot,
        stage: DamageModificationStage
    ): Float {
        if (stage != DamageModificationStage.POST_EFFECT) return super.modifyDamage(
            stack,
            entity,
            damage,
            source,
            equipmentSlot,
            stage
        )

        if (tags.any { !source.isTypeIn(it) }) return damage
        return damage * multiplier
    }
}