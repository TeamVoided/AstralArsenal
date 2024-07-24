package org.teamvoided.astralarsenal.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.astralarsenal.util.UtilKt;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "getArrowType", at = @At("RETURN"), cancellable = true)
    public void modifyArrowType(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        var kosmogliphs = UtilKt.getKosmogliphsOnStack(stack);
        if (kosmogliphs.isEmpty()) return;
        var kosmogliph = UtilKt.findFirstRanged(kosmogliphs);
        if (kosmogliph == null) return;

        var newArrowType = kosmogliph.overrideArrowType((PlayerEntity) (Object) this, stack);
        if (newArrowType == null) return;

        cir.setReturnValue(newArrowType);
    }
}
