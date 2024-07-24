package org.teamvoided.astralarsenal.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.registry.HolderLookup
import java.util.concurrent.CompletableFuture

class AstralRegistryProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricDynamicRegistryProvider(output, registriesFuture) {
    override fun getName() = "astral_arsenal::generic_registry_provider"

    override fun configure(registries: HolderLookup.Provider, entries: Entries) {
        AstralArsenalData.registriesToGenerate.forEach { key ->
            entries.addAll(registries.getLookupOrThrow(key))
        }
    }
}