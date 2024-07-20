package org.teamvoided.astralarsenal.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class DamageReductionMixin {
    @ModifyVariable(method = "damage", at = @At(value = "HEAD", ordinal = 0))
    private float modifyDamageType(float damage, DamageSource source) {
        var self = (LivingEntity) (Object) this;
        return DamageReductionKt.kosmogliphDamageReductionCall(self, damage, source);
    }
}
