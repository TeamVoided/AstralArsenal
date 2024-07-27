package org.teamvoided.astralarsenal.init

import arrow.core.Predicate
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageType
import net.minecraft.item.ArmorItem
import net.minecraft.item.ElytraItem
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.item.kosmogliph.AttributeModificationKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.DamageModificationKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.armor.*
import org.teamvoided.astralarsenal.item.kosmogliph.melee.*
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams.*
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.*
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.strikes.*
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.trident.TridentBleedKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.trident.TridentReduceKosmogliph
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
    //val SLIDE = register("slide", ::SlideKosmogliph)
    val SLAM = register("slam", ::SlamKosmogliph)
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
            { it.isIn(ItemTags.FOOT_ARMOR) },
            EntityAttributes.GENERIC_STEP_HEIGHT,
            id.extendPath("_modifier"),
            1.0,
            EntityAttributeModifier.Operation.ADD_VALUE,
            true,
            0 // 0 is boots
        )
    }

    private val CHEST_ARMOR: Predicate<ItemStack> = {
        val item = it.item
        (item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.CHESTPLATE) || item is ElytraItem
    }

    val ANTIDOTE = registerDamageModification("antidote", 0.2f, CHEST_ARMOR, AstralDamageTypeTags.IS_MAGIC)
    val CAPACITANCE = registerDamageModification("antidote", 0.2f, CHEST_ARMOR, AstralDamageTypeTags.IS_PLASMA)
    val ENDURANCE = registerDamageModification("endurance", 0.2f, CHEST_ARMOR, AstralDamageTypeTags.IS_MELEE)
    val HEAVY = registerDamageModification("heavy", 0.2f, CHEST_ARMOR, AstralDamageTypeTags.IS_EXPLOSION)
    val REFLECTIVE = registerDamageModification("reflective", 0.2f, CHEST_ARMOR, AstralDamageTypeTags.IS_PROJECTILE)
    val SCORCH_PROOF = registerDamageModification("scorch_proof", 0.2f, CHEST_ARMOR, AstralDamageTypeTags.IS_FIRE)
    val THERMAL = registerDamageModification("thermal", 0.2f, CHEST_ARMOR, AstralDamageTypeTags.IS_ICE)

    val BASIC_RAILGUN = register("basic_railgun", ::BasicRailgunKosmogliph)
    val EXPLOSIVE_BEAM = register("explosive_beam", ::ExplosiveBeamKosmogliph)
    val RAY_OF_FROST = register("ray_of_frost", ::RayofFrostKosmogliph)
    val FLAME_THROWER = register("flame_thrower", ::FlameThrowerKosmogliph)
    val RANCID_BREW = register("rancid_brew", ::RancidBrewKosmogliph)
    val SNIPE = register("snipe", ::SnipeKosmogliph)

    val CANNONBALL_LAUNCHER = register("cannonball_launcher", ::CannonballLauncherKosmogliph)
    val SHOTGUN = register("shotgun", ::ShotgunKosmogliph)

    val ORBITAL = register("orbital", ::OrbitalKosmogliph)
    val DEVASTATE = register("devastate", ::DevastateKosmogliph)
    val LOCK_OFF = register("lock_off", ::LockOffKosmogliph)
    val TIME_BOMB = register("time_bomb", ::TimeBombKosmogliph)

    val TRIDENT_REDUCE = register("trident_reduce", ::TridentReduceKosmogliph)
    val TRIDENT_BLEED = register("trident_bleed", ::TridentBleedKosmogliph)

    fun <T : Kosmogliph> register(name: String, kosmogliphProvider: (Identifier) -> T): T =
        Registry.register(Kosmogliph.REGISTRY, AstralArsenal.id(name), kosmogliphProvider(AstralArsenal.id(name)))

    fun registerDamageModification(name: String, multiplier: Float, predicate: Predicate<ItemStack>, vararg types: TagKey<DamageType>) =
        register(name) { id -> DamageModificationKosmogliph(id, multiplier, predicate, *types) }

    fun registerSimple(name: String, applicationPredicate: Predicate<ItemStack>): SimpleKosmogliph =
        register(name) { SimpleKosmogliph(it, applicationPredicate) }
}