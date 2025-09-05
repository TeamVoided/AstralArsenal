package org.teamvoided.astralarsenal.kosmogliph.armor.defensive

import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.ElderGuardianEntity
import net.minecraft.entity.mob.GuardianEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.components.EnduranceData
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.init.AstralEffects.BREACHED
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import kotlin.math.min

class EnduranceKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_ENDURANCE) }) {

    val TICKS_TO_CHARGE = 100
    val MAX_CHARGES = 3

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
            val effects = entity.statusEffects.filter { breached.contains(it.effectType) }
            val data = stack.getOrDefault(AstralDataComponents.ENDURANCE_DATA, EnduranceData.DEFAULT)
            var multiplyer = 0.7
            if (source.attacker !is PlayerEntity){
                multiplyer = 0.5
            }
            var charges = data.charges
            if (charges > 0) {
                multiplyer = 0.3
                if (source.attacker !is PlayerEntity){
                    multiplyer = 0.1
                }
                charges -=1
                entity.world.playSoundFromEntity(
                    entity,
                    SoundEvents.ITEM_SHIELD_BLOCK,
                    SoundCategory.PLAYERS,
                    1.0f,
                    1.2f
                )
            }
            stack.set(AstralDataComponents.ENDURANCE_DATA, EnduranceData(charges, TICKS_TO_CHARGE))
            for (effect in effects) {
                multiplyer = min(multiplyer + (((1 - multiplyer) * 0.25) * (effect.amplifier + 1)), 1.0)
            }
            outputDamage = (outputDamage * multiplyer).toFloat()
        }
        return outputDamage
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (entity is LivingEntity && entity.getEquippedStack(EquipmentSlot.CHEST) == stack) {
            val data = stack.getOrDefault(AstralDataComponents.ENDURANCE_DATA, EnduranceData.DEFAULT)
            var ticks = data.ticksTillCharge
            var charges = data.charges
            if (charges < MAX_CHARGES) {
                ticks -= 1
                if (ticks <= 0) {
                    ticks = TICKS_TO_CHARGE
                    charges++
                    world.playSoundFromEntity(
                        entity,
                        SoundEvents.ITEM_ARMOR_EQUIP_CHAIN.value(),
                        SoundCategory.PLAYERS,
                        1.0f,
                        1.0f
                    )
                }
            }
            stack.set(AstralDataComponents.ENDURANCE_DATA, EnduranceData(charges, ticks))
        }
        super.inventoryTick(stack, world, entity, slot, selected)
    }


    val breached = listOf(
        BREACHED
    )
}