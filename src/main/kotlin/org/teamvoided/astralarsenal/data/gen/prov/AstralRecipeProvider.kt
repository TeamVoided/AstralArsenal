package org.teamvoided.astralarsenal.data.gen.prov

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeCategory
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import org.teamvoided.astralarsenal.init.AstralBlocks
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
        shaped(AstralBlocks.COSMIC_TABLE)
            .pattern("DED")
            .pattern("ONO")
            .pattern("COC")
            .ingredient('D', Items.DIAMOND_BLOCK)
            .ingredient('E', AstralItems.KOSMIC_GEM)
            .ingredient('O', Items.OBSIDIAN)
            .ingredient('N', Items.NETHERITE_INGOT)
            .ingredient('C', Items.CRYING_OBSIDIAN)
            .criterion(hasItem(Items.END_CRYSTAL), conditionsFromItem(Items.END_CRYSTAL))
            .offerTo(this)

        simpleShapeless(
            AstralItems.AMERALD_BLEND,
            RecipeCategory.REDSTONE, 1,
            listOf(AstralItems.AMETHYST_DUST, AstralItems.AMETHYST_DUST)
        ).offerTo(this)

        simpleShapeless(
            AstralItems.LAZULICA_BLEND,
            RecipeCategory.REDSTONE, 1,
            listOf(AstralItems.LAPIS_LAZULI_DUST, AstralItems.QUARTZ_DUST)
        ).offerTo(this)

        simpleShapeless(
            AstralItems.CONCENTRATED_AMETHYST_BLEND,
            RecipeCategory.REDSTONE, 1,
            listOf(AstralItems.AMETHYST_DUST, AstralItems.AMETHYST_DUST, AstralItems.AMETHYST_DUST, AstralItems.AMETHYST_DUST)
        ).offerTo(this)

        simpleShapeless(
            AstralItems.KOSMIC_GEM,
            RecipeCategory.REDSTONE, 1,
            listOf(AstralItems.LAZULICA, AstralItems.AMERALD, Items.DIAMOND)
        ).offerTo(this)

        simpleShapeless(
            AstralItems.KOSMIC_GEM,
            RecipeCategory.REDSTONE, 1,
            listOf(AstralItems.LAZULICA, AstralItems.AMETHYST, Items.DIAMOND)
        ).offerTo(this, AstralItems.KOSMIC_GEM.id.extendPath("_amethyst"))

        simpleShapeless(
            AstralItems.AMETHYST_DUST,
            RecipeCategory.REDSTONE, 1,
            listOf(Items.AMETHYST_SHARD)
        ).offerTo(this)

        simpleShapeless(
            AstralItems.EMERALD_DUST,
            RecipeCategory.REDSTONE, 1,
            listOf(Items.EMERALD)
        ).offerTo(this)

        simpleShapeless(
            AstralItems.LAPIS_LAZULI_DUST,
            RecipeCategory.REDSTONE, 1,
            listOf(Items.LAPIS_LAZULI)
        ).offerTo(this)

        simpleShapeless(
            AstralItems.QUARTZ_DUST,
            RecipeCategory.REDSTONE, 1,
            listOf(Items.QUARTZ)
        ).offerTo(this)
    }

    private fun shaped(
        result: ItemConvertible,
        category: RecipeCategory = RecipeCategory.MISC,
        count: Int = 1
    ): ShapedRecipeJsonFactory {
        return ShapedRecipeJsonFactory(category, result, count)
    }

    private fun simpleShapeless(
        result: ItemConvertible,
        category: RecipeCategory = RecipeCategory.MISC,
        count: Int = 1,
        ingredients: List<ItemConvertible>
    ): ShapelessRecipeJsonFactory {
        val factory = ShapelessRecipeJsonFactory(category, result, count)
        ingredients.forEach(factory::ingredient)
        ingredients.toSet().forEach {
            factory.criterion(hasItem(it), conditionsFromItem(it))
        }

        return factory
    }

    val Item.id get() = Registries.ITEM.getId(this)
}