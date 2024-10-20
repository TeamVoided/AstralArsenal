package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.astralarsenal.init.AstralItemComponents;
import org.teamvoided.astralarsenal.init.AstralKosmogliphs;
import org.teamvoided.astralarsenal.kosmogliph.ranged.trident.AstralRainKosmogliph;

import java.util.Objects;

import static org.teamvoided.astralarsenal.util.UtilKt.getKosmogliphsOnStack;

@Mixin(TridentItem.class)
public class TridentItemMixin {
    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
    private boolean isWetOrUsingKosmogliph(PlayerEntity instance, @Local(argsOnly = true) ItemStack stack) {
        var charges = Objects.requireNonNull(stack.get(AstralItemComponents.ASTRAL_RAIN_DATA)).getCharges();
        boolean hasAstralRain = getKosmogliphsOnStack(stack).contains(AstralKosmogliphs.ASTRAL_RAIN);
        if (stack.get(AstralItemComponents.ASTRAL_RAIN_DATA) == null) {
            charges = 0;
        }
        if (instance.isTouchingWaterOrRain() || (hasAstralRain && charges > 0)) {
            if(!instance.isTouchingWaterOrRain()) stack.set(AstralItemComponents.ASTRAL_RAIN_DATA, new AstralRainKosmogliph.Data(charges - 1));
            if(hasAstralRain){
                instance.playSound(SoundEvents.BLOCK_TRIAL_SPAWNER_SPAWN_ITEM_BEGIN, 2.0f, 0.5f);
                if(instance.getWorld() instanceof ServerWorld){
                    ((ServerWorld) instance.getWorld()).spawnParticles(
                            ParticleTypes.GLOW,
                            instance.getX() + (instance.getWorld().random.nextDouble() - 0.5) * 1.7,
                            instance.getY() + 1 + (instance.getWorld().random.nextDouble() - 0.5) * 1.7,
                            instance.getZ() + (instance.getWorld().random.nextDouble() - 0.5) * 1.7,
                            20,
                            instance.getVelocity().x,
                            instance.getVelocity().y,
                            instance.getVelocity().z,
                            -0.2
                            );
                }
            }
            return true;
        } else return false;
    }
    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
    private boolean isWetOrUsingKosmogliph2(PlayerEntity instance, @Local(argsOnly = true) Hand hand) {
        ItemStack stack = instance.getStackInHand(hand);
        boolean hasAstralRain = getKosmogliphsOnStack(stack).contains(AstralKosmogliphs.ASTRAL_RAIN);
        var charges = Objects.requireNonNull(stack.get(AstralItemComponents.ASTRAL_RAIN_DATA)).getCharges();
        if (stack.get(AstralItemComponents.ASTRAL_RAIN_DATA) == null) {
            charges = 0;
        }
        return instance.isTouchingWaterOrRain() || ( hasAstralRain && charges > 0);
    }
}
