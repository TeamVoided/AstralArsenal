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
import org.teamvoided.astralarsenal.item.kosmogliph.melee.*
import org.teamvoided.astralarsenal.item.kosmogliph.tools.*

@Suppress("unused")
object AstralKosmogliphs {
    val VEIN_MINER = register("vein_miner", ::VeinmineKosmogliph)
    val HAMMER = register("hammer", ::HammerKosmogliph)
    val SMELTER = register("smelter", ::SmelterKosmogliph)

    val CANNONBALL = register("cannonball", ::CannonballKosmogliph)
    val MORTER = register("morter", ::MorterKosmogliph)

    val JUMP = register("jump", ::JumpKosmogliph)
    val DASH = register("dash", ::DashKosmogliph)
    val DODGE = register("dodge", ::DodgeKosmogliph)
    val SLIDE = register("slide", ::SlideKosmogliph)
    val SLAM = register("slam", ::SlamKosmogliph)

    val ASTRAL_STRIKE = register("astral_strike", ::AstralStrikeKosmogliph)
    val FREEZE = register("freeze", ::FreezeKosmogliph)
    val FLAME_BURST = register("flame_burst", ::FlameBurstKosmogliph)

    val BOOST = register("boost") { id ->
        AttributeModificationKosmogliph(
            id,
            { it.isIn(ItemTags.FOOT_ARMOR) },
            EntityAttributes.GENERIC_JUMP_STRENGTH,
            id.extendPath("_modifier"),
            0.42,
            EntityAttributeModifier.Operation.ADD_VALUE,
            true,
            0 // 0 is boots
        )
    }
    val SCORCH_PROOF = register("scorch-proof", ::ScorchProofKosmogliph)
    val ANTIDOTE = register("antidote", ::AntidoteKosmogliph)
    val ENDURACE = register("endurance", ::EnduranceKosmogliph)
    val CAPACITANCE = register("capacitance", ::CapacitanceKosmogliph)
    val THERMAL = register("thermal", ::ThermalKosmogliph)
    val HEAVY = register("heavy", ::HeavyKosmogliph)
    val REFLECTIVE = register("reflective", ::ReflectiveKosmogliph)

    fun <T : Kosmogliph> register(name: String, kosmogliphProvider: (Identifier) -> T): T =
        Registry.register(Kosmogliph.REGISTRY, AstralArsenal.id(name), kosmogliphProvider(AstralArsenal.id(name)))

    fun registerSimple(name: String, applicationPredicate: Predicate<ItemStack>): SimpleKosmogliph =
        register(name) { SimpleKosmogliph(it, applicationPredicate) }
}