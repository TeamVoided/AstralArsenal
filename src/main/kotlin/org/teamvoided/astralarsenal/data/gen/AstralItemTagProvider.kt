package org.teamvoided.astralarsenal.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.tag.ItemTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import java.util.concurrent.CompletableFuture

class AstralItemTagProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricTagProvider.ItemTagProvider(output, registriesFuture) {
    override fun configure(wrapperLookup: HolderLookup.Provider) {
        getOrCreateTagBuilder(AstralItemTags.WEARABLE_BOOTS)
            .forceAddTag(ItemTags.FOOT_ARMOR)

        getOrCreateTagBuilder(AstralItemTags.WEARABLE_LEGGINGS)
            .forceAddTag(ItemTags.LEG_ARMOR)

        getOrCreateTagBuilder(AstralItemTags.WEARABLE_CHESTPLATES)
            .forceAddTag(ItemTags.CHEST_ARMOR)

        getOrCreateTagBuilder(AstralItemTags.WEARABLE_HELMETS)
            .forceAddTag(ItemTags.HEAD_ARMOR)

    }
}