package org.teamvoided.astralarsenal.kosmogliph.logic

import net.minecraft.component.DataComponentTypes
import net.minecraft.item.*
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.item.NailCannonItem
import org.teamvoided.astralarsenal.item.TomeOfHexesItem
import org.teamvoided.astralarsenal.util.hasKosmogliph

// (ender)
// Modifies how much you are slowed down when using an item.
// 1f Player moves like normal, 0f player does not move at all.
// Vanilla is 0.2f.
// TODO Should be changed to use a data component (vlib?) & a kosmogliph modifier function
fun modifyItemUseSlowdown(stack: ItemStack): Float {
    return when (stack.item) {
        is ShieldItem -> if (stack.hasKosmogliph(AstralKosmogliphs.PARRY)) 1f else 0.3f
        is SwordItem, is AxeItem -> if (stack.hasKosmogliph(AstralKosmogliphs.DEEP_WOUNDS)) 0.4f else 1f
        is BowItem, is CrossbowItem -> 0.5f
        is NailCannonItem -> 0.8f
        is MaceItem -> 0.7f
        is PotionItem -> 0.4f
        is TomeOfHexesItem -> 0.1f
        is BrushItem -> 0f
        else -> if (stack.get(DataComponentTypes.FOOD) != null) 0.4f else 0.5f
    }
}