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
import org.teamvoided.astralarsenal.kosmogliph.melee.mace.*
import org.teamvoided.astralarsenal.kosmogliph.ranged.*
import org.teamvoided.astralarsenal.kosmogliph.ranged.beams.*
import org.teamvoided.astralarsenal.kosmogliph.ranged.strikes.*
import org.teamvoided.astralarsenal.kosmogliph.ranged.trident.*
import org.teamvoided.astralarsenal.kosmogliph.shield.*
import org.teamvoided.astralarsenal.kosmogliph.tools.*

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
    val MORTAR = register("mortar", ::MortarKosmogliph)

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

    val SCORCH_PROOF = register("scorch-proof", ::ScorchProofKosmogliph)
    val ANTIDOTE = register("antidote", ::AntidoteKosmogliph)
    val ENDURANCE = register("endurance", ::EnduranceKosmogliph)
    val CAPACITANCE = register("capacitance", ::CapacitanceKosmogliph)
    val THERMAL = register("thermal", ::ThermalKosmogliph)
    val HEAVY = register("heavy", ::HeavyKosmogliph)
    val REFLECTIVE = register("reflective", ::ReflectiveKosmogliph)

    val BASIC_RAILGUN = register("basic_railgun", ::BasicRailgunKosmogliph)
    val EXPLOSIVE_BEAM = register("explosive_beam", ::ExplosiveBeamKosmogliph)
    val RAY_OF_FROST = register("ray_of_frost", ::RayofFrostKosmogliph)
    val FLAME_THROWER = register("flame_thrower", ::FlameThrowerKosmogliph)
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
    val OVER_HEAT = registerSimple("over_heat",AstralItemTags.SUPPORTS_OVER_HEAT)
    val STATIC_RELEASE = registerSimple("static_release", AstralItemTags.SUPPORTS_STATIC_RELEASE)
    val TEAR = registerSimple("tear", AstralItemTags.SUPPORTS_TEAR)

    @JvmField
    val PULVERISER = register("pulveriser", ::PulveriserKosmogliph)
    val WIND_ERUPTION = register("wind_eruption", ::WindEruptionKosmogliph)

    val GENERATOR = registerSimple("generator", AstralItemTags.SUPPORTS_GENERATOR)
    val QUICKSHOT = registerSimple("quickshot", AstralItemTags.SUPPORTS_QUICKSHOT)
    val TARGET = registerSimple("target", AstralItemTags.SUPPORTS_TARGET)

    fun <T : Kosmogliph> register(name: String, kosmogliphProvider: (Identifier) -> T): T =
        Registry.register(Kosmogliph.REGISTRY, AstralArsenal.id(name), kosmogliphProvider(AstralArsenal.id(name)))

    fun registerSimple(name: String, tag: TagKey<Item>): SimpleKosmogliph = register(name) { SimpleKosmogliph(it, tag) }
    fun registerSimple(name: String, applicationPredicate: Predicate<ItemStack>): SimpleKosmogliph =
        register(name) { SimpleKosmogliph(it, applicationPredicate) }
}