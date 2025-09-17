package org.teamvoided.astralarsenal.util.mixin

import net.minecraft.item.ItemStack
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.util.getKosmogliphs
import org.teamvoided.astralarsenal.util.hasKosmogliph
import org.teamvoided.astralarsenal.util.hasKosmogliphs
import org.teamvoided.astralarsenal.util.setEmpty

fun grindOffGlyphs(input0: ItemStack, input1: ItemStack, output: ItemStack): ItemStack? {
    if (!output.isEmpty) {
        val glyphs = output.getKosmogliphs()
        if (!glyphs.isEmpty()) return output.setEmpty()
    } else {
        val list = mutableListOf<ItemStack>()
        if (!input0.isEmpty) list.add(input0)
        if (!input1.isEmpty) list.add(input1)
        if (list.size != 1) return null

        val newStack = list[0]
        if (newStack.hasKosmogliphs() && !newStack.hasKosmogliph(AstralKosmogliphs.EMPTY)) {
            return newStack.copy().setEmpty()
        }
    }


    return null
}