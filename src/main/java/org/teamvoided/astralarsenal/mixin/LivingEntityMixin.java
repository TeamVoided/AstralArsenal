package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.astralarsenal.init.AstralKosmogliphs;
import org.teamvoided.astralarsenal.util.UtilKt;
import org.teamvoided.astralarsenal.init.AstralItemComponents;

import java.util.Objects;

import static org.teamvoided.astralarsenal.util.UtilKt.getKosmogliphsOnStack;

@Debug(export = true)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract long getLootTableSeed();

    @Shadow
    private @Nullable LivingEntity attacker;

    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"))
    private void shieldDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attackingEntity = source.getAttacker();
        Entity sourceEntity = source.getSource();
        // This will have to be moved to logic utilKt
        UtilKt.shieldDamage(this, attackingEntity, sourceEntity, amount, source);
    }

    @Inject(method = "dropLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/LootTable;generateRawLoot(Lnet/minecraft/loot/context/LootContextParameterSet;JLjava/util/function/Consumer;)V"), cancellable = true)
    private void modifyMobDrops(DamageSource source, boolean causedByPlayer, CallbackInfo ci,
                                @Local LootTable lootTable, @Local LootContextParameterSet parameterSet) {
        if (!causedByPlayer) return;
        var world = parameterSet.getWorld();
        if (this.attacker == null) return;
        var tool = this.attacker.getWeaponStack();
        var kosmogliphs = getKosmogliphsOnStack(this.attacker.getWeaponStack());
        if (kosmogliphs.isEmpty()) return;
        var fistGlyph = kosmogliphs.stream().toList().getFirst();
        lootTable.generateRawLoot(parameterSet, this.getLootTableSeed(), (stack) ->
                this.dropStack(fistGlyph.modifyEntityDropLoot(lootTable, parameterSet, world, tool, stack)));
        ci.cancel();
    }

    @ModifyConstant(method = "isBlocking", constant = @Constant(intValue = 5))
    private int setShieldUseDelay(int constant) {
        return 0;
    }


    @Inject(method = "fall", at = @At(value = "HEAD"))
    public void fall(double fallDistance, boolean onGround, BlockState landedState, BlockPos landedPosition, CallbackInfo ci) {
        if (((LivingEntity) (Object) this) instanceof PlayerEntity) {
            LivingEntity faller = (LivingEntity) (Object) this;
            UtilKt.fall(fallDistance, onGround, faller, landedPosition);
        }
    }
}
