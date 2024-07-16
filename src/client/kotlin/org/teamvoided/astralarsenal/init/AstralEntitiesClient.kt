package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.FlyingItemEntityRenderer


object AstralEntitiesClient {
    fun clientInit() {
        EntityRendererRegistry.register(AstralEntities.CANNONBALL_ENTITY, ::FlyingItemEntityRenderer)
        EntityRendererRegistry.register(AstralEntities.MORTER_ENTITY, ::FlyingItemEntityRenderer)
    }
}