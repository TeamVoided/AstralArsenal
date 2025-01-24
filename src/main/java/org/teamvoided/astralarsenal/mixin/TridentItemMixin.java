package org.teamvoided.astralarsenal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.astralarsenal.components.AstralRainData;
import org.teamvoided.astralarsenal.init.AstralDataComponents;
import org.teamvoided.astralarsenal.init.AstralKosmogliphs;

import static org.teamvoided.astralarsenal.util.KosmogliphsStackUtilsKt.hasKosmogliph;

@Mixin(TridentItem.class)
public class TridentItemMixin {

    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
    private boolean isWetOrUsingKosmogliph(PlayerEntity instance, @Local(argsOnly = true) ItemStack stack) {
        var charges = stack.getOrDefault(AstralDataComponents.ASTRAL_RAIN_DATA, AstralRainData.DEFAULT).getCharges();
        boolean hasAstralRain = hasKosmogliph(stack, AstralKosmogliphs.ASTRAL_RAIN);
        if (instance.isTouchingWaterOrRain() || (hasAstralRain && charges > 0)) {
            if (!instance.isTouchingWaterOrRain()) {
                stack.set(AstralDataComponents.ASTRAL_RAIN_DATA, new AstralRainData(charges - 1));
            }
            if (hasAstralRain) {
                instance.getWorld().playSoundFromEntity(instance, SoundEvents.BLOCK_TRIAL_SPAWNER_SPAWN_ITEM_BEGIN, SoundCategory.PLAYERS, 2.0f, 0.5f);
                if (instance.getWorld() instanceof ServerWorld) {
                    ((ServerWorld) instance.getWorld()).spawnParticles(
                            ParticleTypes.GLOW,
                            instance.getX() + (instance.getWorld().random.nextDouble() - 0.5) * 1.7,
                            instance.getY() + 1 + (instance.getWorld().random.nextDouble() - 0.5) * 1.7,
                            instance.getZ() + (instance.getWorld().random.nextDouble() - 0.5) * 1.7,
                            20,
                            instance.getVelocity().x,
                            instance.getVelocity().y,
                            instance.getVelocity().z,
                            1.0
                    );
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSoundFromEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V", ordinal = 1))
    private void canPlaySoundFromEntity(World instance, PlayerEntity except, Entity entity, SoundEvent sound, SoundCategory category, float volume, float pitch, Operation<Void> original, ItemStack stack) {
        if (!hasKosmogliph(stack, AstralKosmogliphs.ASTRAL_RAIN)) {
            original.call(instance, except, entity, sound, category, volume, pitch);
        }
    }

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
    private boolean isWetOrUsingKosmogliph2(PlayerEntity instance, @Local(argsOnly = true) Hand hand) {
        ItemStack stack = instance.getStackInHand(hand);
        boolean hasAstralRain = hasKosmogliph(stack, AstralKosmogliphs.ASTRAL_RAIN);
        var charges = stack.getOrDefault(AstralDataComponents.ASTRAL_RAIN_DATA, AstralRainData.DEFAULT).getCharges();
        return instance.isTouchingWaterOrRain() || (hasAstralRain && charges > 0);
    }
}
