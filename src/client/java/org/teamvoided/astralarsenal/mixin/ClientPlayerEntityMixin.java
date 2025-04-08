package org.teamvoided.astralarsenal.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static org.teamvoided.astralarsenal.kosmogliph.logic.SmallLogicKt.modifyItemUseSlowdown;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @ModifyConstant(method = "tickMovement", constant = @Constant(floatValue = 0.2f))
    private float setShieldUseDelay(float constant) {
        return modifyItemUseSlowdown(this.getActiveItem());
    }
}
