package org.teamvoided.astralarsenal.init

import arrow.core.Predicate
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.kosmogliph.AttributeModificationKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.armor.*
import org.teamvoided.astralarsenal.kosmogliph.armor.defensive.*
import org.teamvoided.astralarsenal.kosmogliph.melee.*
import org.teamvoided.astralarsenal.kosmogliph.ranged.AlchemistKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.CannonballLauncherKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.ShotgunKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.beams.*
import org.teamvoided.astralarsenal.kosmogliph.ranged.nailgun.CapacityKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.nailgun.OverHeatKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.nailgun.StaticReleaseKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.strikes.DevastateKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.strikes.LockOffKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.strikes.OrbitalKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.strikes.TimeBombKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.trident.TridentBleedKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.trident.TridentReduceKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.shield.FrostThornsKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.shield.ParryKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.tools.HammerKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.tools.ReaperKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.tools.SmelterKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.tools.VeinmineKosmogliph

@Suppress("unused")
object AstralKosmogliphs {
    val VEIN_MINER = register("vein_miner", ::VeinmineKosmogliph)
    val HAMMER = register("hammer", ::HammerKosmogliph)
    val SMELTER = register("smelter", ::SmelterKosmogliph)
    val REAPER = register("reaper", ::ReaperKosmogliph)

    val CANNONBALL = register("cannonball", ::CannonballKosmogliph)
    val MORTAR = register("mortar", ::MortarKosmogliph)

    val JUMP = register("jump", ::JumpKosmogliph)
    val DASH = register("dash", ::DashKosmogliph)
    val DODGE = register("dodge", ::DodgeKosmogliph)

    //val SLIDE = register("slide", ::SlideKosmogliph)
    val SLAM = register("slam", ::SlamKosmogliph)
    val ANKLE_GUARD = register("ankle_guard", ::AnkleGuardKosmogliph)

    val PARRY = register("parry", ::ParryKosmogliph)
    val FROST_THORNS = register("frost_thorns", ::FrostThornsKosmogliph)

    // val GRAPPLE = register("grapple", ::GrappleKosmogliph)
    // val BEAM = register("beam", ::BeamKosmogliph)
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
    //2.0.0 content
    // val BLACK_HOLE = register("black_hole", ::BlackHoleKosmogliph)

    val TRIDENT_REDUCE = register("trident_reduce", ::TridentReduceKosmogliph)
    val TRIDENT_BLEED = register("trident_bleed", ::TridentBleedKosmogliph)

    val CAPACITY = register("capacity", ::CapacityKosmogliph)
    val OVER_HEAT = register("over_heat", ::OverHeatKosmogliph)
    val STATIC_RELEASE = register("static_release", ::StaticReleaseKosmogliph)

    fun <T : Kosmogliph> register(name: String, kosmogliphProvider: (Identifier) -> T): T =
        Registry.register(Kosmogliph.REGISTRY, AstralArsenal.id(name), kosmogliphProvider(AstralArsenal.id(name)))

    fun registerSimple(name: String, applicationPredicate: Predicate<ItemStack>): SimpleKosmogliph =
        register(name) { SimpleKosmogliph(it, applicationPredicate) }
}