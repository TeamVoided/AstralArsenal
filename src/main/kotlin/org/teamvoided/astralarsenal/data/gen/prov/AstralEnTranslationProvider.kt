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
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph
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
        gen.add("death.attack.bleed", "%s couldn't recover their blood in time")
        gen.add("death.attack.drain", "%s drained their life")
        gen.add("death.attack.burn", "%s was incinerated by %s")
        gen.add("death.attack.boom", "%s was blown up by %s")
        gen.add("death.attack.parry", "%s couldn't hit %s")
        gen.add("death.attack.richochet", "%s was electrocuted by %s")
        gen.add("death.attack.nailed", "%s was nailed by %s")
        gen.add("death.attack.pulverised", "%s was pancaked by %s")

        gen.add("death.attack.cannonball.item", "%s was dunked on by %s using %s")
        gen.add("death.attack.ballnt.item", "%s ain't ballin'")
        gen.add("death.attack.beam_of_light.item", "%s was delivered straight to cod by %s using %s")
        gen.add("death.attack.railed.item", "%s was railed by %s using %s")
        gen.add("death.attack.non_railed.item", "%s was railed by %s using %s")
        gen.add("death.attack.drain.item", "%s drained their life")
        gen.add("death.attack.burn.item", "%s was incinerated by %s using %s")
        gen.add("death.attack.boom.item", "%s was blown up by %s using %s")
        gen.add("death.attack.parry.item", "%s couldn't handle deflections from %s's %s")
        gen.add("death.attack.richochet.item", "%s was electrocuted by %s using %s")
        gen.add("death.attack.nailed.item", "%s was nailed by %s using %s")
        gen.add("death.attack.pulverised.item", "%s was pancaked by %s using %s")


        gen.add("death.attack.bleed.player", "%s bled to death while fighting %s")

        gen.add(AstralTabs.TAB, "Astral Arsenal")

        // example gen.add("kosmogliph.tooltip.astral_arsenal..desc","")
        // chestplate
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.scorch-proof.desc",
            "Reduces damage from fire by 80% and stops fire from sticking to you"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.antidote.desc",
            "Reduces damage from magic by 80%, half the time negitive effects last on you, and doubles the time positive effects last on you"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.endurance.desc",
            "Reduces damage from melee sources by 50%"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.capacitance.desc",
            "Reduces damage from electricity and light by 80% and stores 50% of the damage. Next time you are hit, redirect the stored damage to the attacker."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.thermal.desc",
            "Reduces damage from ice and frost by 80% and prevents slowness and frost from effecting you"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.heavy.desc",
            "Reduces damage from explosions by 90%"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.reflective.desc",
            "Reduces damage from projectiles by 50% and has a 70% chance to fling them back entirely"
        )

        //weapons
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.astral_strike.desc",
            "Every 8th hit, strike the target with an astral beam of light for 5 damage"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.freeze.desc",
            "Entities hit are frozen for a short time. On kill, the entity will release ice shards"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.flame_burst.desc",
            "Entities hit are set alight for a short time. On kill, the entity will release fire balls"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.astral_slash.desc",
            "Hold right click to charge an astral blade for 6 magic damage, x2.5 on non-players. 10 second cooldown"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.deep_wounds.desc",
            "Hold right click to charge a poison blade that steals missing hp from entities and grants it to you. Gain entities missing hp on kill. 60 second cooldown"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.trident_reduce.desc",
            "On hit, give 1 level of reduce. On ranged hit, give 6. Reduce causes entities to take 5% more damage per level"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.trident_bleed.desc",
            "on hit, give 1 level of bleed. on ranged hit, give 5. Bleed causes entities to take 0.75 damage per level every second."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.cannonball.desc",
            "Right clicking summons a cannonball that can be struck. The cannonball deals 5 damage and bounces off of targets, increasing in damage every bounce. 5 second cooldown"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.mortar.desc",
            "Right clicking summons a mortar that can be struck. When hitting an entity, the mortar explodes. 5 second cooldown"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.alchemist.desc",
            "Right click in the inventory with a potion to store 4 potion charges. Potion charges tip the next arrow fired. Holds up to 64."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.cannonball_launcher.desc",
            "Replaces ammo with cannonballs when fired. Cannonballs deal 5 damage and bounce off of entities, increasing in damage every bounce "
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.shotgun.desc",
            "shoots 5 projectiles instead of one in a spread. Increases with multishot."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.orbital.desc",
            "Replaces ammo with an orbital arrow. When hitting an entity or landing it will charge up and strike for 4 damage. 5 second cooldown"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.devastate.desc",
            "Replaces ammo with a devastate arrow. When hitting an entity or landing it will charge up and strike for 8 damage with less area than orbital. 5 second cooldown"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.lock_off.desc",
            "Replaces ammo with a lock-off arrow. When hitting an entity or landing on the ground it will charge up and begin reducing the max hp of entities inside. 5 second cooldown"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.time_bomb.desc",
            "Replaces ammo with a time-bomb arrow. When hitting an entity or landing on the ground it will charge up and strike for 50 damage, taking a long time to strike. 20 second cooldown"
        )

        //other armor and shield
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.slam.desc",
            "Crouching mid-air lets you slam, taking no fall damage. Gives a short-lasting jump-boost when slamming, letting you jump higher then you fell from"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.ankle_guard.desc",
            "Prevents fall damage from taking you to less then 1hp"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.dash.desc",
            "Pressing a keybind[default R] lets you dash forward. Dashes recharge over a second. You can hold up to 3"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.dodge.desc",
            "Pressing a keybind[default R] lets you dodge in the direction you are moving. Dodges recharge over a second. You can hold up to 3"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.jump.desc",
            "Gives you 3 mid-air jumps. Jumps start recharging when you hit the ground, each take 1 second to recharge"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.step_up.desc",
            "Lets you step up 1.5 blocks"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.parry.desc",
            "For the first quarter second of holding your shield up, you can parry. Parrying projectiles launches them back, causing them to explode. Parrying melee attacks causes 1.25x the damage to hit the attacker"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.frost_thorns.desc",
            "When the shield is hit, release a set of frost thorns towards the attacker"
        )

        //tools
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.smelter.desc",
            "Automatically smelts blocks broken and mob drops"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.hammer.desc",
            "Allows you to mine a 3x3 area"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.vein_miner.desc",
            "Allows you to mine a full vein of ores"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.reaper.desc",
            "Lets you till a 3x3 area. Lets you mine a 3x3x3 volume of fully grown crops"
        )

        //railgun and nailcannon
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.basic_railgun.desc",
            "Fires a piercing beam, dealing 10 electric damage. costs 5hp to fire and takes 5 of your max hp for 20 seconds. 15 second cooldown"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.explosive_beam.desc",
            "Fires an explosive beam, causing the first entity it hits to explode, or the ground if it misses. 30 second cooldown"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.ray_of_frost.desc",
            "Fires a ray of frost, dealing 7.5 ice damage and freezing entities. 30 second cooldown"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.flame_thrower.desc",
            "Fires a constant stream of fire, dealing 0.25 damage per hit and setting entities on fire"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.rancid_brew.desc",
            "Fire an enchanted beam that deals 8 damage, giving entities a set of negative effects for 15 seconds, and you a set of negitive effects for 5 seconds. 20 second cooldown"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.snipe.desc",
            "Fires a piercing beam, dealing 7.5 damage. After one second, get a 5 second window to fire again. costs 5hp to fire the first shot and takes 5 of your max hp for 20 seconds. 15 second cooldown"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.ricochet.desc",
            "Fires a bouncing beam that deals 1 damage per hit and bounces 20 times. 15 second cooldown."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.capacity.desc",
            "Doubles the capacity of the nailcannon and reduces how long it takes for each nail to charge by 30%"
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.over_heat.desc",
            "After firing for 3 seconds, nails are set ablaze, dealing 0.5 fire damage instead of nail damage and setting entities on fire."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.static_release.desc",
            "Every second fire an extra nail that deals 1 electric damage. This nail does take from your supply."
        )

        //mace :3
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.pulveriser.desc",
            "Hold right click to charge up a leap. Landing after a leap causes an explosion that deals more damage the longer its charged and has a higher radius the further you fall. Negates fall damage. 10 second cooldown."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.wind_eruption.desc",
            "Hold right click to charge up an explosion. When released, explode and launch yourself upwards. Holding for longer makes it launch you further and deal more damage. 5 second cooldown."
        )

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
        gen.add(AstralEffects.CONDUCTIVE.value().translationKey, "Conductive")

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