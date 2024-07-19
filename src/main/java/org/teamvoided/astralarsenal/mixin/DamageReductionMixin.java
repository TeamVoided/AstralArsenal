package org.teamvoided.astralarsenal.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.teamvoided.astralarsenal.init.AstralItemComponents;
import org.teamvoided.astralarsenal.item.kosmogliph.DamageReductionKosmogliph;
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph;

@Mixin(LivingEntity.class)
public class DamageReductionMixin {
  @ModifyVariable(method = "damage", at = @At(value = "HEAD", ordinal = 0))
  private float modifyDamageType(float damage, DamageSource source) {
    var self = (LivingEntity) (Object) this;
    for(ItemStack stack: self.getArmorItems()) {
      var kosmogliphs = stack.get(AstralItemComponents.INSTANCE.getKOSMOGLIPHS());
      if(kosmogliphs != null)
        for(Kosmogliph kosmogliph: kosmogliphs)
          if(kosmogliph instanceof DamageReductionKosmogliph damageReductionKosmogliph)
            for(var tag: damageReductionKosmogliph.getTags()) {
              if(source.isTypeIn(tag))
                damage = damage * damageReductionKosmogliph.getMultiplier();
            }
    }
    return damage;
  }
}
