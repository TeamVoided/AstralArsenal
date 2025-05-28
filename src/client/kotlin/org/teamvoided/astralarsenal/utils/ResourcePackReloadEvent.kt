package org.teamvoided.astralarsenal.utils

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceReloader
import net.minecraft.resource.SynchronousResourceReloader
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.utils.CustomUseAnimation.pageModel
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

object ResourcePackReloadEvent : SynchronousResourceReloader, IdentifiableResourceReloadListener {

    override fun reload(manager: ResourceManager?) {
        pageModel = null
    }

    override fun getFabricId(): Identifier = id("generic_reload_listener")
}