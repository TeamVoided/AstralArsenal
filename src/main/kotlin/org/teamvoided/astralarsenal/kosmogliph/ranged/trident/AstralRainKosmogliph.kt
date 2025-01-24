package org.teamvoided.astralarsenal.kosmogliph.ranged.trident

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.components.AstralRainData
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import java.util.*

class AstralRainKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_ASTRAL_RAIN) }) {
    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val charges = Objects.requireNonNull(stack.get(AstralDataComponents.ASTRAL_RAIN_DATA))?.charges ?: 0
        if (entity is PlayerEntity && entity.isTouchingWaterOrRain && charges < 3) {
            stack.set(AstralDataComponents.ASTRAL_RAIN_DATA, AstralRainData(3))
        }
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.RIPTIDE)
    }

}