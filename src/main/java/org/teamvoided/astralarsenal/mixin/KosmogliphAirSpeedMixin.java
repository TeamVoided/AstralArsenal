package org.teamvoided.astralarsenal.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.astralarsenal.pseudomixin.AirSpeedKt;

@Mixin(PlayerEntity.class)
public class KosmogliphAirSpeedMixin {
    @Inject(method = "getAirSpeed()F", at = @At("RETURN"), cancellable = true)
    private void modifyAirSpeed(CallbackInfoReturnable<Float> info) {
        LivingEntity entity = (LivingEntity) (Object) this;
        info.setReturnValue(AirSpeedKt.airSpeedKosmogliphCall(entity, info.getReturnValue()));
    }
}
