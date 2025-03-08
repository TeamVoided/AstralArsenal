package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.in_game.InGameHud;
import net.minecraft.client.render.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.astralarsenal.init.AstralHudRendering;

@Mixin(InGameHud.class)
public class CrimsonTimeRendererMixin {

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SourceFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DestFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SourceFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DestFactor;)V", shift = At.Shift.AFTER))
    void crimsonTimeRendererInit(GuiGraphics graphics, DeltaTracker tracker, CallbackInfo ci) {
        if (AstralHudRendering.crimsonCrosshair) {
            RenderSystem.setShaderColor(0.75f, 0.0f, 0.0f, 1.0f);
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.ONE_MINUS_CONSTANT_COLOR, GlStateManager.DestFactor.ONE_MINUS_DST_COLOR,
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
            );
        }
    }

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", ordinal = 2, target = "Lnet/minecraft/client/gui/GuiGraphics;getScaledWindowHeight()I"))
    void extendAttackIndicatorForCrimsonTime(GuiGraphics graphics, DeltaTracker tracker, CallbackInfo ci,
                                             @Local float cooldownProgress, @Local LocalBooleanRef isFull) {
        isFull.set(isFull.get() || (AstralHudRendering.crimsonCrosshair && !(cooldownProgress < 1.0f)));
    }

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;defaultBlendFunc()V", remap = false))
    void disableCrimsonTimeModifications(GuiGraphics graphics, DeltaTracker tracker, CallbackInfo ci) {
        if (AstralHudRendering.crimsonCrosshair) RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
