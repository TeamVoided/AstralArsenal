package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ArrowEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ArrowEntity.class})
abstract class ArrowEntityMixin {

    @WrapWithCondition(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z"))
    private boolean noMoreHarming(LivingEntity instance, StatusEffectInstance effect, Entity source) {
            return effect.getEffectType() != StatusEffects.INSTANT_DAMAGE;
    }

    @WrapWithCondition(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z"))
    private boolean noMoreHarmingTwo(LivingEntity instance, StatusEffectInstance effect, Entity source) {
        return effect.getEffectType() != StatusEffects.INSTANT_DAMAGE;
    }
}
