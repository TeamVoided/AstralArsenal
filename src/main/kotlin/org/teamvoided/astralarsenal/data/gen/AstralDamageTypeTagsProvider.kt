package org.teamvoided.astralarsenal.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.entity.damage.DamageType
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.DamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import java.util.concurrent.CompletableFuture

class AstralDamageTypeTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<DamageType>(o, RegistryKeys.DAMAGE_TYPE, r) {

    override fun configure(wrapperLookup: HolderLookup.Provider) {
        getOrCreateTagBuilder(AstralDamageTypeTags.IS_MAGIC)
            .add(DamageTypes.DRAGON_BREATH)
            .add(DamageTypes.INDIRECT_MAGIC)
            .add(DamageTypes.STING)
            .add(DamageTypes.WITHER_SKULL)
            .add(DamageTypes.MAGIC)

        getOrCreateTagBuilder(AstralDamageTypeTags.IS_FIRE)
            .forceAddTag(DamageTypeTags.IS_FIRE)
    }
}