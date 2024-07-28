package org.teamvoided.astralarsenal.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.astralarsenal.init.AstralKosmogliphs;
import org.teamvoided.astralarsenal.util.PlayerInteractionManagerExtension;
import org.teamvoided.astralarsenal.util.UtilKt;

import static org.teamvoided.astralarsenal.item.kosmogliph.logic.UtilKt.hammerTryBeakBlocks;

// this code is a modified version fabric-hammers mod
// https://github.com/bdani0717/fabric-hammers-1.20
@Mixin(value = ServerPlayerInteractionManager.class, priority = 1002)
public abstract class ServerPlayerInteractionManagerMixin implements PlayerInteractionManagerExtension {
    @Final
    @Shadow
    protected ServerPlayerEntity player;
    @Shadow
    protected ServerWorld world;

    @Unique
    private boolean kosmogliph_isMining = false;

    @Inject(
            method = "tryBreakBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/block/BlockState;"
            ),
            cancellable = true
    )
    private void tryBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ItemStack heldStack = player.getMainHandStack();
        var kosmogliphs = UtilKt.getKosmogliphsOnStack(heldStack);
        if (!kosmogliphs.isEmpty() && kosmogliphs.contains(AstralKosmogliphs.INSTANCE.getHAMMER())) {
            // This is to avoid recursion, but the goal is to make sure every block it doesn't override cancelled block breaks using Fabric's callbacks. This was made to support claim mods.
            boolean v = kosmogliph_isMining || hammerTryBeakBlocks(world, player, pos);

            // only cancel if the break was successful (false is returned if the player is sneaking)
            if (v) {
                cir.setReturnValue(true);
            }
        }
    }


    @Override
    public boolean kosmogliph_isMining() {
        return kosmogliph_isMining;
    }

    @Override
    public void kosmogliph_setMining(boolean mining) {
        kosmogliph_isMining = mining;
    }
}