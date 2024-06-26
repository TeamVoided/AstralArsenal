package org.teamvoided.astral_arsenal.init

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import org.teamvoided.astral_arsenal.AstralArsenal

object AsTabs {

    val TAB = RegistryKey.of(RegistryKeys.ITEM_GROUP, AstralArsenal.id("astral_arsenal"))

    fun init() {
        Registry.register(
            Registries.ITEM_GROUP,
            TAB,
            FabricItemGroup.builder().name(Text.translatable("itemgroup.astral_arsenal.tab")).icon {
                ItemStack(AsBlocks.COSMIC_TABLE.asItem())
            }.entries { _, entries -> entries.addItem(AsBlocks.COSMIC_TABLE) }.build()
        )
    }
}