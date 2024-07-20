package org.teamvoided.astralarsenal.init

import arrow.core.Predicate
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.item.kosmogliph.AttributeModificationKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.DamageReductionKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.armor.*
import org.teamvoided.astralarsenal.item.kosmogliph.melee.AstralStrikeKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.melee.BlastKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.melee.CannonballKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.melee.MorterKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.tools.HammerKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.tools.SmelterKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.tools.VeinmineKosmogliph

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
    val ASTRAL_STRIKE = register("astral_strike", ::AstralStrikeKosmogliph)
    val BLAST = register("blast", ::BlastKosmogliph)
    val SLAM = register("slam", ::SlamKosmogliph)

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

    val REFLECTIVE = register(
        "reflective"
    ) { identifier: Identifier ->
        DamageReductionKosmogliph(
            identifier,
            0.2f,
            { stack -> stack.isIn(ItemTags.CHEST_ARMOR) },
            DamageTypeTags.IS_PROJECTILE
        )
    }

    fun <T : Kosmogliph> register(name: String, kosmogliphProvider: (Identifier) -> T): T =
        Registry.register(Kosmogliph.REGISTRY, AstralArsenal.id(name), kosmogliphProvider(AstralArsenal.id(name)))

    fun registerSimple(name: String, applicationPredicate: Predicate<ItemStack>): SimpleKosmogliph =
        register(name) { SimpleKosmogliph(it, applicationPredicate) }
}