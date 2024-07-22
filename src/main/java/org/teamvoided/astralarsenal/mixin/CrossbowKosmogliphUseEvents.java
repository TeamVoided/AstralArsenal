package org.teamvoided.astralarsenal.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.astralarsenal.util.UtilKt;

@Mixin(CrossbowItem.class)
public class CrossbowKosmogliphUseEvents {
    @Inject(method = "use", at = @At("HEAD"))
    public void kosmogliphPreUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        UtilKt.getKosmogliphsOnStack(user.getStackInHand(hand)).forEach((kosmogliph) -> kosmogliph.preUse(world, user, hand));
    }

    @Inject(method = "use", at = @At("TAIL"))
    public void kosmogliphOnUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        UtilKt.getKosmogliphsOnStack(user.getStackInHand(hand)).forEach((kosmogliph) -> kosmogliph.onUse(world, user, hand));
    }
}
