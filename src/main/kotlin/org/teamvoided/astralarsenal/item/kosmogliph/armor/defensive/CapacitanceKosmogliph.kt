package org.teamvoided.astralarsenal.item.kosmogliph.armor.defensive

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.ElderGuardianEntity
import net.minecraft.entity.mob.GuardianEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.BeamOfLightEntity
import org.teamvoided.astralarsenal.item.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class CapacitanceKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_CAPACITANCE) }) {
    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot,
        stage: DamageModificationStage
    ): Float {
        if (stage != DamageModificationStage.POST_ARMOR) return super.modifyDamage(stack, entity, damage, source, equipmentSlot, stage)

        var outputDamage = damage
        val dmg: Int
        val random: Int
        if (source.isTypeIn(AstralDamageTypeTags.IS_PLASMA) || source.attacker is GuardianEntity || source.attacker is ElderGuardianEntity) {
            outputDamage = (outputDamage * 0.2).toFloat()
            dmg = (damage * 0.8).toInt()
            random = 1
        } else {
            dmg = (damage * 0.8).toInt()
            random = entity.world.random.range(1, 5)
        }
        if (source.attacker != null && source.attacker != entity) {
            if (random == 1) {
                val snowballEntity = BeamOfLightEntity(entity.world, entity)
                snowballEntity.setPosition(entity.pos)
                snowballEntity.DOT = false
                snowballEntity.side = 2
                snowballEntity.THRUST = 0.0
                snowballEntity.TIMEACTIVE = 10
                snowballEntity.WINDUP = 1
                snowballEntity.DMG = dmg
                snowballEntity.trackTime = 1
                snowballEntity.hard_damage = damage.times(0.2).toInt()
                snowballEntity.owner = entity
                snowballEntity.targetEntity = source.attacker
                entity.world.spawnEntity(snowballEntity)
            }
        }
        return super.modifyDamage(stack, entity, outputDamage, source, equipmentSlot, stage)
    }
}