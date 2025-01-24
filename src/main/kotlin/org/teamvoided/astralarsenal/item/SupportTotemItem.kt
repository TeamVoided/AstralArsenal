package org.teamvoided.astralarsenal.item

import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import org.teamvoided.astralarsenal.components.TotemData
import org.teamvoided.astralarsenal.init.AstralDataComponents

class SupportTotemItem(settings: Settings) : Item(settings) {
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        stack.set(AstralDataComponents.TOTEM_DATA, TotemData(target.uuid))
        return super.postHit(stack, target, attacker)
    }
}