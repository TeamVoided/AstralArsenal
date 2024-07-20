package org.teamvoided.astralarsenal.item.kosmogliph.armor

import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class AntidoteKosmogliph (id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.CHESTPLATE
}) {
    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot
    ): Float {
        var outputDamage = damage
        if (source.isTypeIn(AstralDamageTypeTags.IS_MAGIC)){
            outputDamage = (outputDamage * 0.2).toFloat()
        }

        return super.modifyDamage(stack, entity, outputDamage, source, equipmentSlot)
    }
    val blacklist = listOf(
        StatusEffects.INSTANT_DAMAGE,
        StatusEffects.BAD_OMEN,
        StatusEffects.TRIAL_OMEN,
        StatusEffects.RAID_OMEN,
    )

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if(entity is LivingEntity){
            if (!entity.world.isClient) {
                val x = entity.statusEffects.filter { !it.effectType.value().isBeneficial &&
                        !blacklist.contains(it.effectType)}
                if (x.isNotEmpty()) {
                    for (balls in x) {
                        entity.statusEffects.remove(balls)
                        entity.addStatusEffect(
                            StatusEffectInstance(
                            balls.effectType,
                            balls.duration - 1, balls.amplifier,
                            balls.isAmbient, balls.shouldShowParticles(), balls.shouldShowIcon()
                        )
                        )
                    }

                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected)
    }

}