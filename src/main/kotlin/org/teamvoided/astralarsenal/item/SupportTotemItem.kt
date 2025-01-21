package org.teamvoided.astralarsenal.item

import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.dynamic.Codecs
import org.teamvoided.astralarsenal.init.AstralItemComponents
import java.util.*

class SupportTotemItem(settings: Settings) : Item(settings) {

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        stack.set(AstralItemComponents.TOTEM_DATA, TotemData(target.uuid))
        return super.postHit(stack, target, attacker)
    }

    data class TotemData(
        val target: UUID?,
    ) {
        companion object {
            val CODEC = Codecs.ESCAPED_STRING.xmap(
                { string -> TotemData(if (string.isEmpty()) null else UUID.fromString(string)) },
                { data -> data.target.toString() }
            )
        }
    }
}