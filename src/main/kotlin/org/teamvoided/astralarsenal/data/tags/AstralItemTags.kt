package org.teamvoided.astralarsenal.data.tags

import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.util.tag

object AstralItemTags {
    val ALL_TAGS = mutableSetOf<TagKey<Item>>()

    val SUPPORTS_KOSMOGLIPHS = supports("kosmogliphs")

    val SUPPORTS_VEIN_MINER = supports("vein_miner")
    val SUPPORTS_HAMMER = supports("hammer")
    val SUPPORTS_SMELTER = supports("smelter")
    val SUPPORTS_REAPER = supports("reaper")

    val SUPPORTS_CANNONBALL = supports("cannonball")
    val SUPPORTS_MORTAR = supports("mortar")

    val SUPPORTS_JUMP = supports("jump")
    val SUPPORTS_STEP_UP = supports("step_up")

    val SUPPORTS_DASH = supports("dash")
    val SUPPORTS_DODGE = supports("dodge")

    val SUPPORTS_SLAM = supports("slam")
    val SUPPORTS_ANKLE_GUARD = supports("ankle_guard")

    val SUPPORTS_ALCHEMIST = supports("alchemist")

    val SUPPORTS_ASTRAL_STRIKE = supports("astral_strike")
    val SUPPORTS_FREEZE = supports("freeze")
    val SUPPORTS_FLAME_BURST = supports("flame_burst")
    val SUPPORTS_ASTRAL_SLASH = supports("astral_slash")
    val SUPPORTS_DEEP_WOUNDS = supports("deep_wounds")

    val SUPPORTS_SCORCH_PROOF = supports("scorch_proof")
    val SUPPORTS_ANTIDOTE = supports("antidote")
    val SUPPORTS_ENDURANCE = supports("endurance")
    val SUPPORTS_CAPACITANCE = supports("capacitance")
    val SUPPORTS_THERMAL = supports("thermal")
    val SUPPORTS_HEAVY = supports("heavy")
    val SUPPORTS_REFLECTIVE = supports("reflective")

    val SUPPORTS_BASIC_RAILGUN = supports("basic_railgun")
    val SUPPORTS_EXPLOSIVE_BEAM = supports("explosive_beam")
    val SUPPORTS_RAY_OF_FROST = supports("ray_of_frost")
    val SUPPORTS_FLAME_THROWER = supports("flame_thrower")
    val SUPPORTS_RANCID_BREW = supports("rancid_brew")
    val SUPPORTS_SNIPE = supports("snipe")

    val SUPPORTS_CANNONBALL_LAUNCHER = supports("cannonball_launcher")
    val SUPPORTS_SHOTGUN = supports("shotgun")

    val SUPPORTS_ORBITAL = supports("orbital")
    val SUPPORTS_DEVASTATE = supports("devastate")
    val SUPPORTS_LOCK_OFF = supports("lock_off")
    val SUPPORTS_TIME_BOMB = supports("time_bomb")

    val SUPPORTS_TRIDENT_REDUCE = supports("trident_reduce")
    val SUPPORTS_TRIDENT_BLEED = supports("trident_bleed")


    private fun create(id: String): TagKey<Item> {
        val key = RegistryKeys.ITEM.tag(id(id))
        ALL_TAGS.add(key)
        return key
    }

    private fun supports(id: String): TagKey<Item> = create("supports/$id")
}