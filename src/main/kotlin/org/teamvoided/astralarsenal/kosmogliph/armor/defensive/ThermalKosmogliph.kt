package org.teamvoided.astralarsenal.kosmogliph.armor.defensive

import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralEffects.BREACHED
import org.teamvoided.astralarsenal.init.AstralEffects.BLAZED
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import kotlin.math.min

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
            val effects = entity.statusEffects.filter { breached.contains(it.effectType) }
            var multiplyer = 0.3
            for (effect in effects) {
                multiplyer = min(0.3 + (0.175 * (effect.amplifier + 1)), 1.0)
            }
            val blazed = entity.statusEffects.filter { blazed.contains(it.effectType) }
            if (blazed.isEmpty() || (!source.isType(AstralDamageTypes.FROZEN) && !source.isType(AstralDamageTypes.INCINERATED))) {
                outputDamage = (outputDamage * multiplyer).toFloat()
            }
            else if(source.isType(AstralDamageTypes.FROZEN) || source.isType(AstralDamageTypes.INCINERATED)){
                outputDamage = (outputDamage * 0.8).toFloat()
                }
        }
        return outputDamage
    }

    val breached = listOf(
        BREACHED
    )

    val preventers = listOf(
        BREACHED,
        BLAZED
    )

    val blazed = listOf(
        BLAZED
    )

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        var bool = true
        if (entity is LivingEntity) {
            val effects = entity.statusEffects.filter { preventers.contains(it.effectType) }
            for (effect in effects) {
                bool = false
            }
        }
        if (slot == 2 && bool) {
            if (entity.frozenTicks > 0) {
                entity.frozenTicks = 0
            }
            if (entity.fireTicks > 0) {
                entity.fireTicks = 0
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected)
    }

}