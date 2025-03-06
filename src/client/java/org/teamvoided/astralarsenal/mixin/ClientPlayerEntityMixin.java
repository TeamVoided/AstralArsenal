package org.teamvoided.astralarsenal.mixin;

import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.astralarsenal.util.UtilKt;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Shadow
    public Input input;

    @Unique
    private final PlayerEntity astralArsenal$self = (PlayerEntity) (Object) this;

    @Inject(method = "tickMovement", at = @At(value = "FIELD", target = "Lnet/minecraft/client/input/Input;forwardMovement:F"))
    private void changeItemUseMovement(CallbackInfo ci) {
        float modifier = UtilKt.modifyItemUseSpeed(astralArsenal$self, astralArsenal$self.getActiveItem());
        input.forwardMovement *= modifier;
        input.sidewaysMovement *= modifier;
    }
}
