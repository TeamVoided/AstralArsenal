package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.entity.EmptyEntityRenderer
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import org.teamvoided.astralarsenal.block.entity.KosmicTableBlockEntityRenderer
import org.teamvoided.astralarsenal.entity.AstralProjectionEntityRenderer
import org.teamvoided.astralarsenal.entity.BeamRenderer
import org.teamvoided.astralarsenal.entity.NailEntityRenderer


object AstralRenderers {
    fun init() {
        EntityRendererRegistry.register(AstralEntities.CANNONBALL_ENTITY, ::FlyingItemEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.BEAM_OF_LIGHT, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.BEAM_OF_LIGHT_ARROW, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.SLASH_ENTITY, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.DEEP_WOUND_ENTITY, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.FLAME_SHOT_ENTITY, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.FREEZE_SHOT_ENTITY, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.RICHOCHET, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.CONDUCTIVITY_ENGINE, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.NAIL_ENTITY, ::NailEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.BEAM_RENDERER, ::BeamRenderer)
        EntityRendererRegistry.register(AstralEntities.ASTRAL_PROJECTION, ::AstralProjectionEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.VOID_SHARD, ::FlyingItemEntityRenderer)

        BlockEntityRendererFactories.register(AstralBlocks.COSMIC_TABLE_BLOCK_ENTITY, ::KosmicTableBlockEntityRenderer)
    }
}