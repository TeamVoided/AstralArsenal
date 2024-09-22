package org.teamvoided.astralarsenal.item.kosmogliph.armor

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.mob.ElderGuardianEntity
import net.minecraft.entity.mob.GuardianEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.item.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class AnkleGuardKosmogliph (id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_ANKLE_GUARD) }) {

    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot,
        stage: DamageModificationStage
    ): Float {
        if (stage != DamageModificationStage.POST_ENCHANT) return super.modifyDamage(stack, entity, damage, source, equipmentSlot, stage)

        var outputDamage = damage
        if (source.isTypeIn(DamageTypeTags.IS_FALL) || source.isType(DamageTypes.FLY_INTO_WALL)) {
            if(damage > entity.health && entity.health > 1){
                outputDamage = entity.health - 1
            }
            else if(damage > entity.health){
                outputDamage = 0.0f
            }
        }
        return outputDamage
    }
}