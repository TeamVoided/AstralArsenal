package org.teamvoided.astralarsenal.init

import net.minecraft.item.Item
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterials
import net.minecraft.registry.Holder
import net.minecraft.registry.HolderLookup.RegistryLookup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Rarity
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.item.AstralGreathammerItem
import org.teamvoided.astralarsenal.item.CannonballItem
import org.teamvoided.astralarsenal.item.MorterItem
import org.teamvoided.astralarsenal.item.RailgunItem
import java.util.stream.Stream

@Suppress("unused")
object AstralItems {
    val AMETHYST_DUST = register("amethyst_dust", Item(Item.Settings()))
    val EMERALD_DUST = register("emerald_dust", Item(Item.Settings()))
    val LAPIS_LAZULI_DUST = register("lapis_lazuli_dust", Item(Item.Settings()))
    val QUARTZ_DUST = register("quartz_dust", Item(Item.Settings()))
    val LAZULICA_BLEND = register("lazulica_blend", Item(Item.Settings()))
    val LAZULICA = register("lazulica", Item(Item.Settings().rarity(Rarity.UNCOMMON)))
    val AMERALD_BLEND = register("amerald_blend", Item(Item.Settings()))
    val AMERALD = register("amerald", Item(Item.Settings().rarity(Rarity.UNCOMMON)))
    val CONCENTRATED_AMETHYST_BLEND = register("concentrated_amethyst_blend", Item(Item.Settings()))
    val AMETHYST = register("amethyst", Item(Item.Settings().rarity(Rarity.UNCOMMON)))
    val KOSMIC_GEM = register("kosmic_gem", Item(Item.Settings().rarity(Rarity.RARE)))
    val CANNONBALL = register("cannonball", CannonballItem(Item.Settings()))
    val MORTER = register("morter", MorterItem(Item.Settings()))
    val ASTRAL_GREATHAMMER = register("astral_greathammer", AstralGreathammerItem((Item.Settings()).fireproof().rarity(Rarity.EPIC).attributeModifiersComponent(SwordItem.createAttributes(ToolMaterials.NETHERITE, 5, -3.0F))))
    val RAILGUN = register("railgun", RailgunItem(Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)))

    @Deprecated(
        "Replaced with automatic fetching",
        ReplaceWith("AstralItems.items()"),
    )

    val AstItems = listOf(
        AMETHYST_DUST,
        EMERALD_DUST,
        LAPIS_LAZULI_DUST,
        QUARTZ_DUST,
        LAZULICA_BLEND,
        LAZULICA,
        AMERALD_BLEND,
        AMERALD,
        CONCENTRATED_AMETHYST_BLEND,
        AMETHYST,
        KOSMIC_GEM,
        CANNONBALL,
        MORTER,
    )

    fun items(): Set<Item> {
        return Registries.ITEM.holders().astItems()
    }

    fun items(lookup: RegistryLookup<Item>): Set<Item> {
        return lookup.holders().astItems()
    }

    private fun Stream<Holder.Reference<Item>>.astItems(): Set<Item> {
        return this.filter { it.registryKey.value.namespace == AstralArsenal.MOD_ID }
            .toList()
            .distinctBy { it.registryKey.value }
            .map { it.value() }
            .toSet()
    }

    fun <T: Item> register(id: String, item: T): T {
        return Registry.register(Registries.ITEM, id(id), item)
    }
}