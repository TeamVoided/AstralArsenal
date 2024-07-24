package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.teamvoided.astralarsenal.util.UtilKt;

@Mixin(BowItem.class)
public class BowMixin {
    @WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getArrowType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"))
    public ItemStack modifyArrowType(PlayerEntity instance, ItemStack stack, Operation<ItemStack> original) {
        var originalStack = original.call(instance, stack);
        var kosmogliphs = UtilKt.getKosmogliphsOnStack(stack);
        if (kosmogliphs.isEmpty()) return originalStack;
        var kosmogliph = UtilKt.findFirstBow(kosmogliphs);
        if (kosmogliph == null) return originalStack;

        var newArrowType = kosmogliph.overrideArrowType(instance, stack, originalStack);
        if (newArrowType == null) return originalStack;

        return newArrowType;
    }
}
