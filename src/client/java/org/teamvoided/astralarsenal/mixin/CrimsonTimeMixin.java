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

@Mixin(MinecraftClient.class)
public class CrimsonTimeMixin {
  @Shadow
  public ClientPlayerInteractionManager interactionManager;
  @Shadow
  public ClientPlayerEntity player;

  // Change this to change the number of ticks players have crimson time for
  @Unique
  private static int CRIMSON_TIME_TICKS = 3;
  @Unique
  private static int ticks = 0;
  @Unique
  private static Entity target = null;

  @Inject(method = "tick", at = @At("HEAD"))
  private void astral$crimsonTick(CallbackInfo info) {
    var instance = MinecraftClient.getInstance();
    AstralHudRendering.INSTANCE.setCrimsonCrosshair(false);

    if(instance.targetedEntity != null) {
      target = instance.targetedEntity;
      ticks = CRIMSON_TIME_TICKS;
    }

    if(ticks > 0) {
      ticks--;

      if(instance.targetedEntity == null && !target.isInvisible())
        AstralHudRendering.INSTANCE.setCrimsonCrosshair(true);
    } else {
      target = null;
    }
  }

  @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
  private void astral$crimsonAttack(CallbackInfoReturnable<Boolean> info) {
    if (target != null) {
      interactionManager.attackEntity(player, target);
      player.swingHand(Hand.MAIN_HAND);
      info.setReturnValue(true);
    }
  }
}
