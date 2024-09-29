package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.component.DataComponentMap
import net.minecraft.component.DataComponentType
import net.minecraft.item.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.item.RailgunItem
import org.teamvoided.astralarsenal.item.components.KosmogliphsComponent
import org.teamvoided.astralarsenal.item.kosmogliph.armor.*
import org.teamvoided.astralarsenal.item.kosmogliph.melee.AstralStrikeKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.AlchemistKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.BeamKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams.MinigunKosmogliph
import java.util.*

object AstralItemComponents {
    private val mods = mutableSetOf<DataComponentTypeModificationContext<*>>()

    @JvmField
    val KOSMOGLIPHS: DataComponentType<KosmogliphsComponent> =
        register(
            "kosmogliphs",
            { it is ToolItem || it is ArmorItem || it is RangedWeaponItem || it is RailgunItem || it is ElytraItem || it is TridentItem || it is ShieldItem},
            { KosmogliphsComponent() }) { builder ->
            builder.codec(KosmogliphsComponent.CODEC).build()
        }

    val JUMP_DATA: DataComponentType<JumpKosmogliph.Data> =
        register("jump_data", { it is ArmorItem || it is ToolItem }, { JumpKosmogliph.Data(0, 0, 0, 0) }) { builder ->
            builder.codec(JumpKosmogliph.Data.CODEC).build()
        }

    val DASH_DATA: DataComponentType<DashKosmogliph.Data> =
        register("dash_data", { it is ArmorItem || it is ToolItem }, { DashKosmogliph.Data(0, 0) }) { builder ->
            builder.codec(DashKosmogliph.Data.CODEC).build()
        }

    val DODGE_DATA: DataComponentType<DodgeKosmogliph.Data> =
        register("dodge_data", { it is ArmorItem || it is ToolItem }, { DodgeKosmogliph.Data(0, 0) }) { builder ->
            builder.codec(DodgeKosmogliph.Data.CODEC).build()
        }

    val SLAM_DATA: DataComponentType<SlamKosmogliph.Data> =
        register("slam_data", { it is ArmorItem }, { SlamKosmogliph.Data(0f, false) }) { builder ->
            builder.codec(SlamKosmogliph.Data.CODEC).build()
        }

    val ASTRAL_STRIKE_DATA: DataComponentType<AstralStrikeKosmogliph.Data> =
        register(
            "astral_strike_data",
            { it is ArmorItem || it is ToolItem },
            { AstralStrikeKosmogliph.Data(0) }) { builder ->
            builder.codec(AstralStrikeKosmogliph.Data.CODEC).build()
        }
    val MINIGUN_DATA: DataComponentType<MinigunKosmogliph.Data> =
        register("minigun_data", { it is RailgunItem }, { MinigunKosmogliph.Data(0, 0) }) { builder ->
            builder.codec(MinigunKosmogliph.Data.CODEC).build()
        }

    val GRAPPLE_DATA: DataComponentType<GrappleKosmogliph.Data> =
        register(
            "grapple_data",
            { it is ArmorItem || it is ToolItem },
            { GrappleKosmogliph.Data(0, 0, false) }) { builder ->
            builder.codec(GrappleKosmogliph.Data.CODEC).build()
        }

    val ALCHEMIST_DATA: DataComponentType<AlchemistKosmogliph.Data> =
        register("alchemist_data", { it is BowItem }, { AlchemistKosmogliph.Data(Optional.empty(), 0) }) { builder ->
            builder.codec(AlchemistKosmogliph.Data.CODEC).build()
        }

    //(ender) Delete this?
    val BEAM_DATA: DataComponentType<BeamKosmogliph.Data> =
        register("beam_data", { it is CrossbowItem }, { BeamKosmogliph.Data(false) }) { builder ->
            builder.codec(BeamKosmogliph.Data.CODEC).build()
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