package org.teamvoided.astralarsenal.init

import arrow.core.Predicate
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.item.kosmogliph.AttributeModificationKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.armor.*
import org.teamvoided.astralarsenal.item.kosmogliph.armor.defensive.*
import org.teamvoided.astralarsenal.item.kosmogliph.melee.*
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams.*
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.*
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.strikes.*
import org.teamvoided.astralarsenal.item.kosmogliph.tools.*

@Suppress("unused")
object AstralKosmogliphs {
    val VEIN_MINER = register("vein_miner", ::VeinmineKosmogliph)
    val HAMMER = register("hammer", ::HammerKosmogliph)
    val SMELTER = register("smelter", ::SmelterKosmogliph)

    val CANNONBALL = register("cannonball", ::CannonballKosmogliph)
    val MORTAR = register("mortar", ::MortarKosmogliph)

    val JUMP = register("jump", ::JumpKosmogliph)
    val DASH = register("dash", ::DashKosmogliph)
    val DODGE = register("dodge", ::DodgeKosmogliph)
    val SLIDE = register("slide", ::SlideKosmogliph)
    val SLAM = register("slam", ::SlamKosmogliph)
    val GRAPPLE = register("grapple", ::GrappleKosmogliph)
    val BEAM = register("beam", ::BeamKosmogliph)

    val ASTRAL_STRIKE = register("astral_strike", ::AstralStrikeKosmogliph)
    val FREEZE = register("freeze", ::FreezeKosmogliph)
    val FLAME_BURST = register("flame_burst", ::FlameBurstKosmogliph)
    val ASTRAL_SLASH = register("astral_slash", ::AstralSlashKosmogliph)
    val DEEP_WOUNDS = register("deep_wounds", ::DeepWoundsKosmogliph)

    val STEP_UP = register("step_up") { id ->
        AttributeModificationKosmogliph(
            id,
            { it.isIn(ItemTags.FOOT_ARMOR) },
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

    val CANNONBALL_LAUNCHER = register("cannonball_launcher", ::CannonballLauncherKosmogliph)
    val SHOTGUN = register("shotgun", ::ShotgunKosmogliph)

    val ORBITAL = register("orbital", ::OrbitalKosmogliph)
    val DEVISTATE = register("devistate", ::DevistateKosmogliph)
    val LOCK_OFF = register("lock_off", ::LockOffKosmogliph)
    val TIME_BOMB = register("time_bomb", ::TimeBombKosmogliph)

    fun <T : Kosmogliph> register(name: String, kosmogliphProvider: (Identifier) -> T): T =
        Registry.register(Kosmogliph.REGISTRY, AstralArsenal.id(name), kosmogliphProvider(AstralArsenal.id(name)))

    fun registerSimple(name: String, applicationPredicate: Predicate<ItemStack>): SimpleKosmogliph =
        register(name) { SimpleKosmogliph(it, applicationPredicate) }
}