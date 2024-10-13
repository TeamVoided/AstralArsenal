package org.teamvoided.astralarsenal.kosmogliph.armor.defensive

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.ElderGuardianEntity
import net.minecraft.entity.mob.GuardianEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

class EnduranceKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_ENDURANCE) }) {

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
        if (source.isTypeIn(AstralDamageTypeTags.IS_MELEE) && source.attacker !is GuardianEntity && source.attacker !is ElderGuardianEntity) {
            outputDamage = (outputDamage * 0.35).toFloat()
        }
        return outputDamage
    }
}