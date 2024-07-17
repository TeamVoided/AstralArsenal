package org.teamvoided.astralarsenal.init

import arrow.core.Predicate
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.item.kosmogliph.*

@Suppress("unused")
object AstralKosmogliphs {
    val VEIN_MINER = register("vein_miner", ::VeinmineKosmogliph)
    val HAMMER = register("hammer", ::HammerKosmogliph)
    val SMELTER = register("smelter", ::SmelterKosmogliph)
    val CANNONBALL = register("cannonball", ::CannonballKosmogliph)

    fun <T: Kosmogliph> register(name: String, kosmogliphProvider: (Identifier) -> T): T =
        Registry.register(Kosmogliph.REGISTRY, AstralArsenal.id(name), kosmogliphProvider(AstralArsenal.id(name)))

    fun registerSimple(name: String, applicationPredicate: Predicate<ItemStack>): SimpleKosmogliph =
        register(name) { SimpleKosmogliph(it, applicationPredicate) }
}