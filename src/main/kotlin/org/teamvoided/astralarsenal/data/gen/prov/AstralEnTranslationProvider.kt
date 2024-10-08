package org.teamvoided.astralarsenal.data.gen.prov


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
import org.teamvoided.astralarsenal.data.tags.AstralItemTags.ALL_TAGS
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.init.AstralTabs
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
        gen.add("death.attack.cannonball", "%s was dunked on by %s")
        gen.add("death.attack.ballnt", "%s ain't ballin'")
        gen.add("death.attack.beam_of_light", "%s was delivered straight to cod by %s")
        gen.add("death.attack.railed", "%s was railed by %s")
        gen.add("death.attack.non_railed", "%s was electrocuted by %s")
        gen.add("death.attack.bleed", "%s was left out to bleed")
        gen.add("death.attack.drain", "%s drained their life")
        gen.add("death.attack.burn", "%s was given 4th degree burns by %s")
        gen.add("death.attack.boom", "%s was blown up")
        gen.add("death.attack.parry", "%s was parried by %s")
        gen.add("death.attack.richochet", "%s was electrocuted by %s")

        gen.add("death.attack.cannonball.item", "%s was dunked on by %s using %s")
        gen.add("death.attack.ballnt.item", "%s ain't ballin'")
        gen.add("death.attack.beam_of_light.item", "%s was delivered straight to cod by %s using %s")
        gen.add("death.attack.railed.item", "%s was railed by %s using %s")
        gen.add("death.attack.non_railed.item", "%s was railed by %s using %s")
        gen.add("death.attack.drain.item", "%s drained their life")
        gen.add("death.attack.burn.item", "%s was given 4th degree burns by %s using %s")
        gen.add("death.attack.boom.item", "%s was blown up by %s using %s")
        gen.add("death.attack.parry.item", "%s was parried by %s using %s")
        gen.add("death.attack.richochet.item", "%s was electrocuted by %s using %s")

        gen.add("death.attack.bleed.player", "%s bled to death while fighting %s")

        gen.add(AstralTabs.TAB, "Astral Arsenal")

        gen.add(AstralArsenal.DEFAULT_KEY_CATEGORY, "Astral Arsenal Keys")
        keybinds.forEach { (id, name) ->
            gen.add(id.toTranslationKey("key"), name.titleCase())
        }

        gen.add("kosmogliph.alchemist.charges", "Charges: %s")
        gen.add("cosmic_table.enchantments.missing", "Missing: ")
        gen.add("cosmic_table.enchantments.incompatible", "Incompatible: ")
        gen.add("comic_table.message.missing", "Your %s is missing enchantments!")
        gen.add("comic_table.message.incompatible", "Your %s has incompatible enchantments!")

        gen.add(AstralEffects.UNHEALABLE_DAMAGE.value().translationKey, "Hard Damage")
        gen.add(AstralEffects.SLAM_JUMP.value().translationKey, "Slam Jump")
        gen.add(AstralEffects.REDUCE.value().translationKey, "Reduce")
        gen.add(AstralEffects.BLEED.value().translationKey, "Bleed")
        gen.add(AstralEffects.OVERHEAL.value().translationKey, "Overheal")
        gen.add(AstralEffects.HARD_DAMAGE.value().translationKey, "Weak Hard Damage")

        gen.add("container.cosmic_table", "Cosmic Table")

        gen.add("entities.astral_arsenal.beam_of_light", "Cod")

        ALL_TAGS.forEach { gen.add(it.translationKey, genLang(it.id).titleCase("/")) }
    }

    private fun genLang(identifier: Identifier): String = identifier.path.titleCase()

    private fun String.titleCase(del: String = "_"): String {
        return split(del).joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }
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