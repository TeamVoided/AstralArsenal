package org.teamvoided.astralarsenal.item.kosmogliph

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.client.item.TooltipConfig
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.item.components.KosmogliphsComponent.Companion.toComponent

interface Kosmogliph {
    fun modifyItemTooltip(stack: ItemStack, ctx: Item.TooltipContext, tooltip: MutableList<Text>, config: TooltipConfig)
    fun canBeAppliedTo(stack: ItemStack): Boolean
    fun apply(stack: ItemStack): Either<ApplicationFailure, ItemStack> = addToComponent(stack)

    fun id() = REGISTRY.getId(this)!!

    fun addToComponent(stack: ItemStack): Either<ApplicationFailure, ItemStack> {
        val kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS)
        if (kosmogliphs == null) return ApplicationFailure(Text.translatable("kosmogliph.error.missing_component")).left()
        AstralArsenal.LOGGER.info("applied thingy: ${id()}")
        val mutableClone = kosmogliphs.toMutableSet()
        mutableClone.add(this)
        stack.set(AstralItemComponents.KOSMOGLIPHS, mutableClone.toComponent())

        return stack.right()
    }

    data class ApplicationFailure(val reason: Text)

    companion object {
        val REGISTRY_KEY: RegistryKey<Registry<Kosmogliph>> = RegistryKey.ofRegistry(AstralArsenal.id("kosmogliphs"))
        val REGISTRY: Registry<Kosmogliph> = FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister()

        val CODEC = REGISTRY.codec
        val PACKET_CODEC = PacketCodecs.fromCodec(REGISTRY.codec)
    }
}
