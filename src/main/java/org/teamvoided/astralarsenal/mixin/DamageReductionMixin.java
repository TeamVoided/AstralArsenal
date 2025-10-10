package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage;
import org.teamvoided.astralarsenal.pseudomixin.DamageReductionKt;

import static org.teamvoided.astralarsenal.util.EffectDamageModifiersKt.*;
import static org.teamvoided.astralarsenal.util.HexAplicationKt.applyHexes;

@Mixin(LivingEntity.class)
public class DamageReductionMixin {

    @Unique
    private final LivingEntity astralArsenal$self = (LivingEntity) (Object) this;

    @ModifyVariable(method = "damage", at = @At(value = "HEAD", ordinal = 0), argsOnly = true)
    private float modifyDamageEffect(float damage, DamageSource source) {
        damage = DamageReductionKt.kosmogliphDamageReductionCall(astralArsenal$self, damage, source, DamageModificationStage.PRE_EFFECT);
        damage = modifyDamage(astralArsenal$self, damage, source);
        damage = DamageReductionKt.kosmogliphDamageReductionCall(astralArsenal$self, damage, source, DamageModificationStage.POST_EFFECT);
        cancelTomeOnHit(astralArsenal$self, source);
        return damage;
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void cancelDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (effectCancelDamage(astralArsenal$self, amount, source)) {
            cir.cancel();
        }
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void apply(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        applyHexes(source, entity);
    }

    @ModifyVariable(
            method = "applyArmorToDamage",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float modifyDamagePreArmor(float value, DamageSource source) {
        return DamageReductionKt.kosmogliphDamageReductionCall(astralArsenal$self, value, source, DamageModificationStage.PRE_ARMOR);
    }

    @ModifyReturnValue(
            method = "applyArmorToDamage",
            at = @At("RETURN")
    )
    private float modifyDamagePostArmor(float value, DamageSource source) {
        return DamageReductionKt.kosmogliphDamageReductionCall(astralArsenal$self, value, source, DamageModificationStage.POST_ARMOR);
    }

    @ModifyVariable(
            method = "applyEnchantmentsToDamage",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float modifyDamagePreEnchant(float value, DamageSource source) {
        return DamageReductionKt.kosmogliphDamageReductionCall(astralArsenal$self, value, source, DamageModificationStage.PRE_ENCHANT);
    }

    @ModifyReturnValue(
            method = "applyEnchantmentsToDamage",
            at = @At("RETURN")
    )
    private float modifyDamagePostEnchant(float value, DamageSource source) {
        return DamageReductionKt.kosmogliphDamageReductionCall(astralArsenal$self, value, source, DamageModificationStage.POST_ENCHANT);
    }

    @Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
    private void kosmogliphInvulnerability(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        DamageReductionKt.kosmogliphInvulnerabilityCheck(astralArsenal$self, damageSource, cir);
    }
}
