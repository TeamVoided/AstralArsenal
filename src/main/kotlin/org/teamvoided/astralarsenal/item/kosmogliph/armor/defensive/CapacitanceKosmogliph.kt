package org.teamvoided.astralarsenal.item.kosmogliph.armor.defensive

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ArmorItem
import net.minecraft.item.ElytraItem
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.entity.BeamOfLightEntity
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class CapacitanceKosmogliph(id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    (item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.CHESTPLATE) || item is ElytraItem
}) {
    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot
    ): Float {
        var outputDamage = damage
        val dmg: Int
        val random: Int
        if (source.isTypeIn(AstralDamageTypeTags.IS_PLASMA)) {
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
                snowballEntity.WINDUP = 40
                snowballEntity.DMG = dmg
                snowballEntity.trackTime = 20
                snowballEntity.owner = entity
                snowballEntity.targetEntity = source.attacker
                entity.world.spawnEntity(snowballEntity)
            }
        }
        return super.modifyDamage(stack, entity, outputDamage, source, equipmentSlot)
    }
}