package org.teamvoided.astralarsenal.item.kosmogliph.tools

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.item.PickaxeItem
import net.minecraft.loot.LootTable
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.SingleRecipeInput
import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import kotlin.jvm.optionals.getOrNull

class SmelterKosmogliph(id: Identifier): SimpleKosmogliph(id, { it.item is PickaxeItem }) {
    override fun modifyBlockBreakLoot(
        table: LootTable,
        parameters: LootContextParameterSet,
        world: ServerWorld,
        stack: ItemStack,
        original: ObjectArrayList<ItemStack>
    ): List<ItemStack> {
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

        return smeltedStacks
    }
    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.SILK_TOUCH)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}