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

import java.util.List;
import java.util.stream.Stream;

import static org.teamvoided.astralarsenal.util.KosmogliphsStackUtilsKt.getKosmogliphs;


@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerMixin {

    @SuppressWarnings("SuspiciousMethodCalls")
    @Redirect(method = "method_7637", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;generateEnchantments(Lnet/minecraft/util/random/RandomGenerator;Lnet/minecraft/item/ItemStack;ILjava/util/stream/Stream;)Ljava/util/List;"))
    private List<EnchantmentLevelEntry> filterEnchantments(RandomGenerator random, ItemStack stack, int level, Stream<Holder<Enchantment>> possibleEnchantments,
                                                           @Local(argsOnly = true) DynamicRegistryManager registryManager) {
        var newList = possibleEnchantments.toList();
        var originalList = EnchantmentHelper.generateEnchantments(random, stack, level, newList.stream());
        var kosmogliphs = getKosmogliphs(stack);
        if (kosmogliphs.isEmpty()) return originalList;

        var bannedEnchants = kosmogliphs.stream()
                .flatMap(list ->
                        list.disallowedEnchantment().stream().map(registryManager.get(RegistryKeys.ENCHANTMENT)::getHolderOrThrow)
                ).toList();
        if (bannedEnchants.isEmpty()) return originalList;

        return EnchantmentHelper.generateEnchantments(random, stack, level, newList.stream().filter(it -> !bannedEnchants.contains(it)).toList().stream());
    }
}
