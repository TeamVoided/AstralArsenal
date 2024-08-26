package org.teamvoided.astralarsenal.mixin;

import net.minecraft.block.Block;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({HoeItem.class,})
public class HoeItemMixin extends MiningToolItem {

    public HoeItemMixin(ToolMaterial material, TagKey<Block> blocks, Settings settings) {
        super(material, blocks, settings);
    }

    @Inject(method = "useOnBlock", at = @At("RETURN"))
    public void kosmogliphPostHit(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        super.useOnBlock(context);
    }
}
