package org.teamvoided.astralarsenal.menu

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Holder
import net.minecraft.registry.RegistryKeys
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.init.AstralMenus
import org.teamvoided.astralarsenal.item.components.KosmogliphsComponent
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph
import kotlin.jvm.optionals.getOrNull

class CosmicTableMenu(
    syncId: Int,
    val playerInventory: PlayerInventory,
    val data: CosmicTableData = CosmicTableData(SimpleInventory(2)),
) : ScreenHandler(AstralMenus.COSMIC_TABLE, syncId) {
    private val world = playerInventory.player.world

    init {
        addSlot(object : Slot(data.inventory, 0, 70, 55) {
            override fun canInsert(stack: ItemStack) = kosmogliphCanBeApplied(stack)
        })
        addSlot(object : Slot(data.inventory, 1, 90, 55) {
            override fun canInsert(stack: ItemStack) = isKosmikGem(stack)
        })

        for (i in 0..<3) {
            for (j in 0..<9) {
                this.addSlot(Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
            }
        }

        for (i in 0..<9) {
            this.addSlot(Slot(playerInventory, i, 8 + i * 18, 142))
        }
    }

    override fun onButtonClick(player: PlayerEntity, id: Int): Boolean {
        val applicationSlot = getSlot(0)
        val kosmicGemSlot = getSlot(1)
        if ((!kosmicGemSlot.hasStack() || !kosmicGemSlot.stack.isOf(AstralItems.KOSMIC_GEM)) && !hasKosmogliph(applicationSlot.stack)) return false
        AstralArsenal.LOGGER.info("pass")
        val stack = applicationSlot.stack
        val kosmicGemStack = kosmicGemSlot.stack


        val applicable = applicableKosmogliphs()
        val kosmogliph = applicable[id]

        val kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS) ?: return false
        val enchantments = stack.enchantments.enchantments
        val missing = missingEnchantments(kosmogliph, enchantments)
        val incompatible = incompatibleEnchantments(kosmogliph, enchantments)

        if (missing.isNotEmpty()) {
            player.sendMessage(Text.translatable("comic_table.message.missing", Text.translatable(stack.translationKey)).formatted(Formatting.RED), false)
            return false
        }

        if (incompatible.isNotEmpty()) {
            player.sendMessage(Text.translatable("comic_table.message.incompatible", Text.translatable(stack.translationKey)).formatted(Formatting.RED), false)
            return false
        }

        stack.set(AstralItemComponents.KOSMOGLIPHS, KosmogliphsComponent(setOf(kosmogliph)))
        if (kosmogliphs.isEmpty()) kosmicGemStack.count--

        return true
    }

    fun hasKosmogliph(stack: ItemStack): Boolean {
        return (stack.get(AstralItemComponents.KOSMOGLIPHS) ?: setOf()).isNotEmpty()
    }

    fun applicableKosmogliphs(): List<Kosmogliph> {
        return Kosmogliph.REGISTRY.holders()
            .toList()
            .map { it.value() }
            .filter { it.canBeAppliedTo(getSlot(0).stack) }
    }

    fun kosmogliphCanBeApplied(stack: ItemStack) = stack.get(AstralItemComponents.KOSMOGLIPHS) != null
    fun isKosmikGem(stack: ItemStack) = stack.isOf(AstralItems.KOSMIC_GEM)

    fun isIncompatible(kosmogliph: Kosmogliph, stack: ItemStack): Boolean {
        return missingEnchantments(kosmogliph, stack.enchantments.enchantments).isNotEmpty() ||
                incompatibleEnchantments(kosmogliph, stack.enchantments.enchantments).isNotEmpty()
    }

    fun createKosmogliphTooltip(kosmogliph: Kosmogliph): Text {
        val text = Text.translatable(kosmogliph.translationKey(true)).formatted(Formatting.DARK_PURPLE)

        val applicationSlot = getSlot(0)
        if (!applicationSlot.hasStack()) return text
        val stack = applicationSlot.stack
        val enchantments = stack.enchantments.enchantments
        val missing = missingEnchantments(kosmogliph, enchantments)
        val incompatible = incompatibleEnchantments(kosmogliph, enchantments)

        val color = if (missing.isNotEmpty() || incompatible.isNotEmpty()) Formatting.RED else Formatting.DARK_PURPLE

        if (missing.isNotEmpty()) {
            text.append("\n\n")
                .append(Text.translatable("cosmic_table.enchantments.missing").formatted(Formatting.GRAY))
                .append("\n  ")

            missing.forEachIndexed { i, enchantment ->
                val enchantmentText = enchantment.value().description.copy().formatted(color)
                text.append(enchantmentText)
                if (i < missing.size - 1) text.append("\n  ")
            }
        }

        if (incompatible.isNotEmpty()) {
            text.append("\n\n")
                .append(Text.translatable("cosmic_table.enchantments.incompatible").formatted(Formatting.GRAY))
                .append("\n  ")

            incompatible.forEachIndexed { i, enchantment ->
                val enchantmentText = enchantment.value().description.copy().formatted(color)
                text.append(enchantmentText)
                if (i < incompatible.size - 1) text.append("\n  ")
            }
        }

        return text
    }

    fun missingEnchantments(kosmogliph: Kosmogliph, enchantments: Set<Holder<Enchantment>>): Set<Holder<Enchantment>> {
        val missing = mutableSetOf<Holder<Enchantment>>()
        kosmogliph.requiredEnchantments().forEach { key ->
            val holder = world.registryManager.get(RegistryKeys.ENCHANTMENT).getHolder(key).getOrNull() ?: return@forEach
            if (!enchantments.contains(holder)) missing += holder
        }

        return missing
    }

    fun incompatibleEnchantments(kosmogliph: Kosmogliph, enchantments: Set<Holder<Enchantment>>): Set<Holder<Enchantment>> {
        val incompatible = mutableSetOf<Holder<Enchantment>>()
        kosmogliph.disallowedEnchantment().forEach { key ->
            val holder = world.registryManager.get(RegistryKeys.ENCHANTMENT).getHolder(key).getOrNull() ?: return@forEach
            if (enchantments.contains(holder)) incompatible += holder
        }

        return incompatible
    }

    override fun quickTransfer(
        player: PlayerEntity,
        fromIndex: Int
    ): ItemStack {
        var newStack = ItemStack.EMPTY
        val slot = slots[fromIndex]
        if (!slot.hasStack()) return newStack

        val originalStack = slot.stack
        newStack = originalStack.copy()
        if (fromIndex < data.inventory.size()) {
            if (!this.insertItem(originalStack, data.inventory.size(), slots.size, true)) {
                return ItemStack.EMPTY
            }
        } else if (!this.insertItem(originalStack, 0, data.inventory.size(), false)) {
            return ItemStack.EMPTY
        }

        if (originalStack.isEmpty) {
            slot.stack = ItemStack.EMPTY
        } else {
            slot.markDirty()
        }

        return newStack
    }

    override fun canUse(player: PlayerEntity): Boolean = true
}

data class CosmicTableData(
    val inventory: Inventory = SimpleInventory(2),
) {
    companion object {
        val PACKET_CODEC: PacketCodec<PacketByteBuf, CosmicTableData> =
            PacketCodec.create(::encode, ::decode)

        fun encode(buf: PacketByteBuf, data: CosmicTableData) {}

        fun decode(buf: PacketByteBuf): CosmicTableData {
            return CosmicTableData()
        }
    }
}
