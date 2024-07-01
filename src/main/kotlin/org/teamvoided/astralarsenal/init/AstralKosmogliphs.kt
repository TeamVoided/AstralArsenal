package org.teamvoided.astralarsenal.init

import arrow.core.Predicate
import net.minecraft.item.ItemStack
import net.minecraft.item.PickaxeItem
import net.minecraft.registry.Registry
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

@Suppress("unused")
object AstralKosmogliphs {
    val VEIN_MINER = registerSimple("vein_miner") { it.item is PickaxeItem }

    fun <T: Kosmogliph> register(name: String, kosmogliph: T): T =
        Registry.register(Kosmogliph.REGISTRY, AstralArsenal.id(name), kosmogliph)

    fun registerSimple(name: String, applicationPredicate: Predicate<ItemStack>): SimpleKosmogliph =
        register(name, SimpleKosmogliph(AstralArsenal.id(name), applicationPredicate))
}