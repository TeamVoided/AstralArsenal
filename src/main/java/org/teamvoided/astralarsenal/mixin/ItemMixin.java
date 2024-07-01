package org.teamvoided.astralarsenal.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.astralarsenal.init.AstralItemComponents;
import org.teamvoided.astralarsenal.item.kosmogliph.LogicKt;

import java.util.List;
import java.util.Objects;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "appendTooltip", at = @At("TAIL"))
    void kosmogliphTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipConfig config, CallbackInfo ci) {
        if (!stack.getComponents().contains(AstralItemComponents.INSTANCE.getKOSMOGLIPHS())) return;

        Objects.requireNonNull(stack.get(AstralItemComponents.INSTANCE.getKOSMOGLIPHS()))
                .forEach(kosmogliph -> kosmogliph.modifyItemTooltip(stack, context, tooltip, config));
    }

    @Inject(method = "postMine", at = @At("TAIL"))
    public void kosmogliphVeinMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
        LogicKt.veinMine(stack, world, state, pos, miner);
    }
}
