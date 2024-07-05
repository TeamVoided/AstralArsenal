package org.teamvoided.astralarsenal.item.kosmogliph.logic

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.SingleRecipeInput
import net.minecraft.server.world.ServerWorld
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import kotlin.jvm.optionals.getOrNull

object SmelterKosmogliphLogic {
    fun smelt(
        world: ServerWorld,
        stack: ItemStack?,
        original: ObjectArrayList<ItemStack>,
    ): ObjectArrayList<ItemStack> {
        if (stack == null) return original
        if (!stack.components.contains(AstralItemComponents.KOSMOGLIPHS)) return original
        val kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS)!!
        if (!kosmogliphs.contains(AstralKosmogliphs.SMELTER)) return original

        val smeltedStacks = original.map {
            val recipe = world.recipeManager.getFirstMatch(RecipeType.SMELTING, SingleRecipeInput(it), world).getOrNull()
            if (recipe == null) {
                it
            } else {
                val result = recipe.value.craft(SingleRecipeInput(it), world.registryManager)
                result.count = it.count
                result
            }
        }

        return ObjectArrayList.of(*smeltedStacks.toTypedArray())
    }
}