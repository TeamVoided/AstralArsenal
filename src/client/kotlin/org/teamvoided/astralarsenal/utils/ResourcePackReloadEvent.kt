package org.teamvoided.astralarsenal.utils

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceReloader
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.utils.CustomUseAnimation.pageModel
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

object ResourcePackReloadEvent : IdentifiableResourceReloadListener {
    override fun getFabricId(): Identifier = id("generic_reload_listener")
    override fun reload(
        synchronizer: ResourceReloader.Synchronizer,
        manager: ResourceManager,
        prepareProfiler: Profiler,
        applyProfiler: Profiler,
        prepareExecutor: Executor,
        applyExecutor: Executor,
    ): CompletableFuture<Void> {

        // (ender) here is where it is set
        pageModel = null
        return CompletableFuture()
    }
}