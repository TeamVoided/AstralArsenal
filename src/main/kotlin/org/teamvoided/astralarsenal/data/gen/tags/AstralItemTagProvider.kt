package org.teamvoided.astralarsenal.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.tag.ItemTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralItems
import java.util.concurrent.CompletableFuture

class AstralItemTagProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricTagProvider.ItemTagProvider(output, registriesFuture) {
    override fun configure(wrapperLookup: HolderLookup.Provider) {
        vanillaTags()
        enchantTags()
        kosmogliphTags()
    }

    private fun vanillaTags() {
        getOrCreateTagBuilder(ItemTags.SWORDS).add(AstralItems.ASTRAL_GREATHAMMER)
    }

    private fun enchantTags() {
        getOrCreateTagBuilder(ItemTags.FIRE_ASPECT_ENCHANTABLE).add(AstralItems.ASTRAL_GREATHAMMER)
        getOrCreateTagBuilder(ItemTags.SHARP_WEAPON_ENCHANTABLE).add(AstralItems.ASTRAL_GREATHAMMER)
        getOrCreateTagBuilder(ItemTags.WEAPON_ENCHANTABLE).add(AstralItems.ASTRAL_GREATHAMMER)
        getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE).add(AstralItems.ASTRAL_GREATHAMMER)
        getOrCreateTagBuilder(ItemTags.VANISHING_ENCHANTABLE).add(AstralItems.ASTRAL_GREATHAMMER)
        getOrCreateTagBuilder(ItemTags.SWORD_ENCHANTABLE).add(AstralItems.ASTRAL_GREATHAMMER)
    }

    private fun kosmogliphTags() {
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_VEIN_MINER).forceAddTag(ItemTags.PICKAXES)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_HAMMER)
            .forceAddTag(ItemTags.PICKAXES)
            .forceAddTag(ItemTags.SHOVELS)
            .forceAddTag(ItemTags.AXES)
            .forceAddTag(ItemTags.HOES)
            .forceAddTag(ConventionalItemTags.SHEAR_TOOLS)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_SMELTER)
            .forceAddTag(ItemTags.PICKAXES)
            .forceAddTag(ItemTags.SHOVELS)
            .forceAddTag(ItemTags.AXES)
            .forceAddTag(ItemTags.HOES)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_REAPER)
            .forceAddTag(ItemTags.HOES)

        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_CANNONBALL).add(AstralItems.ASTRAL_GREATHAMMER)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_MORTAR).add(AstralItems.ASTRAL_GREATHAMMER)


        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_JUMP).forceAddTag(ItemTags.FOOT_ARMOR)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_STEP_UP).forceAddTag(ItemTags.FOOT_ARMOR)

        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_DASH).forceAddTag(ItemTags.LEG_ARMOR)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_DODGE).forceAddTag(ItemTags.LEG_ARMOR)

        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_SLAM).forceAddTag(ItemTags.HEAD_ARMOR)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_ANKLE_GUARD).forceAddTag(ItemTags.HEAD_ARMOR)

        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_ALCHEMIST).forceAddTag(ConventionalItemTags.BOW_TOOLS)

        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_ASTRAL_STRIKE).forceAddTag(ItemTags.SWORDS)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_FREEZE)
            .forceAddTag(ItemTags.SWORDS).forceAddTag(ItemTags.AXES)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_FLAME_BURST)
            .forceAddTag(ItemTags.SWORDS).forceAddTag(ItemTags.AXES)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_ASTRAL_SLASH).forceAddTag(ItemTags.SWORDS)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_DEEP_WOUNDS)
            .forceAddTag(ItemTags.SWORDS).forceAddTag(ItemTags.AXES)

        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_SCORCH_PROOF).forceAddTag(ItemTags.CHEST_ARMOR)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_ANTIDOTE).forceAddTag(ItemTags.CHEST_ARMOR)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_ENDURANCE).forceAddTag(ItemTags.CHEST_ARMOR)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_CAPACITANCE).forceAddTag(ItemTags.CHEST_ARMOR)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_THERMAL).forceAddTag(ItemTags.CHEST_ARMOR)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_HEAVY).forceAddTag(ItemTags.CHEST_ARMOR)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_REFLECTIVE).forceAddTag(ItemTags.CHEST_ARMOR)

        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_BASIC_RAILGUN).add(AstralItems.RAILGUN)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_EXPLOSIVE_BEAM).add(AstralItems.RAILGUN)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_RAY_OF_FROST).add(AstralItems.RAILGUN)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_FLAME_THROWER).add(AstralItems.RAILGUN)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_RANCID_BREW).add(AstralItems.RAILGUN)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_SNIPE).add(AstralItems.RAILGUN)

        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_CANNONBALL_LAUNCHER).forceAddTag(ConventionalItemTags.CROSSBOW_TOOLS)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_SHOTGUN).forceAddTag(ConventionalItemTags.CROSSBOW_TOOLS)

        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_ORBITAL).forceAddTag(ConventionalItemTags.CROSSBOW_TOOLS)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_DEVASTATE).forceAddTag(ConventionalItemTags.CROSSBOW_TOOLS)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_LOCK_OFF).forceAddTag(ConventionalItemTags.CROSSBOW_TOOLS)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_TIME_BOMB).forceAddTag(ConventionalItemTags.CROSSBOW_TOOLS)

        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_TRIDENT_REDUCE).forceAddTag(ConventionalItemTags.SPEAR_TOOLS)
        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_TRIDENT_BLEED).forceAddTag(ConventionalItemTags.SPEAR_TOOLS)


        getOrCreateTagBuilder(AstralItemTags.SUPPORTS_KOSMOGLIPHS)
            .forceAddTag(AstralItemTags.SUPPORTS_VEIN_MINER)
            .forceAddTag(AstralItemTags.SUPPORTS_HAMMER)
            .forceAddTag(AstralItemTags.SUPPORTS_SMELTER)
            .forceAddTag(AstralItemTags.SUPPORTS_REAPER)
            .forceAddTag(AstralItemTags.SUPPORTS_CANNONBALL)
            .forceAddTag(AstralItemTags.SUPPORTS_MORTAR)
            .forceAddTag(AstralItemTags.SUPPORTS_JUMP)
            .forceAddTag(AstralItemTags.SUPPORTS_STEP_UP)
            .forceAddTag(AstralItemTags.SUPPORTS_DASH)
            .forceAddTag(AstralItemTags.SUPPORTS_DODGE)
            .forceAddTag(AstralItemTags.SUPPORTS_SLAM)
            .forceAddTag(AstralItemTags.SUPPORTS_ALCHEMIST)
            .forceAddTag(AstralItemTags.SUPPORTS_ASTRAL_STRIKE)
            .forceAddTag(AstralItemTags.SUPPORTS_FREEZE)
            .forceAddTag(AstralItemTags.SUPPORTS_FLAME_BURST)
            .forceAddTag(AstralItemTags.SUPPORTS_ASTRAL_SLASH)
            .forceAddTag(AstralItemTags.SUPPORTS_DEEP_WOUNDS)
            .forceAddTag(AstralItemTags.SUPPORTS_SCORCH_PROOF)
            .forceAddTag(AstralItemTags.SUPPORTS_ANTIDOTE)
            .forceAddTag(AstralItemTags.SUPPORTS_ENDURANCE)
            .forceAddTag(AstralItemTags.SUPPORTS_CAPACITANCE)
            .forceAddTag(AstralItemTags.SUPPORTS_THERMAL)
            .forceAddTag(AstralItemTags.SUPPORTS_HEAVY)
            .forceAddTag(AstralItemTags.SUPPORTS_REFLECTIVE)
            .forceAddTag(AstralItemTags.SUPPORTS_BASIC_RAILGUN)
            .forceAddTag(AstralItemTags.SUPPORTS_EXPLOSIVE_BEAM)
            .forceAddTag(AstralItemTags.SUPPORTS_RAY_OF_FROST)
            .forceAddTag(AstralItemTags.SUPPORTS_FLAME_THROWER)
            .forceAddTag(AstralItemTags.SUPPORTS_RANCID_BREW)
            .forceAddTag(AstralItemTags.SUPPORTS_SNIPE)
            .forceAddTag(AstralItemTags.SUPPORTS_CANNONBALL_LAUNCHER)
            .forceAddTag(AstralItemTags.SUPPORTS_SHOTGUN)
            .forceAddTag(AstralItemTags.SUPPORTS_ORBITAL)
            .forceAddTag(AstralItemTags.SUPPORTS_DEVASTATE)
            .forceAddTag(AstralItemTags.SUPPORTS_LOCK_OFF)
            .forceAddTag(AstralItemTags.SUPPORTS_TIME_BOMB)
            .forceAddTag(AstralItemTags.SUPPORTS_TRIDENT_REDUCE)
            .forceAddTag(AstralItemTags.SUPPORTS_TRIDENT_BLEED)
    }
}