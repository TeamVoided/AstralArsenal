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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.teamvoided.astralarsenal.init.AstralItemComponents;
import org.teamvoided.astralarsenal.item.components.KosmogliphsComponent;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @WrapOperation(method = "getDroppedStacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/LootTable;generateLoot(Lnet/minecraft/loot/context/LootContextParameterSet;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"))
    private ObjectArrayList<ItemStack> modifyLoot(LootTable instance, LootContextParameterSet parameterSet, Operation<ObjectArrayList<ItemStack>> original) {
        var world = parameterSet.getWorld();
        var stack = parameterSet.getParameterOrNull(LootContextParameters.TOOL);
        if (stack == null) return original.call(instance, parameterSet);
        var kosmogliphs = astralArsenal$getKosmogliphs(stack);
        if (kosmogliphs.isEmpty()) return original.call(instance, parameterSet);
        var priority = kosmogliphs.stream().toList().getFirst();
        var modifiedLoot = priority.modifyBlockBreakLoot(instance, parameterSet, world, stack, original.call(instance, parameterSet));
        if (modifiedLoot instanceof ObjectArrayList<ItemStack> oal) return oal;
        var mlArray = modifiedLoot.toArray(new ItemStack[0]);
        return ObjectArrayList.wrap(mlArray);
    }

    @Unique
    private KosmogliphsComponent astralArsenal$getKosmogliphs(ItemStack stack) {
        var component = stack.getComponents().get(AstralItemComponents.INSTANCE.getKOSMOGLIPHS());
        if (component == null) return new KosmogliphsComponent();
        return component;
    }
}
