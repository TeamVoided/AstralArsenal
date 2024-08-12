package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Holder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.util.random.RandomGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.astralarsenal.init.AstralItemComponents;

import java.util.List;
import java.util.stream.Stream;


@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerMixin {

    @SuppressWarnings("SuspiciousMethodCalls")
    @Redirect(method = "method_7637", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;generateEnchantments(Lnet/minecraft/util/random/RandomGenerator;Lnet/minecraft/item/ItemStack;ILjava/util/stream/Stream;)Ljava/util/List;"))
    private List<EnchantmentLevelEntry> filterEnchantments(RandomGenerator random, ItemStack stack, int level, Stream<Holder<Enchantment>> possibleEnchantments,
                                                           @Local(argsOnly = true) DynamicRegistryManager registryManager) {
        var newList = possibleEnchantments.toList();
        var x = EnchantmentHelper.generateEnchantments(random, stack, level, newList.stream());
        var kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS);
        if (kosmogliphs == null || kosmogliphs.isEmpty()) return x;

        var lookup = registryManager.get(RegistryKeys.ENCHANTMENT);
        var bannedEnchants = kosmogliphs.stream().flatMap(list -> list.disallowedEnchantment().stream().map(lookup::getHolderOrThrow)).toList();
        if (bannedEnchants.isEmpty()) return x;

        return EnchantmentHelper.generateEnchantments(random, stack, level, newList.stream().filter(it -> !bannedEnchants.contains(it)).toList().stream());
    }
}
