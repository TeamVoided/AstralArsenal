package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Holder;
import net.minecraft.util.hit.EntityHitResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.astralarsenal.entity.Arrows.AntiphaseArrow;
import org.teamvoided.astralarsenal.entity.Arrows.AntiphaseSpectralArrow;

import static net.minecraft.entity.effect.StatusEffects.INSTANT_DAMAGE;
import static org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage;

@Mixin({PersistentProjectileEntity.class})
public class PersistantProjectileEntityMixin {

    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;onHit(Lnet/minecraft/entity/LivingEntity;)V"))
    void nowHaveHarming(EntityHitResult entityHitResult, CallbackInfo ci, @Local(ordinal = 0) int i) {
        if ((Object) this instanceof ArrowEntity arrow) {
            var potionContents = arrow.getStack().getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
            if (potionContents.hasEffects() && potionContents.potion().isPresent()) {
                for (StatusEffectInstance statusEffectInstance : ((Potion) ((Holder<?>) potionContents.potion().get()).value()).getEffects()) {
                    if (i >= 3 && statusEffectInstance.getEffectType() == StatusEffects.INSTANT_DAMAGE) {
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

    @WrapWithCondition(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;inGround:Z", opcode = Opcodes.PUTFIELD))
    boolean goThroughBlock(PersistentProjectileEntity instance, boolean value){
        if (instance instanceof AntiphaseArrow || instance instanceof AntiphaseSpectralArrow){
            return false;
        }
        return value;
    }
}
