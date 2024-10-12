package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.astralarsenal.init.AstralEffects;
import org.teamvoided.astralarsenal.item.kosmogliph.DamageModificationStage;
import org.teamvoided.astralarsenal.pseudomixin.DamageReductionKt;
import org.spongepowered.asm.mixin.Debug;

@Debug(export = true)
@Mixin(LivingEntity.class)
public class DamageReductionMixin {
    @ModifyVariable(method = "damage", at = @At(value = "HEAD", ordinal = 0), argsOnly = true)
    private float modifyDamageEffect(float damage, DamageSource source) {
        var self = (LivingEntity) (Object) this;
        damage = DamageReductionKt.kosmogliphDamageReductionCall(self, damage, source, DamageModificationStage.PRE_EFFECT);
        damage = AstralEffects.INSTANCE.modifyDamage(self, damage, source);
        damage = DamageReductionKt.kosmogliphDamageReductionCall(self, damage, source, DamageModificationStage.POST_EFFECT);
        return damage;
    }

    @ModifyVariable(
            method = "applyArmorToDamage",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float modifyDamagePreArmor(float value, DamageSource source) {
        var self = (LivingEntity) (Object) this;
        return DamageReductionKt.kosmogliphDamageReductionCall(self, value, source, DamageModificationStage.PRE_ARMOR);
    }

    @ModifyReturnValue(
            method = "applyArmorToDamage",
            at = @At("RETURN")
    )
    private float modifyDamagePostArmor(float value, DamageSource source) {
        var self = (LivingEntity) (Object) this;
        return DamageReductionKt.kosmogliphDamageReductionCall(self, value, source, DamageModificationStage.POST_ARMOR);
    }

    @ModifyVariable(
            method = "applyEnchantmentsToDamage",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float modifyDamagePreEnchant(float value, DamageSource source) {
        var self = (LivingEntity) (Object) this;
        return DamageReductionKt.kosmogliphDamageReductionCall(self, value, source, DamageModificationStage.PRE_ENCHANT);
    }

    @ModifyReturnValue(
            method = "applyEnchantmentsToDamage",
            at = @At("RETURN")
    )
    private float modifyDamagePostEnchant(float value, DamageSource source) {
        var self = (LivingEntity) (Object) this;
        return DamageReductionKt.kosmogliphDamageReductionCall(self, value, source, DamageModificationStage.POST_ENCHANT);
    }

    @Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
    private void kosmogliphInvulnerability(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        var self = (LivingEntity) (Object) this;
        DamageReductionKt.kosmogliphInvulnerabilityCheck(self, damageSource, cir);
    }
}
