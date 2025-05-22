package org.teamvoided.astralarsenal.init

import arrow.core.Predicate
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.kosmogliph.AttributeModificationKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.armor.*
import org.teamvoided.astralarsenal.kosmogliph.armor.defensive.*
import org.teamvoided.astralarsenal.kosmogliph.melee.*
import org.teamvoided.astralarsenal.kosmogliph.melee.mace.PulveriserKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.melee.mace.WindEruptionKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.AlchemistKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.CannonballLauncherKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.ShotgunKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.beams.*
import org.teamvoided.astralarsenal.kosmogliph.ranged.strikes.DevastateKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.strikes.LockOffKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.strikes.OrbitalKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.strikes.TimeBombKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.trident.AstralRainKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.trident.TridentBleedKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.trident.TridentReduceKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.shield.ParryKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.tools.ReaperKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.tools.SmelterKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.tools.VeinmineKosmogliph

@Suppress("unused")
object AstralKosmogliphs {
    //    val STUPID_FUCKING_GLIPH = registerSimple("stupid_fucking_gliph") { true }
    val EMPTY = registerSimple("empty") { false }

    @JvmField
    val HAMMER = registerSimple("hammer", AstralItemTags.SUPPORTS_HAMMER)
    val VEIN_MINER = register("vein_miner", ::VeinmineKosmogliph)
    val SMELTER = register("smelter", ::SmelterKosmogliph)
    val REAPER = register("reaper", ::ReaperKosmogliph)

    val CANNONBALL = register("cannonball", ::CannonballKosmogliph)

    val JUMP = register("jump", ::JumpKosmogliph)
    val DASH = register("dash", ::DashKosmogliph)
    val DODGE = register("dodge", ::DodgeKosmogliph)

    @JvmField
    val SLAM = register("slam", ::SlamKosmogliph)
    val ANKLE_GUARD = register("ankle_guard", ::AnkleGuardKosmogliph)

    val PARRY = register("parry", ::ParryKosmogliph)
    val FROST_THORNS = registerSimple("frost_thorns", AstralItemTags.SUPPORTS_FROST_THORNS)

    val ALCHEMIST = register("alchemist", ::AlchemistKosmogliph)

    val ASTRAL_STRIKE = register("astral_strike", ::AstralStrikeKosmogliph)
    val FREEZE = register("freeze", ::FreezeKosmogliph)
    val FLAME_BURST = register("flame_burst", ::FlameBurstKosmogliph)
    val ASTRAL_SLASH = register("astral_slash", ::AstralSlashKosmogliph)
    val DEEP_WOUNDS = register("deep_wounds", ::DeepWoundsKosmogliph)

    val STEP_UP = register("step_up") { id ->
        AttributeModificationKosmogliph(
            id,
            { it.isIn(AstralItemTags.SUPPORTS_STEP_UP) },
            EntityAttributes.GENERIC_STEP_HEIGHT,
            id.extendPath("_modifier"),
            1.0,
            EntityAttributeModifier.Operation.ADD_VALUE,
            true,
            0 // 0 is boots
        )
    }

    val ANTIDOTE = register("antidote", ::AntidoteKosmogliph)
    val ENDURANCE = register("endurance", ::EnduranceKosmogliph)
    val CAPACITANCE = register("capacitance", ::CapacitanceKosmogliph)
    val THERMAL = register("thermal", ::ThermalKosmogliph)
    val HEAVY = register("heavy", ::HeavyKosmogliph)
    val REFLECTIVE = register("reflective", ::ReflectiveKosmogliph)

    val BASIC_RAILGUN = register("basic_railgun", ::BasicRailgunKosmogliph)
    val EXPLOSIVE_BEAM = register("explosive_beam", ::ExplosiveBeamKosmogliph)
    val RAY_OF_FROST = register("ray_of_frost", ::RayofFrostKosmogliph)
    val RANCID_BREW = register("rancid_brew", ::RancidBrewKosmogliph)
    val SNIPE = register("snipe", ::SnipeKosmogliph)
    val RICOCHET = register("ricochet", ::RicochetKosmogliph)

    val CANNONBALL_LAUNCHER = register("cannonball_launcher", ::CannonballLauncherKosmogliph)
    val SHOTGUN = register("shotgun", ::ShotgunKosmogliph)

    val ORBITAL = register("orbital", ::OrbitalKosmogliph)
    val DEVASTATE = register("devastate", ::DevastateKosmogliph)
    val LOCK_OFF = register("lock_off", ::LockOffKosmogliph)
    val TIME_BOMB = register("time_bomb", ::TimeBombKosmogliph)

    val TRIDENT_REDUCE = register("trident_reduce", ::TridentReduceKosmogliph)
    val TRIDENT_BLEED = register("trident_bleed", ::TridentBleedKosmogliph)

    @JvmField
    val ASTRAL_RAIN = register("astral_rain", ::AstralRainKosmogliph)

    val CAPACITY = registerSimple("capacity", AstralItemTags.SUPPORTS_CAPACITY)
    val OVER_HEAT = registerSimple("over_heat", AstralItemTags.SUPPORTS_OVER_HEAT)
    val STATIC_RELEASE = registerSimple("static_release", AstralItemTags.SUPPORTS_STATIC_RELEASE)
    val TEAR = registerSimple("tear", AstralItemTags.SUPPORTS_TEAR)

    @JvmField
    val PULVERISER = register("pulveriser", ::PulveriserKosmogliph)
    val WIND_ERUPTION = register("wind_eruption", ::WindEruptionKosmogliph)

    val HEX_OF_BREACHING = registerSimple("hex_of_breaching", AstralItemTags.SUPPORTS_HEX_OF_BREACHING)
    val HEX_OF_BLAZING = registerSimple("hex_of_blazing", AstralItemTags.SUPPORTS_HEX_OF_BLAZING)
    val HEX_OF_DIMINISHING = registerSimple("hex_of_diminishing", AstralItemTags.SUPPORTS_HEX_OF_DIMINISHING)
    val HEX_OF_IMPEDING = registerSimple("hex_of_impeding", AstralItemTags.SUPPORTS_HEX_OF_IMPEDING)
    val HEX_OF_CLEANSING = registerSimple("hex_of_cleansing", AstralItemTags.SUPPORTS_HEX_OF_CLEANSING)
    val HEX_OF_WEAKENING = registerSimple("hex_of_weakening", AstralItemTags.SUPPORTS_HEX_OF_WEAKENING)
    val HEX_OF_MAGNETISING = registerSimple("hex_of_magnetising", AstralItemTags.SUPPORTS_HEX_OF_WEAKENING)

    fun <T : Kosmogliph> register(name: String, kosmogliphProvider: (Identifier) -> T): T =
        Registry.register(Kosmogliph.REGISTRY, AstralArsenal.id(name), kosmogliphProvider(AstralArsenal.id(name)))

    fun registerSimple(name: String, tag: TagKey<Item>): SimpleKosmogliph = register(name) { SimpleKosmogliph(it, tag) }
    fun registerSimple(name: String, applicationPredicate: Predicate<ItemStack>): SimpleKosmogliph =
        register(name) { SimpleKosmogliph(it, applicationPredicate) }
}