package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.teamvoided.astralarsenal.components.KosmogliphsComponent;

import java.util.Optional;

import static org.teamvoided.astralarsenal.util.KosmogliphsStackUtilsKt.getKosmogliphs;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @ModifyExpressionValue(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean supportsEnchantments(boolean original) {
        ItemStack stack1 = ingredientInventory.getStack(0);
        ItemStack stack2 = ingredientInventory.getStack(1);
        KosmogliphsComponent kosmogliphs = getKosmogliphs(stack1);
        boolean[] hasDisallowedEnchantments = {false};
        if (!kosmogliphs.isEmpty()) {
            ItemEnchantmentsComponent enchantmentsComponent = EnchantmentHelper.getEnchantments(stack2);
            enchantmentsComponent.getEnchantments().forEach(enchantmentHolder -> {
                Optional<RegistryKey<Enchantment>> key = enchantmentHolder.getKey();
                key.ifPresent(enchantmentRegistryKey -> kosmogliphs.forEach(kosmogliph -> {
                    if (kosmogliph.disallowedEnchantment().contains(enchantmentRegistryKey)) {
                        hasDisallowedEnchantments[0] = true;
                    }
                }));
            });
        }
        return original && !hasDisallowedEnchantments[0];
    }
}
