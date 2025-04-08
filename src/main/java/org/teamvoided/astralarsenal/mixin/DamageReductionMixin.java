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
import org.teamvoided.astralarsenal.init.AstralEffects;
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage;
import org.teamvoided.astralarsenal.pseudomixin.DamageReductionKt;

@Mixin(LivingEntity.class)
public class DamageReductionMixin {

    @Unique
    private final LivingEntity astralArsenal$self = (LivingEntity) (Object) this;

    @ModifyVariable(method = "damage", at = @At(value = "HEAD", ordinal = 0), argsOnly = true)
    private float modifyDamageEffect(float damage, DamageSource source) {
        damage = DamageReductionKt.kosmogliphDamageReductionCall(astralArsenal$self, damage, source, DamageModificationStage.PRE_EFFECT);
        damage = AstralEffects.INSTANCE.modifyDamage(astralArsenal$self, damage, source);
        damage = DamageReductionKt.kosmogliphDamageReductionCall(astralArsenal$self, damage, source, DamageModificationStage.POST_EFFECT);
        return damage;
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void cancelDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (AstralEffects.INSTANCE.cancelDamage(astralArsenal$self, amount, source)) {
            cir.cancel();
        }
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
