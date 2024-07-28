package org.teamvoided.astralarsenal.item.kosmogliph.tools

import net.minecraft.block.BlockState
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.LivingEntity
import net.minecraft.item.*
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class HammerKosmogliph(id: Identifier) : SimpleKosmogliph(id,
    { it.item is PickaxeItem || it.item is ShovelItem || it.item is AxeItem || it.item is HoeItem }) {
    override fun postMine(stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miner: LivingEntity) {}


    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}