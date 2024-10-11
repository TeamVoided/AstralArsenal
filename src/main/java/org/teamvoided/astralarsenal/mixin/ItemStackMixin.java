package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.item.TooltipConfig;
import net.minecraft.component.DataComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TooltipAppender;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.astralarsenal.init.AstralItemComponents;

import java.util.List;
import java.util.function.Consumer;

import static org.teamvoided.astralarsenal.util.UtilKt.getKosmogliphsOnStack;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    protected abstract <T extends TooltipAppender> void appendTooltip(DataComponentType<T> componentType, Item.TooltipContext context, Consumer<Text> tooltipConsumer, TooltipConfig config);

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/DataComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/client/item/TooltipConfig;)V", ordinal = 4))
    void modifyTooltip(Item.TooltipContext context, PlayerEntity player, TooltipConfig config, CallbackInfoReturnable<List<Text>> cir,
                       @Local Consumer<Text> consumer) {
        this.appendTooltip(AstralItemComponents.KOSMOGLIPHS, context, consumer, config);

        var glyph = getKosmogliphsOnStack((ItemStack) (Object) this);
        if (!glyph.isEmpty())
            glyph.forEach(it -> it.modifyItemTooltip((ItemStack) (Object) this, context, consumer, config));

    }
}
