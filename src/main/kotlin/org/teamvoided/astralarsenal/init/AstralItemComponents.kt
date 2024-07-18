package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.component.DataComponentMap
import net.minecraft.component.DataComponentType
import net.minecraft.item.ArmorItem
import net.minecraft.item.Item
import net.minecraft.item.ToolItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.item.components.KosmogliphsComponent
import org.teamvoided.astralarsenal.item.kosmogliph.JumpKosmogliph

object AstralItemComponents {
    private val mods = mutableSetOf<DataComponentTypeModificationContext<*>>()

    val KOSMOGLIPHS: DataComponentType<KosmogliphsComponent> =
        register("kosmogliphs", { it is ToolItem || it is ArmorItem }, { KosmogliphsComponent() }) { builder ->
            builder.codec(KosmogliphsComponent.CODEC).build()
        }

    val JUMP_DATA: DataComponentType<JumpKosmogliph.Data> =
        register("jump_data", { it is ArmorItem || it is ToolItem }, { JumpKosmogliph.Data(0, 0, 0) }) { builder ->
            builder.codec(JumpKosmogliph.Data.CODEC).build()
        }

    fun <T> register(
        name: String,
        predicate: (Item) -> Boolean,
        valueProvider: (Item) -> T,
        build: (DataComponentType.Builder<T>) -> DataComponentType<T>
    ): DataComponentType<T> {
        val type = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            AstralArsenal.id(name),
            build(DataComponentType.builder())
        )

        mods.add(DataComponentTypeModificationContext(type, predicate, valueProvider))

        return type
    }

    init {
        DefaultItemComponentEvents.MODIFY.register { ctx ->
            mods.forEach { modCtx ->
                ctx.modify(modCtx.predicate) { builder, item ->
                    modCtx.addToBuilder(builder, item)
                }
            }

            mods.clear()
        }
    }

    data class DataComponentTypeModificationContext<T>(
        val type: DataComponentType<T>,
        val predicate: (Item) -> Boolean,
        val valueProvider: (Item) -> T,
    ) {
        fun addToBuilder(builder: DataComponentMap.Builder, item: Item) {
            builder.put(type, valueProvider(item))
        }
    }
}