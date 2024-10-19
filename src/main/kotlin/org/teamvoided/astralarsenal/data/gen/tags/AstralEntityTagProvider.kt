package org.teamvoided.astralarsenal.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.entity.EntityType
import net.minecraft.registry.HolderLookup
import org.teamvoided.astralarsenal.data.tags.AstralEntityTags
import org.teamvoided.astralarsenal.init.AstralEntities
import java.util.concurrent.CompletableFuture

class AstralEntityTagProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricTagProvider.EntityTypeTagProvider(output, registriesFuture) {
    override fun configure(wrapperLookup: HolderLookup.Provider) {
        getOrCreateTagBuilder(AstralEntityTags.MOUNTS_WITH_DASH)
            .add(EntityType.CAMEL)
            .add(EntityType.HORSE)
            .add(EntityType.SKELETON_HORSE)
            .add(EntityType.DONKEY)
            .add(EntityType.MULE)
            .add(EntityType.ZOMBIE_HORSE)

        getOrCreateTagBuilder(AstralEntityTags.PROTECTED_FROM_DEL)
            .add(EntityType.TRIDENT)
            .add(EntityType.ENDER_PEARL)
            .add(AstralEntities.NAIL_ENTITY)
    }
}