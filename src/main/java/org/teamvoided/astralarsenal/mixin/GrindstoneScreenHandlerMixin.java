package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static org.teamvoided.astralarsenal.util.mixin.GrindstoneUtilsKt.grindOffGlyphs;

@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneScreenHandlerMixin {

    @ModifyReturnValue(method = "method_58070", at = @At("RETURN"))
    ItemStack removeGlyphWithGrindstone(ItemStack output, ItemStack input0, ItemStack input1) {
        var stack = grindOffGlyphs(input0, input1, output);
        if (stack != null) return stack;
        return output;
    }
}
