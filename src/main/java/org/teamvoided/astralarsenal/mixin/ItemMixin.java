package org.teamvoided.astralarsenal.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.astralarsenal.util.UtilKt;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "use", at = @At("HEAD"))
    public void kosmogliphPreUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        UtilKt.getKosmogliphsOnStack(user.getStackInHand(hand)).forEach((kosmogliph) -> kosmogliph.preUse(world, user, hand));
    }

    @Inject(method = "use", at = @At("TAIL"))
    public void kosmogliphOnUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        UtilKt.getKosmogliphsOnStack(user.getStackInHand(hand)).forEach((kosmogliph) -> kosmogliph.onUse(world, user, hand));
    }

    @Inject(method = "useOnBlock", at = @At("TAIL"))
    public void kosmogliphOnUseOnBlock(ItemUsageContext ctx, CallbackInfoReturnable<ActionResult> cir) {
        UtilKt.getKosmogliphsOnStack(ctx.getStack()).forEach((kosmogliph) -> kosmogliph.onUseOnBlock(ctx));
    }

    @Inject(method = "useOnEntity", at = @At("TAIL"))
    public void kosmogliphOnUseOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        UtilKt.getKosmogliphsOnStack(stack).forEach((kosmogliph) -> kosmogliph.onUseOnEntity(stack, user, entity, hand));
    }

    @Inject(method = "onClicked", at = @At("TAIL"), cancellable = true)
    public void kosmogliphOnClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
        AtomicBoolean actionPerformed = new AtomicBoolean(false);
        UtilKt.getKosmogliphsOnStack(thisStack).forEach((kosmogliph) -> {
            if (kosmogliph.onStackClicked(thisStack, otherStack, thisSlot, clickType, player, cursorStackReference)) {
                actionPerformed.set(true);
            }
        });

        if (actionPerformed.get()) cir.setReturnValue(true);
    }

    @Inject(method = "postMine", at = @At("TAIL"))
    public void kosmogliphPostMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
        UtilKt.getKosmogliphsOnStack(stack).forEach((kosmogliph) -> kosmogliph.postMine(stack, world, state, pos, miner));
    }

    @Inject(method = "postHit", at = @At("TAIL"))
    public void kosmogliphPostHit(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        UtilKt.getKosmogliphsOnStack(stack).forEach((kosmogliph) -> kosmogliph.postHit(stack, target, attacker));
    }

    @Inject(method = "inventoryTick", at = @At("TAIL"))
    public void kosmogliphInvTick(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        UtilKt.getKosmogliphsOnStack(stack).forEach((kosmogliph) -> kosmogliph.inventoryTick(stack, world, entity, slot, selected));
    }

    @Inject(method = "usageTick", at = @At("TAIL"))
    public void kosmogliphUsageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
        UtilKt.getKosmogliphsOnStack(stack).forEach((kosmogliph) -> kosmogliph.usageTick(world, user, stack, remainingUseTicks));
    }
}
