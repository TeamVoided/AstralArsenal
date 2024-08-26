package org.teamvoided.astralarsenal.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags
import net.minecraft.block.Blocks
import net.minecraft.registry.HolderLookup
import org.teamvoided.astralarsenal.data.tags.AstralBlockTags
import java.util.concurrent.CompletableFuture

class AstralBlockTagProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricTagProvider.BlockTagProvider(output, registriesFuture) {
    override fun configure(wrapperLookup: HolderLookup.Provider) {
        getOrCreateTagBuilder(AstralBlockTags.VEIN_MINEABLE)
            .forceAddTag(ConventionalBlockTags.ORES)
        getOrCreateTagBuilder(AstralBlockTags.REAPABLE_CROPS)
            // block crops
            .add(Blocks.PUMPKIN)
            .add(Blocks.MELON)
            // crop crops
            .add(Blocks.WHEAT)
            .add(Blocks.CARROTS)
            .add(Blocks.POTATOES)
            .add(Blocks.BEETROOTS)
            // special
            .add(Blocks.COCOA)
            .add(Blocks.NETHER_WART)
            .add(Blocks.SWEET_BERRY_BUSH)
    }
}