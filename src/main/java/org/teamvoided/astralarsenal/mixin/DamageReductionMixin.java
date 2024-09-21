package org.teamvoided.astralarsenal.mixin;

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
//import org.spongepowered.asm.mixin.Debug;

//@Debug(export = true)
@Mixin(LivingEntity.class)
public class DamageReductionMixin {
    @ModifyVariable(method = "damage", at = @At(value = "HEAD", ordinal = 0), argsOnly = true)
    private float modifyDamagePreArmor(float damage, DamageSource source) {
        var self = (LivingEntity) (Object) this;
        damage = DamageReductionKt.kosmogliphDamageReductionCall(self, damage, source, DamageModificationStage.PRE_EFFECT);
        damage = AstralEffects.INSTANCE.modifyDamage(self, damage);
        damage = DamageReductionKt.kosmogliphDamageReductionCall(self, damage, source, DamageModificationStage.POST_EFFECT);
        return DamageReductionKt.kosmogliphDamageReductionCall(self, damage, source, DamageModificationStage.PRE_ARMOR);
    }

    //Right before damage application
    //Painfully found the right ordinal for this
    @ModifyVariable(
            method = "damage",
            at = @At(
                    value = "LOAD",
                    ordinal = 7
            ),
            argsOnly = true
    )
    private float modifyDamagePostArmor(float value, DamageSource source) {
        var self = (LivingEntity) (Object) this;
        return DamageReductionKt.kosmogliphDamageReductionCall(self, value, source, DamageModificationStage.POST_ARMOR);
    }

    @Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
    private void kosmogliphInvulnerability(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        var self = (LivingEntity) (Object) this;
        DamageReductionKt.kosmogliphInvulnerabilityCheck(self, damageSource, cir);
    }
}
