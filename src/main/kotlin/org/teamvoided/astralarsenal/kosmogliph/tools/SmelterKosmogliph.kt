package org.teamvoided.astralarsenal.kosmogliph.tools

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.item.ItemStack
import net.minecraft.loot.LootTable
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.SingleRecipeInput
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import kotlin.jvm.optionals.getOrNull

class SmelterKosmogliph(id: Identifier) : SimpleKosmogliph(id, AstralItemTags.SUPPORTS_SMELTER) {
    override fun modifyBlockBreakLoot(
        table: LootTable,
        parameters: LootContextParameterSet,
        world: ServerWorld,
        stack: ItemStack,
        original: ObjectArrayList<ItemStack>
    ): List<ItemStack> {
        val smeltedStacks = original.map {
            val recipe =
                world.recipeManager.getFirstMatch(RecipeType.SMELTING, SingleRecipeInput(it), world).getOrNull()
            if (recipe == null) {
                it
            } else {
                val result = recipe.value.craft(SingleRecipeInput(it), world.registryManager)
                result.count = it.count
                result
            }
        }

        return smeltedStacks
    }

    override fun modifyEntityDropLoot(
        table: LootTable,
        parameters: LootContextParameterSet,
        world: ServerWorld,
        stack: ItemStack,
        original: ItemStack
    ): ItemStack = world.recipeManager
        .getFirstMatch(RecipeType.SMELTING, SingleRecipeInput(original), world)
        .getOrNull()
        ?.value?.craft(SingleRecipeInput(original), world.registryManager)
        ?.copyWithCount(original.count) ?: original

}
