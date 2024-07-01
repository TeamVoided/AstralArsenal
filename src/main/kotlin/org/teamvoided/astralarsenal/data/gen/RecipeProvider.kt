package org.teamvoided.astralarsenal.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.*
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeCategory
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import org.teamvoided.astralarsenal.init.AstralItems
import java.util.concurrent.CompletableFuture

class RecipeProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) : FabricRecipeProvider(o, r) {
    override fun generateRecipes(re: RecipeExporter) {
        craftingRecipes(re)
    }
    private fun craftingRecipes(c: RecipeExporter) {
        ShapelessRecipeJsonFactory.create(RecipeCategory.REDSTONE, AstralItems.AMERALD_BLEND)
            .ingredient(AstralItems.AMETHYST_DUST)
            .ingredient(AstralItems.EMERALD_DUST)
            .criterion(hasItem(AstralItems.AMETHYST_DUST), conditionsFromItem(AstralItems.AMETHYST_DUST))
            .criterion(hasItem(AstralItems.EMERALD_DUST), conditionsFromItem(AstralItems.EMERALD_DUST))
            .offerTo(c, AstralItems.AMERALD_BLEND.id)
        ShapelessRecipeJsonFactory.create(RecipeCategory.REDSTONE, AstralItems.LAZULICA_BLEND)
            .ingredient(AstralItems.LAPIS_LAZULI_DUST)
            .ingredient(AstralItems.QUARTZ_DUST)
            .criterion(hasItem(AstralItems.LAPIS_LAZULI_DUST), conditionsFromItem(AstralItems.LAPIS_LAZULI_DUST))
            .criterion(hasItem(AstralItems.QUARTZ_DUST), conditionsFromItem(AstralItems.QUARTZ_DUST))
            .offerTo(c, AstralItems.LAZULICA_BLEND.id)
        ShapelessRecipeJsonFactory.create(RecipeCategory.REDSTONE, AstralItems.CONCENTRATED_AMETHYST_BLEND)
            .ingredient(AstralItems.AMETHYST_DUST)
            .ingredient(AstralItems.AMETHYST_DUST)
            .ingredient(AstralItems.AMETHYST_DUST)
            .ingredient(AstralItems.AMETHYST_DUST)
            .criterion(hasItem(AstralItems.AMETHYST_DUST), conditionsFromItem(AstralItems.AMETHYST_DUST))
            .offerTo(c, AstralItems.CONCENTRATED_AMETHYST_BLEND.id)
        ShapelessRecipeJsonFactory.create(RecipeCategory.REDSTONE, AstralItems.KOSMIK_GEM)
            .ingredient(AstralItems.LAZULICA)
            .ingredient(AstralItems.AMERALD)
            .ingredient(Items.DIAMOND)
            .criterion(hasItem(AstralItems.LAZULICA), conditionsFromItem(AstralItems.LAZULICA))
            .criterion(hasItem(AstralItems.AMERALD), conditionsFromItem(AstralItems.AMERALD))
            .offerTo(c, AstralItems.KOSMIK_GEM.id)
        ShapelessRecipeJsonFactory.create(RecipeCategory.REDSTONE, AstralItems.KOSMIK_GEM)
            .ingredient(AstralItems.LAZULICA)
            .ingredient(AstralItems.AMETHYST)
            .ingredient(Items.DIAMOND)
            .criterion(hasItem(AstralItems.LAZULICA), conditionsFromItem(AstralItems.LAZULICA))
            .criterion(hasItem(AstralItems.AMETHYST), conditionsFromItem(AstralItems.AMETHYST))
            .offerTo(c, AstralItems.KOSMIK_GEM.id.toString() + "_2")
        ShapelessRecipeJsonFactory.create(RecipeCategory.REDSTONE, AstralItems.AMETHYST_DUST)
            .ingredient(Items.AMETHYST_SHARD)
            .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
            .offerTo(c, AstralItems.AMETHYST_DUST.id)
        ShapelessRecipeJsonFactory.create(RecipeCategory.REDSTONE, AstralItems.EMERALD_DUST)
            .ingredient(Items.EMERALD)
            .criterion(hasItem(Items.EMERALD), conditionsFromItem(Items.EMERALD))
            .offerTo(c, AstralItems.EMERALD_DUST.id)
        ShapelessRecipeJsonFactory.create(RecipeCategory.REDSTONE, AstralItems.LAPIS_LAZULI_DUST)
            .ingredient(Items.LAPIS_LAZULI)
            .criterion(hasItem(Items.LAPIS_LAZULI), conditionsFromItem(Items.LAPIS_LAZULI))
            .offerTo(c, AstralItems.LAPIS_LAZULI_DUST.id)
        ShapelessRecipeJsonFactory.create(RecipeCategory.REDSTONE, AstralItems.QUARTZ_DUST)
            .ingredient(Items.QUARTZ)
            .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
            .offerTo(c, AstralItems.QUARTZ_DUST.id)
    }
    private fun jesseWeNeedToCook(c: RecipeExporter) {
    }

    val Item.id get() = Registries.ITEM.getId(this)
}