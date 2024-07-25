package org.teamvoided.astralarsenal.data.gen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistrySetBuilder
import org.teamvoided.astralarsenal.AstralArsenal.LOGGER
import org.teamvoided.astralarsenal.data.gen.prov.AstralEnTranslationProvider
import org.teamvoided.astralarsenal.data.gen.prov.AstralModelProvider
import org.teamvoided.astralarsenal.data.gen.prov.AstralRecipeProvider
import org.teamvoided.astralarsenal.data.gen.tags.AstralBlockTagProvider
import org.teamvoided.astralarsenal.data.gen.tags.AstralDamageTypeTagsProvider
import org.teamvoided.astralarsenal.data.gen.tags.AstralItemTagProvider
import org.teamvoided.astralarsenal.data.registry.RegistryBootstrapper
import org.teamvoided.astralarsenal.init.AstralDamageTypes

@Suppress("unused")
object AstralArsenalData : DataGeneratorEntrypoint {
    internal val registriesToGenerate = mutableListOf<RegistryKey<out Registry<*>>>()

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

        pack.addProvider(::AstralRegistryProvider)
    }

    override fun buildRegistry(gen: RegistrySetBuilder) {
        gen.bootstrapRegistry(AstralDamageTypes)
    }

    internal fun <T> RegistrySetBuilder.bootstrapRegistry(bootstrapper: RegistryBootstrapper<T>) {
        add(bootstrapper.registryKey, bootstrapper::bootstrap)
        registriesToGenerate.add(bootstrapper.registryKey)
    }
}
