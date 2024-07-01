package org.teamvoided.astralarsenal.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeCategory
import net.minecraft.registry.HolderLookup
import org.teamvoided.astralarsenal.init.AstralItems
import java.util.concurrent.CompletableFuture

class AstralRecipeProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricRecipeProvider(output, registriesFuture) {
    override fun generateRecipes(exporter: RecipeExporter) = with(exporter) {
        generateCraftingRecipes()
    }

    private fun RecipeExporter.generateCraftingRecipes() {
        shaped(AstralItems.COSMIC_TABLE)
            .pattern("DED")
            .pattern("ONO")
            .pattern("COC")
            .ingredient('D', Items.DIAMOND_BLOCK)
            .ingredient('E', Items.END_CRYSTAL)
            .ingredient('O', Items.OBSIDIAN)
            .ingredient('N', Items.NETHERITE_INGOT)
            .ingredient('C', Items.CRYING_OBSIDIAN)
            .criterion(hasItem(Items.END_CRYSTAL), conditionsFromItem(Items.END_CRYSTAL))
            .offerTo(this)
    }

    private fun shaped(
        result: ItemConvertible,
        category: RecipeCategory = RecipeCategory.MISC,
        count: Int = 1
    ): ShapedRecipeJsonFactory {
        return ShapedRecipeJsonFactory(category, result, count)
    }
}