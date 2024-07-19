package org.teamvoided.astralarsenal.init

import arrow.core.Predicate
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.item.kosmogliph.*

@Suppress("unused")
object AstralKosmogliphs {
    val VEIN_MINER = register("vein_miner", ::VeinmineKosmogliph)
    val HAMMER = register("hammer", ::HammerKosmogliph)
    val SMELTER = register("smelter", ::SmelterKosmogliph)
    val CANNONBALL = register("cannonball", ::CannonballKosmogliph)
    val MORTER = register("morter", ::MorterKosmogliph)
    val JUMP = register("jump", ::JumpKosmogliph)
    val DASH = register("dash", ::DashKosmogliph)
    val SLIDE = register("slide", ::SlideKosmogliph)
    val ASTRAL_STRIKE = register("astral_strike", ::AstralStrikeKosmogliph)

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