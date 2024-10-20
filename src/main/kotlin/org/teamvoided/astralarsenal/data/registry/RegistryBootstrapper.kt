package org.teamvoided.astralarsenal.data.registry

import net.minecraft.registry.*
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.AstralArsenal.id
import kotlin.jvm.optionals.getOrNull

abstract class RegistryBootstrapper<V>(val registryKey: RegistryKey<Registry<V>>) {
    protected val toRegister: MutableMap<RegistryKey<V>, Bootstrapper<V>> = mutableMapOf()

    protected fun register(name: String, bootstrapper: Bootstrapper<V>): RegistryKey<V> {
        val key = registryKey(id(name))
        toRegister[key] = bootstrapper
        return key
    }

    protected fun registryKey(id: Identifier) = RegistryKey.of(registryKey, id)
    protected fun mcRegistryKey(path: String) = registryKey(Identifier.of("minecraft", path))

    open fun bootstrap(ctx: BootstrapContext<V>) {
        toRegister.forEach { (key, value) ->
            ctx.register(key, value.bootstrap(ctx))
        }

        toRegister.clear()
    }

    fun getHolder(manager: DynamicRegistryManager, key: RegistryKey<V>): Holder<V>? {
        return manager.get(registryKey).getHolder(key).getOrNull()
    }

    fun interface Bootstrapper<V> {
        fun bootstrap(ctx: BootstrapContext<V>): V
    }
}