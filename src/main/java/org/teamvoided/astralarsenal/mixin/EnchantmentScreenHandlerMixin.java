package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.teamvoided.astralarsenal.init.AstralItemComponents;
import org.teamvoided.astralarsenal.item.components.KosmogliphsComponent;

@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerMixin {

    @ModifyExpressionValue(method = "onContentChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEnchantable()Z"))
    private boolean isEnchantable(boolean original, Inventory inventory) {
        ItemStack stack = inventory.getStack(0);
        KosmogliphsComponent kosmogliphs = stack.get(AstralItemComponents.INSTANCE.getKOSMOGLIPHS());
        return original && (kosmogliphs == null || kosmogliphs.isEmpty());
    }
}
