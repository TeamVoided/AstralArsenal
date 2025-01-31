package org.teamvoided.astralarsenal.kosmogliph.ranged.trident

import net.minecraft.client.item.TooltipConfig
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.text.CommonTexts
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.components.AstralRainData
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.kosmogliph.KosmogliphWithData
import java.util.function.Consumer

class AstralRainKosmogliph(id: Identifier) :
    KosmogliphWithData(id, AstralDataComponents.ASTRAL_RAIN_DATA, AstralItemTags.SUPPORTS_ASTRAL_RAIN) {
    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val charges = stack.getOrDefault(AstralDataComponents.ASTRAL_RAIN_DATA, AstralRainData.DEFAULT).charges
        if (entity.isTouchingWaterOrRain && charges < 3) {
            stack.set(AstralDataComponents.ASTRAL_RAIN_DATA, AstralRainData(3))
        }
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> = listOf(Enchantments.RIPTIDE)
    override fun modifyItemTooltip(
        stack: ItemStack, ctx: Item.TooltipContext, tooltipConsumer: Consumer<Text>, config: TooltipConfig
    ) {
        super.modifyItemTooltip(stack, ctx, tooltipConsumer, config)

        val data = stack.getOrDefault(AstralDataComponents.ASTRAL_RAIN_DATA, AstralRainData.DEFAULT)
        tooltipConsumer.accept(CommonTexts.EMPTY)

        tooltipConsumer.accept(
            Text.translatable("kosmogliph.astral_rain.charges %s/3", data.charges.toString())
                .formatted(Formatting.DARK_PURPLE)
        )
    }
}