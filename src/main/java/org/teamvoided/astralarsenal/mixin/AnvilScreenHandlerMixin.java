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
import org.teamvoided.astralarsenal.init.AstralItemComponents;
import org.teamvoided.astralarsenal.components.KosmogliphsComponent;

import java.util.Optional;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @ModifyExpressionValue(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean supportsEnchantments(boolean original) {
        ItemStack inputStack1 = ingredientInventory.getStack(0);
        ItemStack inputStack2 = ingredientInventory.getStack(1);
        KosmogliphsComponent kosmogliphs = inputStack1.get(AstralItemComponents.KOSMOGLIPHS);
        boolean[] hasDisallowedEnchantments = {false};

        if (kosmogliphs != null && !kosmogliphs.isEmpty()) {
            ItemEnchantmentsComponent enchantmentsComponent = EnchantmentHelper.getEnchantments(inputStack2);
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
