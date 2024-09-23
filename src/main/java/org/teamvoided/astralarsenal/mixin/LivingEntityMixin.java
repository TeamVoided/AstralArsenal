package org.teamvoided.astralarsenal.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.astralarsenal.util.UtilKt;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"))
    private void shieldDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attackingEntity = source.getAttacker();
        Entity sourceEntity = source.getSource();
        UtilKt.shieldDamage((Entity) (Object) this, attackingEntity, sourceEntity, amount, source);
    }
}
