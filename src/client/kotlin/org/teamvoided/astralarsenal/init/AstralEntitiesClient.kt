package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.*
import org.teamvoided.astralarsenal.entity.NailEntityRenderer


object AstralEntitiesClient {
    fun clientInit() {
        EntityRendererRegistry.register(AstralEntities.CANNONBALL_ENTITY, ::FlyingItemEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.MORTAR_ENTITY, ::FlyingItemEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.BEAM_OF_LIGHT, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.BLACK_HOLE_ENTITY, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.BEAM_OF_LIGHT_ARROW, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.SLASH_ENTITY, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.DEEP_WOUND_ENTITY, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.BOOM_SHOOTER_ENTITY, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.BOOM_ENTITY, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.FLAME_SHOT_ENTITY, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.FREEZE_SHOT_ENTITY, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.FLAME_THROWER_ENTITY, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.RICHOCHET, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.NAIL_ENTITY, ::NailEntityRenderer)
    }
}