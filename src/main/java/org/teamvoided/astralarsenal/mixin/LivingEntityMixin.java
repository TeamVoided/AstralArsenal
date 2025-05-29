package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.astralarsenal.effects.AstralStatusEffect;
import org.teamvoided.astralarsenal.networking.UpdateHexRingPayload;
import org.teamvoided.astralarsenal.util.EntityHexAccessor;
import org.teamvoided.astralarsenal.util.UtilKt;

import java.util.Optional;

import static org.teamvoided.astralarsenal.util.HexAplicationKt.*;
import static org.teamvoided.astralarsenal.util.KosmogliphsStackUtilsKt.getKosmogliphs;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements EntityHexAccessor {

    @Shadow
    public abstract long getLootTableSeed();

    @Shadow
    private @Nullable LivingEntity attacker;

    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);

    @Unique
    LivingEntity astralArsenal$me = (LivingEntity) (Object) this;

    @Unique
    private int astralArsenal$oldHexColor = -1;

    @Unique
    private int astralArsenal$hexColor = -1;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

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
        if (!causedByPlayer) {
            return;
        }
        var world = parameterSet.getWorld();
        if (this.attacker == null) {
            return;
        }
        var tool = this.attacker.getWeaponStack();
        var kosmogliphs = getKosmogliphs(this.attacker.getWeaponStack());
        if (kosmogliphs.isEmpty()) {
            return;
        }
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
        if (astralArsenal$me instanceof PlayerEntity player) {
            UtilKt.fall(fallDistance, onGround, player, landedPosition);
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "HEAD"))
    public void tickMovement(CallbackInfo ci) {
        UtilKt.tickMovement(astralArsenal$me);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void tick(CallbackInfo ci) {
        removeHexes(astralArsenal$me);
        tickDownPositiveEffects(astralArsenal$me);
        tickDownHealth(astralArsenal$me);

        if (!astralArsenal$me.getWorld().isClient) {
            Optional<StatusEffectInstance> tomeEffects = astralArsenal$me.getStatusEffects().stream()
                    .filter(instance ->
                            instance.getEffectType().value() instanceof AstralStatusEffect astralEffect
                                    && astralEffect.getShowTomeRings()).findFirst();

            tomeEffects.ifPresentOrElse(effect ->
                            astralArsenal$hexColor = effect.getEffectType().value().getColor(),
                    () -> astralArsenal$hexColor = -1
            );

            if (astralArsenal$hexColor != astralArsenal$oldHexColor) {
                astralArsenal$oldHexColor = astralArsenal$hexColor;

                UpdateHexRingPayload payload = new UpdateHexRingPayload(astralArsenal$me.getId(), astralArsenal$hexColor);
                for (ServerPlayerEntity player : PlayerLookup.tracking(astralArsenal$me)) {
                    if (player.equals(astralArsenal$me)) {
                        continue;
                    }

                    ServerPlayNetworking.send(player, payload);
                }

                if (astralArsenal$me instanceof ServerPlayerEntity serverPlayer) {
                    ServerPlayNetworking.send(serverPlayer, payload);
                }
            }
        }
    }

    @Override
    public void setAstralArsenal$hexColor(int color) {
        astralArsenal$hexColor = color;
    }

    @Override
    public int getAstralArsenal$hexColor() {
        return astralArsenal$hexColor;
    }
}
