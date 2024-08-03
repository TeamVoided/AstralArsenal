package org.teamvoided.astralarsenal.data.gen.prov

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.*
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.*
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.AstralArsenal.id
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
            .criterion(hasItem(AstralItems.KOSMIC_GEM), conditionsFromItem(AstralItems.KOSMIC_GEM))
            .offerTo(this)

        shaped(AstralItems.RAILGUN)
            .pattern("IKI")
            .pattern("KCK")
            .pattern("IKI")
            .ingredient('I', Items.IRON_INGOT)
            .ingredient('K', Items.DIAMOND)
            .ingredient('C', Items.CROSSBOW)
            .criterion(hasItem(AstralItems.KOSMIC_GEM), conditionsFromItem(AstralItems.KOSMIC_GEM))
            .offerTo(this)

        shaped(AstralItems.ASTRAL_GREATHAMMER)
            .pattern("IKI")
            .pattern("KCK")
            .pattern("IKI")
            .ingredient('I', Items.IRON_INGOT)
            .ingredient('K', Items.DIAMOND)
            .ingredient('C', Items.DIAMOND_AXE)
            .criterion(hasItem(AstralItems.KOSMIC_GEM), conditionsFromItem(AstralItems.KOSMIC_GEM))
            .offerTo(this)

        simpleShapeless(
            AstralItems.AMERALD_BLEND,
            RecipeCategory.REDSTONE, 1,
            listOf(
                AstralItems.AMETHYST_DUST, AstralItems.EMERALD_DUST,
                AstralItems.AMETHYST_DUST, AstralItems.EMERALD_DUST,
                AstralItems.AMETHYST_DUST, AstralItems.EMERALD_DUST,
                AstralItems.AMETHYST_DUST, AstralItems.EMERALD_DUST,
                Items.DIAMOND
                )
        ).offerTo(this)

        simpleShapeless(
            AstralItems.LAZULICA_BLEND,
            RecipeCategory.REDSTONE, 1,
            listOf(
                AstralItems.LAPIS_LAZULI_DUST, AstralItems.QUARTZ_DUST,
                AstralItems.LAPIS_LAZULI_DUST, AstralItems.QUARTZ_DUST,
                AstralItems.LAPIS_LAZULI_DUST, AstralItems.QUARTZ_DUST,
                AstralItems.LAPIS_LAZULI_DUST, AstralItems.QUARTZ_DUST,
                Items.DIAMOND
                )
        ).offerTo(this)

        simpleShapeless(
            AstralItems.CONCENTRATED_AMETHYST_BLEND,
            RecipeCategory.REDSTONE, 1,
            listOf(
                AstralItems.AMETHYST_DUST, AstralItems.AMETHYST_DUST,
                AstralItems.AMETHYST_DUST, AstralItems.AMETHYST_DUST,
                AstralItems.AMETHYST_DUST, AstralItems.AMETHYST_DUST,
                AstralItems.AMETHYST_DUST, AstralItems.AMETHYST_DUST,
                Items.DIAMOND
                )
        ).offerTo(this)

        simpleShapeless(
            AstralItems.KOSMIC_GEM,
            RecipeCategory.REDSTONE, 1,
            listOf(
                AstralItems.LAZULICA, AstralItems.AMERALD,
                AstralItems.LAZULICA, AstralItems.AMERALD,
                AstralItems.LAZULICA, AstralItems.AMERALD,
                AstralItems.LAZULICA, AstralItems.AMERALD,
                Items.DIAMOND)
        ).offerTo(this)

        simpleShapeless(
            AstralItems.KOSMIC_GEM,
            RecipeCategory.REDSTONE, 1,
            listOf(
                AstralItems.LAZULICA, AstralItems.AMETHYST,
                AstralItems.LAZULICA, AstralItems.AMETHYST,
                AstralItems.LAZULICA, AstralItems.AMETHYST,
                AstralItems.LAZULICA, AstralItems.AMETHYST,
                Items.DIAMOND)
        ).offerTo(this, AstralItems.KOSMIC_GEM.id.extendPath("_amethyst"))

        simpleShapeless(
            AstralItems.AMETHYST_DUST,
            RecipeCategory.REDSTONE, 1,
            listOf(Items.AMETHYST_SHARD, Items.AMETHYST_SHARD,
                Items.AMETHYST_SHARD, Items.AMETHYST_SHARD,
                Items.AMETHYST_SHARD, Items.AMETHYST_SHARD,
                Items.AMETHYST_SHARD, Items.AMETHYST_SHARD,
                Items.AMETHYST_SHARD)

        ).offerTo(this)

        simpleShapeless(
            AstralItems.EMERALD_DUST,
            RecipeCategory.REDSTONE, 1,
            listOf(Items.EMERALD, Items.EMERALD)
        ).offerTo(this)

        simpleShapeless(
            AstralItems.LAPIS_LAZULI_DUST,
            RecipeCategory.REDSTONE, 1,
            listOf(Items.LAPIS_LAZULI, Items.LAPIS_LAZULI)
        ).offerTo(this)

        simpleShapeless(
            AstralItems.QUARTZ_DUST,
            RecipeCategory.REDSTONE, 1,
            listOf(Items.QUARTZ, Items.QUARTZ)
        ).offerTo(this)

        smelting(
            RecipeCategory.MISC,
            AstralItems.AMETHYST,
            AstralItems.CONCENTRATED_AMETHYST_BLEND,
            0.5f,
            200
        )
        blasting(
            RecipeCategory.MISC,
            AstralItems.AMETHYST,
            AstralItems.CONCENTRATED_AMETHYST_BLEND,
            0.5f,
            100
        )
        smelting(
            RecipeCategory.MISC,
            AstralItems.AMERALD,
            AstralItems.AMERALD_BLEND,
            0.5f,
            200
        )
        blasting(
            RecipeCategory.MISC,
            AstralItems.AMERALD,
            AstralItems.AMERALD_BLEND,
            0.5f,
            100
        )
        smelting(
            RecipeCategory.MISC,
            AstralItems.LAZULICA,
            AstralItems.LAZULICA_BLEND,
            0.5f,
            200
        )
        blasting(
            RecipeCategory.MISC,
            AstralItems.LAZULICA,
            AstralItems.LAZULICA_BLEND,
            0.5f,
            100
        )
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
    fun <T : AbstractCookingRecipe> RecipeExporter.genericSmelting(
        recipe: AbstractCookingRecipe.Factory<T>,
        serializer: RecipeSerializer<T>,
        category: RecipeCategory,
        inputs: ItemConvertible,
        output: ItemConvertible,
        experience: Float,
        cookingTime: Int,
        group: String,
        id: Identifier
    ) = CookingRecipeJsonFactory
        .create(Ingredient.ofItems(inputs), category, output, experience, cookingTime, serializer, recipe)
        .group(group)
        .itemCriterion(inputs)
        .itemCriterion(output)
        .offerTo(this, id)
    fun RecipeExporter.smelting(
        category: RecipeCategory, output: ItemConvertible, inout: ItemConvertible,
        experience: Float, cookingTime: Int, group: String = "", id: Identifier = id(getSmeltingItemPath(output))
    ) = genericSmelting(
        ::SmeltingRecipe, RecipeSerializer.SMELTING, category, inout, output, experience, cookingTime, group, id
    )
    fun RecipeExporter.blasting(
        category: RecipeCategory, output: ItemConvertible, inout: ItemConvertible,
        experience: Float, cookingTime: Int, group: String = "", id: Identifier = id(getBlastingItemPath(output))
    ) = genericSmelting(
        ::BlastingRecipe, RecipeSerializer.BLASTING, category, inout, output, experience, cookingTime, group, id
    )
    fun RecipeJsonFactory.itemCriterion(item: ItemConvertible): RecipeJsonFactory =
        this.criterion(hasItem(item), conditionsFromItem(item))

    val Item.id get() = Registries.ITEM.getId(this)
}