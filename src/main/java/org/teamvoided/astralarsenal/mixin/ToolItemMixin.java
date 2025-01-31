package org.teamvoided.astralarsenal.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.teamvoided.astralarsenal.util.KosmogliphsStackUtilsKt.getKosmogliphs;

@Mixin({SwordItem.class, MiningToolItem.class, MaceItem.class, TridentItem.class})
public class ToolItemMixin {

    @Inject(method = "postHit", at = @At("TAIL"))
    public void kosmogliphPostHit(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        getKosmogliphs(stack).forEach((kosmogliph) -> kosmogliph.postHit(stack, target, attacker));
    }
}
