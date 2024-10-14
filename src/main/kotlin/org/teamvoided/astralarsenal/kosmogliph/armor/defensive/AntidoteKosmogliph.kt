package org.teamvoided.astralarsenal.kosmogliph.armor.defensive

import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

class AntidoteKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_ANTIDOTE) }) {
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
        if (source.isTypeIn(AstralDamageTypeTags.IS_MAGIC)) {
            outputDamage = (outputDamage * 0.2).toFloat()
        }

        return outputDamage
    }

    val blacklist = listOf(
        StatusEffects.INSTANT_DAMAGE,
        StatusEffects.BAD_OMEN,
        StatusEffects.TRIAL_OMEN,
        StatusEffects.RAID_OMEN,
        AstralEffects.UNHEALABLE_DAMAGE,
        AstralEffects.HARD_DAMAGE
    )

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (slot == 2) {
            if (entity is LivingEntity) {
                if (!entity.world.isClient) {
                    val x = entity.statusEffects.filter {
                        !it.effectType.value().isBeneficial &&
                                !blacklist.contains(it.effectType) &&
                                !it.isInfinite
                    }
                    x.forEach { effect ->
                        entity.statusEffects.remove(effect)
                        entity.addStatusEffect(
                            StatusEffectInstance(
                                effect.effectType,
                                effect.duration - 1, effect.amplifier,
                                effect.isAmbient, effect.shouldShowParticles(), effect.shouldShowIcon()
                            )
                        )
                    }
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected)
    }

}