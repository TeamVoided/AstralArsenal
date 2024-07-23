package org.teamvoided.astralarsenal.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.trident.ThrownTridentKosmogliph;
import org.teamvoided.astralarsenal.util.UtilKt;

@Mixin(TridentEntity.class)
public class TridentMixin {
  @Inject(method = "onEntityHit", at = @At("TAIL"))
  private void kosmogliphPostDamageEntity(EntityHitResult entityHitResult, CallbackInfo info) {
    TridentEntity entity = (TridentEntity) (Object) this;
    var stack = entity.getWeaponStack();
    if(entityHitResult.getEntity() instanceof LivingEntity target && stack != null)
      UtilKt.getKosmogliphsOnStack(stack).forEach(kosmogliph -> {
        if(kosmogliph instanceof ThrownTridentKosmogliph tridentKosmogliph)
          tridentKosmogliph.onHit(entity.getOwner(), target);
      });
  }
}
