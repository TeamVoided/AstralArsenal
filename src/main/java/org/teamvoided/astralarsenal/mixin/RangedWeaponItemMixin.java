package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.SpectralArrowItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.astralarsenal.entity.Arrows.*;
import org.teamvoided.astralarsenal.init.AstralKosmogliphs;
import org.teamvoided.astralarsenal.kosmogliph.ranged.RangedWeaponKosmogliph;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.teamvoided.astralarsenal.util.KosmogliphsStackUtilsKt.getKosmogliphs;
import static org.teamvoided.astralarsenal.util.KosmogliphsStackUtilsKt.hasKosmogliph;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
    @Inject(method = "shootAll", at = @At("HEAD"), cancellable = true)
    private void preFire(ServerWorld world, LivingEntity livingEntity, Hand hand, ItemStack stack, List<ItemStack> list, float f, float g, boolean bl, @Nullable LivingEntity livingEntity2, CallbackInfo ci) {
        AtomicBoolean shouldCancel = new AtomicBoolean(false);
        getKosmogliphs(stack).forEach((kosmogliph) -> {
            if (kosmogliph instanceof RangedWeaponKosmogliph rwk && rwk.preFire(world, livingEntity, hand, stack, list, f, g, bl, livingEntity2)) {
                shouldCancel.set(true);
            }
        });

        if (shouldCancel.get()) ci.cancel();
    }

    @WrapOperation(method = "getProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/projectile/ProjectileEntity;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ArrowItem;createArrowEntity(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
    private PersistentProjectileEntity changeArrowType(
            ArrowItem arrowItem,
            World world,
            ItemStack arrow,
            LivingEntity entity,
            ItemStack weapon,
            Operation<PersistentProjectileEntity> origional) {
        if (hasKosmogliph(weapon, AstralKosmogliphs.ANTIPHASE)) {
            if (arrowItem instanceof SpectralArrowItem) {
                return new AntiphaseSpectralArrow(world, entity, arrow.copyWithCount(1), weapon);
            } else {
                return new AntiphaseArrow(world, entity, arrow.copyWithCount(1), weapon);
            }
        } else if (hasKosmogliph(weapon, AstralKosmogliphs.MAGNETIC)) {
            if (arrowItem instanceof SpectralArrowItem) {
                return new MagneticSpectralArrow(world, entity, arrow.copyWithCount(1), weapon);
            } else {
                return new MagneticArrow(world, entity, arrow.copyWithCount(1), weapon);
            }
        } else if (hasKosmogliph(weapon, AstralKosmogliphs.CONDUCTIVE)) {
            if (arrowItem instanceof SpectralArrowItem) {
                return new ChargedSpectralArrow(world, entity, arrow.copyWithCount(1), weapon);
            } else {
                return new ChargedArrow(world, entity, arrow.copyWithCount(1), weapon);
            }
        } else {
            return origional.call(arrowItem, world, arrow, entity, weapon);
        }
    }
}
