package org.teamvoided.astralarsenal.item.kosmogliph.ranged

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.item.TooltipConfig
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.PotionContentsComponent
import net.minecraft.entity.effect.StatusEffectUtil
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.BowItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.screen.slot.Slot
import net.minecraft.text.CommonTexts
import net.minecraft.text.Text
import net.minecraft.util.ClickType
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import java.util.*
import kotlin.jvm.optionals.getOrNull
import kotlin.math.floor

class AlchemistKosmogliph(id: Identifier): SimpleKosmogliph(id, { it.item is BowItem }), RangedWeaponKosmogliph {
    override fun onStackClicked(
        stack: ItemStack,
        other: ItemStack,
        slot: Slot,
        clickType: ClickType,
        player: PlayerEntity,
        reference: StackReference
    ): Boolean {
        val data = stack.get(AstralItemComponents.ALCHEMIST_DATA) ?: return false
        val potionContent = other.get(DataComponentTypes.POTION_CONTENTS) ?: return false
        var thisPotion = data.potion
        var charges = data.charges
        if (data.charges >= 24) return false
        val otherPotion = potionContent.potion.get()
        val expectedPotionType = if (thisPotion.isEmpty) {
            val key = otherPotion.key
            if (key.isEmpty) return false
            else key.get().value
        } else thisPotion.get()

        if (expectedPotionType != otherPotion.key.get().value) return false
        thisPotion = Optional.of(expectedPotionType)
        charges += 4
        charges = floor(charges)

        reference.set(other.copyWithCount(other.count - 1))
        stack.set(AstralItemComponents.ALCHEMIST_DATA, Data(thisPotion, charges))

        return true
    }

    override fun overrideArrowType(player: PlayerEntity, stack: ItemStack): ItemStack? {
        val data = stack.get(AstralItemComponents.ALCHEMIST_DATA) ?: return null
        var potion = data.potion
        var charges = data.charges
        if (charges <= 0 || potion.isEmpty) return null
        val potionInst = Registries.POTION.getHolder(potion.get()).getOrNull() ?: return null
        val tippedArrow = PotionContentsComponent.createStack(Items.TIPPED_ARROW, potionInst)
        charges -= 0.5f
        if (floor(charges) <= 0) {
            potion = Optional.empty()
        }

        stack.set(AstralItemComponents.ALCHEMIST_DATA, Data(potion, charges))
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
        if (floor(data.charges) <= 0 || data.potion.isEmpty) {
            tooltip += Text.translatable("effect.none").formatted(Formatting.DARK_PURPLE)
            return
        }

        val potion = Registries.POTION.get(data.potion.get()) ?: return
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

        tooltip += Text.translatable("kosmogliph.alchemist.charges", floor(data.charges).toInt().toString()).formatted(Formatting.DARK_PURPLE)
    }

    class Data(
        val potion: Optional<Identifier>,
        val charges: Float
    ) {
        companion object {
            val CODEC: Codec<Data> = RecordCodecBuilder.create { builder ->
                val group = builder.group(
                    Identifier.CODEC.optionalFieldOf("statusEffects").forGetter(Data::potion),
                    Codec.FLOAT.fieldOf("charges").orElse(0f).forGetter(Data::charges)
                )

                group.apply(builder, ::Data)
            }
        }
    }
}