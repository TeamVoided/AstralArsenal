package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.AbstractBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static org.teamvoided.astralarsenal.util.KosmogliphsStackUtilsKt.getKosmogliphs;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @WrapOperation(method = "getDroppedStacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/LootTable;generateLoot(Lnet/minecraft/loot/context/LootContextParameterSet;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"))
    private ObjectArrayList<ItemStack> modifyLoot(LootTable instance, LootContextParameterSet parameterSet, Operation<ObjectArrayList<ItemStack>> original) {
        var originalItems = original.call(instance, parameterSet);
        var world = parameterSet.getWorld();
        var stack = parameterSet.getParameterOrNull(LootContextParameters.TOOL);
        if (stack == null) return originalItems;

        var kosmogliphs = getKosmogliphs(stack);
        if (kosmogliphs.isEmpty()) return originalItems;

        var priority = kosmogliphs.stream().toList().getFirst();
        var modifiedLoot = priority.modifyBlockBreakLoot(instance, parameterSet, world, stack, originalItems);
        if (modifiedLoot instanceof ObjectArrayList<ItemStack> list) return list;
        return ObjectArrayList.wrap(modifiedLoot.toArray(new ItemStack[0]));
    }
}

