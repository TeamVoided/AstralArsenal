package org.teamvoided.astralarsenal.kosmogliph.armor.defensive

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralEffects.BREACHED
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import kotlin.math.min

class HeavyKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_HEAVY) }) {
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
        val effects = entity.statusEffects.filter { breached.contains(it.effectType) }
        var multiplyer = 0.1
        for (effect in effects){
            multiplyer = min(0.1 + (0.225 * (effect.amplifier + 1)), 1.0)
        }
        if (source.isTypeIn(AstralDamageTypeTags.IS_EXPLOSION)) {
            outputDamage = (outputDamage * multiplyer).toFloat()
        }
        return outputDamage
    }
    val breached = listOf(
        BREACHED
    )
}