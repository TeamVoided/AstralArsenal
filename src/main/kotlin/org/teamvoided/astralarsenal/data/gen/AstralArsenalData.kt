package org.teamvoided.astralarsenal.data.gen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.RegistrySetBuilder
import org.teamvoided.astralarsenal.AstralArsenal.LOGGER

@Suppress("unused")
class AstralArsenalData : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        LOGGER.info("Hello from DataGen")
        val pack = gen.createPack()
        pack.addProvider(::AstralEnTranslationProvider)
        pack.addProvider(::AstralModelProvider)
        pack.addProvider(::AstralRecipeProvider)

        // Tags
        pack.addProvider(::AstralBlockTagProvider)
        pack.addProvider(::AstralDamageTypeTagsProvider)
        pack.addProvider(::AstralItemTagProvider)
    }

    override fun buildRegistry(gen: RegistrySetBuilder) { }
}
