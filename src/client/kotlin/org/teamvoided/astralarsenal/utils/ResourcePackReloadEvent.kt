package org.teamvoided.astralarsenal.utils

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.SynchronousResourceReloader
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.utils.CustomUseAnimation.pageModel

object ResourcePackReloadEvent : SynchronousResourceReloader, IdentifiableResourceReloadListener {

    override fun reload(manager: ResourceManager?) {
        pageModel = null
    }

    override fun getFabricId(): Identifier = id("generic_reload_listener")
}