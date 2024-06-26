package org.teamvoided.astral_arsenal.init

import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.astral_arsenal.AstralArsenal.id

object AstItems {
    // yippee, long list time
    fun init() {}
    val AMETHYST_DUST = register("amethyst_dust", Item(Item.Settings()))
    val EMERALD_DUST = register("emerald_dust", Item(Item.Settings()))
    val LAPIS_DUST = register("lapis_dust", Item(Item.Settings()))
    val QUARTZ_DUST = register("quartz_dust", Item(Item.Settings()))
    val LAZULICA_BLEND = register("lazulica_blend", Item(Item.Settings()))
    val LAZULICA = register("lazulica", Item(Item.Settings()))
    val AMERALD_BLEND = register("amerald_blend", Item(Item.Settings()))
    val AMERALD = register("amerald", Item(Item.Settings()))
    val CONCENTRATED_AMETHYST_BLEND = register("concentrated_amethyst_blend", Item(Item.Settings()))
    val AMETHYST = register("amethyst", Item(Item.Settings()))
    val KOSMIK_GEM = register("kosmic_gem", Item(Item.Settings()))

    // Done this so that you just have to add the items here, then they will be given translation provider etc.
    val AstItems = listOf(
        AMETHYST_DUST,
        EMERALD_DUST,
        LAPIS_DUST,
        QUARTZ_DUST,
        LAZULICA_BLEND,
        LAZULICA,
        AMERALD_BLEND,
        AMERALD,
        CONCENTRATED_AMETHYST_BLEND,
        AMETHYST,
        KOSMIK_GEM,
        AsBlocks.COSMIC_TABLE
    )

    fun register(id: String, item: Item): Item {
        return Registry.register(Registries.ITEM, id(id), item)
    }
}