package org.teamvoided.astralarsenal.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags
import net.minecraft.registry.HolderLookup
import org.teamvoided.astralarsenal.data.tags.AstralBlockTags
import java.util.concurrent.CompletableFuture

class AstralBlockTagProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricTagProvider.BlockTagProvider(output, registriesFuture) {
    override fun configure(wrapperLookup: HolderLookup.Provider) {
        getOrCreateTagBuilder(AstralBlockTags.VEIN_MINEABLE)
            .forceAddTag(ConventionalBlockTags.ORES) // deathlord I will destroy you for this
    }
}