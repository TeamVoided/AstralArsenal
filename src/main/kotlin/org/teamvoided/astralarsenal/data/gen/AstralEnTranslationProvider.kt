package org.teamvoided.astralarsenal.data.gen


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import java.util.concurrent.CompletableFuture

@Suppress("unused")
class AstralEnTranslationProvider(
    o: FabricDataOutput,
    r: CompletableFuture<HolderLookup.Provider>
) : FabricLanguageProvider(o, r) {
    override fun generateTranslations(lookup: HolderLookup.Provider, gen: TranslationBuilder) {
        AstralItems.items(lookup.getLookupOrThrow(RegistryKeys.ITEM))
            .distinctBy { it.translationKey }
            .forEach { gen.add(it, genLang(it.id)) }

        lookup.getLookupOrThrow(Kosmogliph.REGISTRY_KEY)
            .holders()
            .toList()
            .mapNotNull { it.value() as? SimpleKosmogliph }
            .forEach { kosmogliph ->
                gen.add(kosmogliph.id.toTranslationKey("kosmogliph.tooltip"), "Kosmogliph - ${kosmogliph.id.path.titleCase()}")
            }
    }

    private fun genLang(identifier: Identifier): String =
        identifier.path.titleCase()

    private fun String.titleCase(): String {
        return split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }
    }

    val Item.id get() = Registries.ITEM.getId(this)
    val Block.id get() = Registries.BLOCK.getId(this)
    val StatusEffect.id get() = Registries.STATUS_EFFECT.getId(this)
}