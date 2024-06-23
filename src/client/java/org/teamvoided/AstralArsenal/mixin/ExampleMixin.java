package org.teamvoided.AstralArsenal.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.teamvoided.AstralArsenal.Template.log;

@Mixin(MinecraftClient.class)
public class ExampleMixin {

    @Inject(at = @At("HEAD"), method = "run")
    private void run(CallbackInfo info) {
        log.info("Hello from Mixin");
    }
}
