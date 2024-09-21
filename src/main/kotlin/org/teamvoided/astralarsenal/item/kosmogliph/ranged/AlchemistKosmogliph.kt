package org.teamvoided.astralarsenal.item.kosmogliph.ranged

import arrow.core.collectionSizeOrDefault
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.item.TooltipConfig
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.PotionContentsComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectUtil
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.PotionItem
import net.minecraft.registry.RegistryKey
import net.minecraft.screen.slot.Slot
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.CommonTexts
import net.minecraft.text.Text
import net.minecraft.util.ClickType
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import java.util.*
import kotlin.jvm.optionals.getOrNull

class AlchemistKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_ALCHEMIST) }),
    BowKosmogliph {
    override fun onStackClicked(
        stack: ItemStack,
        other: ItemStack,
        slot: Slot,
        clickType: ClickType,
        player: PlayerEntity,
        reference: StackReference
    ): Boolean {
        val data = stack.get(AstralItemComponents.ALCHEMIST_DATA) ?: return false
        if (other.item !is PotionItem) return false
        val potionContent = other.get(DataComponentTypes.POTION_CONTENTS) ?: return false
        var thisPotion = data.contents
        var charges = data.charges
        if (data.charges >= 64) return false
        val expectedPotionType = if (thisPotion.isEmpty) {
            potionContent
        } else thisPotion.get()

        if (!potionsAreEqual(expectedPotionType, potionContent)) return false
        thisPotion = Optional.of(expectedPotionType)
        charges += 4

        reference.set(other.copyWithCount(other.count - 1))
        stack.set(AstralItemComponents.ALCHEMIST_DATA, Data(thisPotion, charges))

        return true
    }

    override fun preFire(
        world: ServerWorld,
        user: LivingEntity,
        hand: Hand,
        stack: ItemStack,
        projectiles: List<ItemStack>,
        speed: Float,
        divergence: Float,
        isPlayer: Boolean,
        entity: LivingEntity?
    ): Boolean {
        if (user.isInCreativeMode) return false

        val data = stack.get(AstralItemComponents.ALCHEMIST_DATA) ?: return false
        var potion = data.contents
        var charges = data.charges
        if (--charges <= 0) {
            potion = Optional.empty()
        }

        stack.set(AstralItemComponents.ALCHEMIST_DATA, Data(potion, charges.coerceAtLeast(0)))

        return false
    }

    override fun overrideArrowType(player: PlayerEntity, stack: ItemStack, original: ItemStack): ItemStack? {
        if (original.isEmpty) return null
        original.decrement(1)

        val data = stack.get(AstralItemComponents.ALCHEMIST_DATA) ?: return null
        if (data.charges <= 0 || data.contents.isEmpty) return null
        val potion = data.contents.getOrNull() ?: return null
        val tippedArrow = ItemStack(Items.TIPPED_ARROW)
        tippedArrow.set(DataComponentTypes.POTION_CONTENTS, potion)

        return tippedArrow
    }

    override fun modifyItemTooltip(
        stack: ItemStack,
        ctx: Item.TooltipContext,
        tooltip: MutableList<Text>,
        config: TooltipConfig
    ) {
        super.modifyItemTooltip(stack, ctx, tooltip, config)

        val data = stack.get(AstralItemComponents.ALCHEMIST_DATA) ?: return
        tooltip += CommonTexts.EMPTY
        if (data.charges <= 0 || data.contents.isEmpty) {
            tooltip += Text.translatable("effect.none").formatted(Formatting.DARK_PURPLE)
            return
        }
        val potion = data.contents.getOrNull() ?: return

        potion.effects.forEach { effect ->
            var text = Text.translatable(effect.translationKey)
            if (effect.amplifier > 0) {
                text = Text.translatable(
                    "potion.withAmplifier",
                    text,
                    Text.translatable("potion.potency.${effect.amplifier}")
                )
            }
            if (!effect.endsWithin(20)) {
                text = Text.translatable(
                    "potion.withDuration",
                    text,
                    StatusEffectUtil.durationToString(effect, 1f, ctx.tickRate)
                )
            }

            tooltip += text.formatted(Formatting.DARK_PURPLE)
        }

        tooltip += Text.translatable("kosmogliph.alchemist.charges", data.charges.toString())
            .formatted(Formatting.DARK_PURPLE)
    }

    class Data(
        val contents: Optional<PotionContentsComponent>,
        val charges: Int
    ) {
        companion object {
            val CODEC: Codec<Data> = RecordCodecBuilder.create { builder ->
                val group = builder.group(
                    Codec.optionalField("contents", PotionContentsComponent.CODEC, true).forGetter(Data::contents),
                    Codec.INT.fieldOf("charges").orElse(0).forGetter(Data::charges)
                )

                group.apply(builder, ::Data)
            }
        }
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.INFINITY)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

    companion object {
        fun potionsAreEqual(a: PotionContentsComponent, b: PotionContentsComponent): Boolean {
            if(a.effects.collectionSizeOrDefault(0) != b.effects.collectionSizeOrDefault(9)) return false
            a.effects.forEach{ aIt ->
                if (!b.effects.any{ bIt -> aIt.isOfType(bIt.effectType) && aIt.amplifier == bIt.amplifier })
                    return false
            }
            return true
        }
    }
}