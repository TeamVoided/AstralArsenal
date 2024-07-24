package org.teamvoided.astralarsenal.data.gen


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.Block
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph
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
            .map { it.value() }
            .forEach { kosmogliph ->
                gen.add(kosmogliph.translationKey(true), kosmogliph.translationText(true).titleCase())
                gen.add(kosmogliph.translationKey(), kosmogliph.translationText().titleCase())
            }
        //can someone make it so that the kosmogliphs have a dull purple colour in text?
        gen.add("death.attack.cannonball", "%s was dunked on by %s")

        gen.add(AstralArsenal.DEFAULT_KEY_CATEGORY, "Astral Arsenal Keys")
        keybinds.forEach { (id, name) ->
            gen.add(id.toTranslationKey("key"), name.titleCase())
        }

        gen.add("kosmogliph.alchemist.charges", "Charges: %s")
    }

    private fun genLang(identifier: Identifier): String =
        identifier.path.titleCase()

    private fun String.titleCase(): String {
        return split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }
    }

    val Item.id get() = Registries.ITEM.getId(this)
    val Block.id get() = Registries.BLOCK.getId(this)
    val StatusEffect.id get() = Registries.STATUS_EFFECT.getId(this)

    companion object {
        private val keybinds = mutableMapOf<Identifier, String>()

        fun registerKeybindForDataGen(id: Identifier, name: String) {
            if (FabricLoader.getInstance().isDevelopmentEnvironment) keybinds.putIfAbsent(id, name)
        }
    }
}