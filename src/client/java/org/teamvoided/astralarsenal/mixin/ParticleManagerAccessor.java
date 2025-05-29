package org.teamvoided.astralarsenal.mixin;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ParticleManager.class)
public interface ParticleManagerAccessor {

    @Accessor("particleAtlasTexture")
    SpriteAtlasTexture getAtlas();
}
