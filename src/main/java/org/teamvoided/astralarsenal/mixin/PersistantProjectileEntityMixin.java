package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Holder;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage;

@Mixin({PersistentProjectileEntity.class})
public class PersistantProjectileEntityMixin {

    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;onHit(Lnet/minecraft/entity/LivingEntity;)V"))
    void nowHaveHarming(EntityHitResult entityHitResult, CallbackInfo ci, @Local(ordinal = 0) int i) {
        if ((Object) this instanceof ArrowEntity arrow) {
            var potionContents = arrow.getStack().getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
            if (potionContents.hasEffects()) {
                for (StatusEffectInstance statusEffectInstance : ((Potion) ((Holder<?>) potionContents.potion().get()).value()).getEffects()) {
                    if (i >= 3) {
                        float dmg = (float) i;
                        if (!(entityHitResult.getEntity() instanceof PlayerEntity)){
                            dmg *= 1.5f;
                        }
                        var damage = dmg + 2 + (2 * statusEffectInstance.getAmplifier());
                        customDamage(entityHitResult.getEntity(), DamageTypes.MAGIC, damage, arrow, arrow.getOwner());
                    }
                }
            }
        }
    }

}
