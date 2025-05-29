package org.teamvoided.astralarsenal.data.gen.prov


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.Block
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.registry.Holder
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralItemTags.ALL_TAGS
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.init.AstralTabs
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.util.DEFAULT_KEY_CATEGORY
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
        gen.add("death.attack.beam_of_light", "%s was same day shipped to god by %s")
        gen.add("death.attack.railed", "%s was railed by %s")
        gen.add("death.attack.non_railed", "%s was electrocuted by %s")
        gen.add("death.attack.bleed", "%s couldn't recover their blood in time")
        gen.add("death.attack.drain", "%s drained their life")
        gen.add("death.attack.burn", "%s was seared by %s")
        gen.add("death.attack.boom", "%s was blown up by %s")
        gen.add("death.attack.parry", "%s couldn't hit %s")
        gen.add("death.attack.richochet", "%s was electrocuted by %s")
        gen.add("death.attack.nailed", "%s was nailed by %s")
        gen.add("death.attack.pulverised", "%s was pancaked by %s")
        gen.add("death.attack.frozen", "%s was frozen to the core")
        gen.add("death.attack.chilled", "%s was forced to chill by %s")
        gen.add("death.attack.incinerated", "%s was incinerated")

        gen.add("death.attack.cannonball.item", "%s was dunked on by %s using %s")
        gen.add("death.attack.ballnt.item", "%s ain't ballin'")
        gen.add("death.attack.beam_of_light.item", "%s was same day shipped to god by %s using %s")
        gen.add("death.attack.railed.item", "%s was railed by %s using %s")
        gen.add("death.attack.non_railed.item", "%s was electrocuted by %s using %s")
        gen.add("death.attack.drain.item", "%s drained their life")
        gen.add("death.attack.burn.item", "%s was seared by %s using %s")
        gen.add("death.attack.boom.item", "%s was blown up by %s using %s")
        gen.add("death.attack.parry.item", "%s couldn't handle deflections from %s's %s")
        gen.add("death.attack.richochet.item", "%s was electrocuted by %s using %s")
        gen.add("death.attack.nailed.item", "%s was nailed by %s using %s")
        gen.add("death.attack.pulverised.item", "%s was pancaked by %s using %s")
        gen.add("death.attack.chilled.item", "%s was forced to chill by %s using %s")


        gen.add("death.attack.bleed.player", "%s bled to death while fighting %s")
        gen.add("death.attack.incinerated.player", "%s was incinerated while fighting %s")
        gen.add("death.attack.frozen.player", "%s was frozen to the core while fighting %s")

        gen.add(AstralTabs.TAB, "Astral Arsenal")

        // example gen.add("kosmogliph.tooltip.astral_arsenal..desc","")
        // chestplate
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.antidote.desc",
            "Reduces damage from magic by 80%, half the time negitive effects last on you, and doubles the time positive effects last on you."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.endurance.desc",
            "Reduces damage from melee sources by 30%. Gain up to 3 'defence charges' that are consumed when melee damage is taken. defence charges increase melee damage reduction to 70%."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.capacitance.desc",
            "Reduces damage from electricity and light by 80% and stores the damage. After taking plasma damage, build up charge by taking damage, releasing all of it when either struck later, or after a cooldown."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.thermal.desc",
            "Reduces damage from fire and ice by 70% and makes it so frost and fire cant stick to you."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.heavy.desc",
            "Reduces damage from explosions by 90%."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.reflective.desc",
            "Reduces damage from certain projectiles such as arrows by 50% and has a 70% chance to fling them back entirely."
        )

        //weapons
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.astral_strike.desc",
            "Every 8th hit, strike the target with an astral beam of light for 5 damage. beam deals 3x damage to non-players."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.freeze.desc",
            "Entities hit are frozen for a short time, stacking with every hit. On kill, the entity will release ice shards."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.flame_burst.desc",
            "Entities hit are set alight for a short time, stacking with every hit. On kill, the entity will release fire balls."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.astral_slash.desc",
            "Hold right click to charge an astral blade for 6 magic damage, x2.5 on non-players. 10 second cooldown."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.deep_wounds.desc",
            "Hold right click to charge a poison blade that steals missing hp from entities and grants it to you. Gain entities missing hp on kill. 60 second cooldown."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.trident_reduce.desc",
            "On hit, give 1 level of reduce. On ranged hit, give 6. Reduce causes entities to take 5% more damage per level. Repeated hits add levels. Reduce has no effect on magic and plasma damage."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.trident_bleed.desc",
            "on hit, give 1 level of bleed. on ranged hit, give 3. Bleed causes entities to take 0.4 damage per level every second. Repeated hits add levels up to level 10."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.astral_rain.desc",
            "Stores water in a cosmic rift, letting you riptide 3 times while out of water. Recharges by entering water."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.cannonball.desc",
            "Right clicking summons a cannonball that can be struck. The cannonball deals 10 damage and bounces off 1 target. every time it hits something, it increases in damage. Cannonballs can be 'charged'. 2 second cooldown."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.alchemist.desc",
            "Right click in the inventory with a potion to store 4 potion charges. Potion charges tip the next arrow fired. Holds up to 64."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.cannonball_launcher.desc",
            "Replaces ammo with cannonballs when fired. Cannonballs deal 10 damage and bounce off of 1 entity, increasing in damage every time it hits an entity."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.shotgun.desc",
            "shoots 5 projectiles instead of one in a spread. Increases with multishot."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.orbital.desc",
            "Replaces ammo with an orbital arrow. When hitting an entity or landing it will charge up and strike for 4 damage. Deals 3x damage to non-players. 5 second cooldown."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.devastate.desc",
            "Replaces ammo with a devastate arrow. When hitting an entity or landing it will charge up and strike for 8 damage with less area than orbital. Deals 3x damage to non-players. 5 second cooldown."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.lock_off.desc",
            "Replaces ammo with a lock-off arrow. When hitting an entity or landing on the ground it will charge up and give entities inside bleed and charge leak, causing them to take damage and be unable to recharge their movement kosmogliphs. 30e second cooldown."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.time_bomb.desc",
            "Replaces ammo with a time-bomb arrow. When hitting an entity or landing on the ground it will charge up and strike for 50 damage, taking a long time to strike. Deals 3x damage to non-players. 20 second cooldown."
        )

        //other armor and shield
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.slam.desc",
            "Crouching mid-air lets you slam, taking no fall damage. Gives a short-lasting jump-boost when slamming, letting you jump 5 blocks."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.ankle_guard.desc",
            "Prevents fall and kinetic damage from taking you to less then 25% of your max hp."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.dash.desc",
            "Pressing a keybind[default R] lets you dash forward. Dashes recharge over a second. You can hold up to 3. Dashing gives immunity frames."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.dodge.desc",
            "Pressing a keybind[default R] lets you dodge in the direction you are moving. Dodges recharge over a second. You can hold up to 3. Dodging gives immunity frames."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.jump.desc",
            "Gives you 3 mid-air jumps. Jumps start recharging when you hit the ground, each take 1 second to recharge. applies jump boost."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.step_up.desc",
            "Lets you step up 1.5 blocks."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.parry.desc",
            "Holding up your shield will parry incoming attacks and projectiles, healing you for 2hp. Missing a parry will put the shield on cooldown for 5 seconds. Projectiles parried will launch an explosive in the direction you are facing. Other attacks will deal 1.25x damage onto the attacker."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.frost_thorns.desc",
            "When the shield is hit, release a set of frost thorns towards the attacker."
        )

        //tools
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.smelter.desc",
            "Automatically smelts blocks broken and mob drops."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.hammer.desc",
            "Allows you to mine a 3x3 area."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.vein_miner.desc",
            "Allows you to mine a full vein of ores."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.reaper.desc",
            "Lets you till a 3x3 area. Lets you mine a 3x3x3 volume of fully grown crops."
        )

        //railgun and nailcannon
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.unstable_discharge.desc",
            "Increases damage by 4x and decreases cooldown by 50% at the cost of dealing 5 damage to yourself and loosing 2.5 hearts of max hp for 20 seconds."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.explosive_beam.desc",
            "Changes ammo to fire an explosive beam, causing the first entity hit to explode, or the ground if it misses, dealing 15 damage and 2x damage to non-players. 30 second cooldown."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.ray_of_frost.desc",
            "Changes ammo to fire a ray of frost, dealing 7.5 ice damage and freezing entities. Spreads frost projectiles on non-player hits and deals 3x damage to them. 30 second cooldown."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.rancid_brew.desc",
            "Changes ammo to fire an enchanted beam that deals 8 damage, giving entities a set of negative effects for 15 seconds. Deals 2x damage to non-players 20 second cooldown."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.dual_caliber.desc",
            "Lets you fire two beams, one after the other with a 5 second window to fire the second shot. Each beam deals 3x as much damage. Deals 5 damage to yourself and loose 2.5 hearts of max hp for 20 seconds. 15 second cooldown."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.ricochet.desc",
            "Changes ammo to fire a bouncing beam that deals 5 damage per hit and bounces 20 times. 15 second cooldown. Maximum 15 damage on players. When it hits a block near an entity, it will home in on them. This effect will not occur on entities that have been hit twice by this beam."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.capacity.desc",
            "Doubles the capacity of the nailcannon and reduces how long it takes for each nail to charge by 30%."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.over_heat.desc",
            "After firing for 3 seconds, nails are set ablaze, dealing 0.25 fire damage instead of nail damage and setting entities on fire."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.static_release.desc",
            "Every second fire an extra nail that deals 1 electric damage. This nail does take from your supply. Electric damage causes Conductive to trigger, causing chain lightning effects."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.tear.desc",
            "Replaces Conductive with Impaled. When dealing melee damage to an impaled entity, deal 0.5 more per nail impaled into them, to a maximum of 15."
        )

        //mace :3
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.pulveriser.desc",
            "Hold right click to charge up a leap. Landing after a leap causes an explosion that deals more damage the longer its charged and has a higher radius the further you fall. Negates fall damage. 5 second cooldown."
        )
        gen.add(
            "kosmogliph.tooltip.astral_arsenal.wind_eruption.desc",
            "Hold right click to charge up an explosion. When released, explode and launch yourself upwards. Holding for longer makes it launch you further and deal more damage. 2 second cooldown."
        )

        //tome
        gen.add("kosmogliph.tooltip.astral_arsenal.hex_of_breaching.desc", "Halves the effect of your opponents defensive kosmogliphs.")
        gen.add("kosmogliph.tooltip.astral_arsenal.hex_of_blazing.desc", "Doubles the damage your opponent takes from frost and fire damage over time.")
        gen.add("kosmogliph.tooltip.astral_arsenal.hex_of_impeding.desc", "Slows your opponent by 50%.")
        gen.add("kosmogliph.tooltip.astral_arsenal.hex_of_diminishing.desc", "Slowly reduces your opponents max hp if they are not at full health.")
        gen.add("kosmogliph.tooltip.astral_arsenal.hex_of_cleansing.desc", "Tripples the rate your opponent uses positive effects.")
        gen.add("kosmogliph.tooltip.astral_arsenal.hex_of_weakening.desc", "Reduces the damage your opponent can deal by 20%.")
        gen.add("kosmogliph.tooltip.astral_arsenal.hex_of_magnetising.desc", "Attracts projectiles towards your opponent.")

        gen.add(DEFAULT_KEY_CATEGORY, "Astral Arsenal Keys")
        keybinds.forEach { (id, name) -> gen.add(id.toTranslationKey("key"), name.titleCase()) }

        gen.add("kosmogliph.alchemist.charges", "Charges: %s/64")
        gen.add("kosmogliph.astral_rain.charges", "Charges: %s/3")
        gen.add("cosmic_table.enchantments.missing", "Missing: ")
        gen.add("cosmic_table.enchantments.incompatible", "Incompatible: ")
        gen.add("kosmogliph.cosmic_table.too_many_applicable", "Too many kosmogliphs applicable!")
        gen.add("comic_table.message.missing", "Your %s is missing enchantments!")
        gen.add("comic_table.message.incompatible", "Your %s has incompatible enchantments!")

        gen.effect(AstralEffects.UNHEALABLE_DAMAGE, "Diminished Life")
        gen.effect(AstralEffects.SLAM_JUMP, "Slam Jump")
        gen.effect(AstralEffects.REDUCE, "Reduce")
        gen.effect(AstralEffects.BLEED, "Bleed")
        gen.effect(AstralEffects.OVERHEAL, "Overheal")
        gen.effect(AstralEffects.HARD_DAMAGE, "Weak Diminished Life")
        gen.effect(AstralEffects.CONDUCTIVE, "Conductive")
        gen.effect(AstralEffects.IMPALED, "Impaled")
        gen.effect(AstralEffects.IMMORTAL, "Immortal")
        gen.effect(AstralEffects.STATICALLY_SLUDGED, "Charge Leak")

        gen.effect(AstralEffects.BREACHED, "Hex of Breaching")
        gen.effect(AstralEffects.DIMINISHED, "Hex of Diminishing")
        gen.effect(AstralEffects.BLAZED, "Hex of Blazing")
        gen.effect(AstralEffects.CLEANSED, "Hex of Cleansing")
        gen.effect(AstralEffects.IMPEDED, "Hex of Impeding")
        gen.effect(AstralEffects.WEAKENED, "Hex of Weakening")
        gen.effect(AstralEffects.MAGNETISED, "Hex of Magnetising")

        gen.effect(AstralEffects.BREACHING, "Hex of Breaching")
        gen.effect(AstralEffects.DIMINISHING, "Hex of Diminishing")
        gen.effect(AstralEffects.BLAZING, "Hex of Blazing")
        gen.effect(AstralEffects.CLEANSING, "Hex of Cleansing")
        gen.effect(AstralEffects.IMPEDING, "Hex of Impeding")
        gen.effect(AstralEffects.WEAKENING, "Hex of Weakening")
        gen.effect(AstralEffects.MAGNETISING, "Hex of Magnetising")

        gen.add("container.cosmic_table", "Cosmic Table")
        gen.add("entity.astral_arsenal.beam_of_light", "Cod")
        gen.add("entity.astral_arsenal.cannonball", "cannonball")

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


        // helpers
        fun TranslationBuilder.effect(effect: Holder<StatusEffect>, name: String) =
            this.add(effect.value().translationKey, name)
    }
}