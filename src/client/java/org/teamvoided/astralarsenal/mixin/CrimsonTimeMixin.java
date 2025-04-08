package org.teamvoided.astralarsenal.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.astralarsenal.init.AstralHudRendering;

import static org.teamvoided.astralarsenal.util.ConstantsKt.CRIMSON_TIME_TICKS;

@Mixin(MinecraftClient.class)
public abstract class CrimsonTimeMixin {
    @Shadow
    public ClientPlayerInteractionManager interactionManager;
    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    public static MinecraftClient getInstance() {
        throw new AssertionError("Mixin Failed");
    }

    @Unique
    private static int astral_arsenal$ticks = 0;
    @Unique
    private static Entity astral_arsenal$target = null;

    @Inject(method = "tick", at = @At("HEAD"))
    private void astral$crimsonTick(CallbackInfo info) {
        AstralHudRendering.crimsonCrosshair = false;

        if (getInstance().targetedEntity != null) {
            astral_arsenal$target = getInstance().targetedEntity;
            astral_arsenal$ticks = CRIMSON_TIME_TICKS;
        }

        if (astral_arsenal$ticks > 0) {
            astral_arsenal$ticks--;

            if (getInstance().targetedEntity == null && !astral_arsenal$target.isInvisible())
                AstralHudRendering.crimsonCrosshair = true;
        } else astral_arsenal$target = null;
    }

    // (ender) I tired changing this to ModifyReturnValue but that made it do a lot less damage somehow, someone look in to this please
    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void astral$crimsonAttack(CallbackInfoReturnable<Boolean> info) {
        if (astral_arsenal$target != null) {
            interactionManager.attackEntity(player, astral_arsenal$target);
            player.swingHand(Hand.MAIN_HAND);
            info.setReturnValue(true);
        }
    }
}
