package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroup.DisplayParameters
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import org.teamvoided.astralarsenal.AstralArsenal

@Suppress("unused", "unused_parameter")
object AstralTabs {
    val TAB = register("tab", AstralItems.KOSMIC_GEM, ::tabESB)

    fun tabESB(displayParameters: DisplayParameters): Set<ItemConvertible> {
        return AstralItems.items()
    }

    fun register(name: String, icon: ItemConvertible, entrySetBuilder: EntrySetBuilder): RegistryKey<ItemGroup> {
        val id = AstralArsenal.id(name)
        val key = RegistryKey.of(RegistryKeys.ITEM_GROUP, id)

        Registry.register(
            Registries.ITEM_GROUP, key,
            FabricItemGroup.builder()
                .name(Text.translatable(id.toTranslationKey("itemgroup")))
                .icon { icon.asItem().defaultStack }
                .entries { displayParameters, collector ->
                    entrySetBuilder.createEntrySet(displayParameters).forEach(collector::addItem)
                }.build()
        )

        return key
    }

    fun interface EntrySetBuilder {
        fun createEntrySet(parameters: DisplayParameters): Set<ItemConvertible>
    }
}