package org.teamvoided.astralarsenal.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.block.tag.AstralBlockTags
import org.teamvoided.astralarsenal.util.tag
import java.util.concurrent.CompletableFuture

class AstralBlockTagProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricTagProvider.BlockTagProvider(output, registriesFuture) {
    override fun configure(wrapperLookup: HolderLookup.Provider) {
        getOrCreateTagBuilder(AstralBlockTags.VEIN_MINEABLE)
            .forceAddTag(RegistryKeys.BLOCK.tag(Identifier.of("c", "ores")))
    }
}