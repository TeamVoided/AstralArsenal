package org.teamvoided.astralarsenal.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.astralarsenal.init.AstralItemComponents;
import org.teamvoided.astralarsenal.item.components.KosmogliphsComponent;

import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "appendTooltip", at = @At("TAIL"))
    void kosmogliphTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipConfig config, CallbackInfo ci) {
        if (!stack.getComponents().contains(AstralItemComponents.INSTANCE.getKOSMOGLIPHS())) return;
        astralArsenal$getKosmogliphs(stack).forEach(kosmogliph -> kosmogliph.modifyItemTooltip(stack, context, tooltip, config));
    }

    @Inject(method = "use", at = @At("TAIL"))
    public void kosmogliphOnUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        astralArsenal$getKosmogliphs(user.getStackInHand(hand)).forEach((kosmogliph) -> kosmogliph.onUse(world, user, hand));
    }

    @Inject(method = "useOnBlock", at = @At("TAIL"))
    public void kosmogliphOnUseOnBlock(ItemUsageContext ctx, CallbackInfoReturnable<ActionResult> cir) {
        astralArsenal$getKosmogliphs(ctx.getStack()).forEach((kosmogliph) -> kosmogliph.onUseOnBlock(ctx));
    }

    @Inject(method = "useOnEntity", at = @At("TAIL"))
    public void kosmogliphOnUseOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        astralArsenal$getKosmogliphs(stack).forEach((kosmogliph) -> kosmogliph.onUseOnEntity(stack, user, entity, hand));
    }

    @Inject(method = "postMine", at = @At("TAIL"))
    public void kosmogliphPostMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
        astralArsenal$getKosmogliphs(stack).forEach((kosmogliph) -> kosmogliph.postMine(stack, world, state, pos, miner));
    }

    @Inject(method = "postHit", at = @At("TAIL"))
    public void kosmogliphPostHit(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        astralArsenal$getKosmogliphs(stack).forEach((kosmogliph) -> kosmogliph.postHit(stack, target, attacker));
    }

    @Unique
    private KosmogliphsComponent astralArsenal$getKosmogliphs(ItemStack stack) {
        var component = stack.getComponents().get(AstralItemComponents.INSTANCE.getKOSMOGLIPHS());
        if (component == null) return new KosmogliphsComponent();
        return component;
    }
}
