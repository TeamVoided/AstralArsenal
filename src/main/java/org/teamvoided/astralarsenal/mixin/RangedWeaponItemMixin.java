package org.teamvoided.astralarsenal.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.astralarsenal.kosmogliph.ranged.RangedWeaponKosmogliph;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.teamvoided.astralarsenal.util.KosmogliphsStackUtilsKt.getKosmogliphs;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
    @Inject(method = "shootAll", at = @At("HEAD"), cancellable = true)
    private void preFire(ServerWorld world, LivingEntity livingEntity, Hand hand, ItemStack stack, List<ItemStack> list, float f, float g, boolean bl, @Nullable LivingEntity livingEntity2, CallbackInfo ci) {
        AtomicBoolean shouldCancel = new AtomicBoolean(false);
        getKosmogliphs(stack).forEach((kosmogliph) -> {
            if (kosmogliph instanceof RangedWeaponKosmogliph rwk && rwk.preFire(world, livingEntity, hand, stack, list, f, g, bl, livingEntity2)) {
                shouldCancel.set(true);
            }
        });

        if (shouldCancel.get()) ci.cancel();
    }
}
