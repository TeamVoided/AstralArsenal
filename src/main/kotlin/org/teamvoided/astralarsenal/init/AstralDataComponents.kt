package org.teamvoided.astralarsenal.init

import com.mojang.serialization.Codec
import net.minecraft.component.DataComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.components.*
import org.teamvoided.astralarsenal.item.CometLauncherItem
import org.teamvoided.astralarsenal.item.NailCannonItem
import org.teamvoided.astralarsenal.kosmogliph.armor.*
import org.teamvoided.astralarsenal.kosmogliph.melee.AstralStrikeKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.AlchemistKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.ranged.beams.MinigunKosmogliph

object AstralDataComponents {
    // KosmogliphsComponent()
//    { it is ToolItem || it is ArmorItem || it is RangedWeaponItem || it is RailgunItem || it is ElytraItem || it is TridentItem || it is ShieldItem || it is NailCannonItem || it is MaceItem || it is SupportTotemItem },
    @JvmField
    val KOSMOGLIPHS = register("kosmogliphs") { builder -> builder.codec(KosmogliphsComponent.CODEC).build() }

    // JumpKosmogliph.Data(0, 0, 0, 0)
    val JUMP_DATA = register("jump_data") { builder -> builder.codec(JumpKosmogliph.Data.CODEC).build() }

    // DashKosmogliph.Data(0, 0)
    val DASH_DATA = register("dash_data") { builder -> builder.codec(DashKosmogliph.Data.CODEC).build() }

    // DodgeKosmogliph.Data(0, 0)
    val DODGE_DATA = register("dodge_data") { builder -> builder.codec(DodgeKosmogliph.Data.CODEC).build() }

    // SlamKosmogliph.Data(0f, false)
    val SLAM_DATA = register("slam_data") { builder -> builder.codec(SlamKosmogliph.Data.CODEC).build() }

    //  AstralStrikeKosmogliph.Data(0)
    val ASTRAL_STRIKE_DATA =
        register("astral_strike_data") { builder -> builder.codec(AstralStrikeKosmogliph.Data.CODEC).build() }

    //MinigunKosmogliph.Data(0, 0)
    val MINIGUN_DATA = register("minigun_data") { builder -> builder.codec(MinigunKosmogliph.Data.CODEC).build() }

    // NailCannonItem.Data(0, 0)
    val NAILGUN_DATA = register("nailgun_data") { builder -> builder.codec(NailCannonItem.Data.CODEC).build() }

    //NailCannonItem.CooldownData(0, 0)
    val NAILGUN_COOLDOWN_DATA =
        register("nailgun_cooldown_data") { builder -> builder.codec(NailCannonItem.CooldownData.CODEC).build() }

    // CometLauncherItem.Data(0, 0) }
    val COMET_LAUNCHER_DATA =
        register("comet_launcher_data") { builder -> builder.codec(CometLauncherItem.Data.CODEC).build() }

    // GrappleKosmogliph.Data(0, 0, false)
    val GRAPPLE_DATA = register("grapple_data") { builder -> builder.codec(GrappleKosmogliph.Data.CODEC).build() }

    //AlchemistKosmogliph.Data(Optional.empty(), 0)
    val ALCHEMIST_DATA = register("alchemist_data") { builder -> builder.codec(AlchemistKosmogliph.Data.CODEC).build() }

    val PULVERISER_DATA = registerSimple("pulveriser_data", PulveriserData.CODEC)

    @JvmField
    val ASTRAL_RAIN_DATA = registerSimple("astral_rain_data", AstralRainData.CODEC)

    val SNIPE_DATA_V1 = registerSimple("snipe_data_v1", SnipeDataV1.CODEC)

    val CAPACITANCE_DATA_V1 = registerSimple("capacitance_data_v1", CapacitanceDataV1.CODEC)
    val CAPACITANCE_DATA_V2 = registerSimple("capacitance_data_v2", CapacitanceDataV2.CODEC)

    val TOTEM_DATA = registerSimple("totem_data", TotemData.CODEC)

    fun <T : SimpleStorageComponent> registerSimple(name: String, codec: Codec<T>): DataComponentType<T> =
        Registry.register(Registries.DATA_COMPONENT_TYPE, id(name), DataComponentType.builder<T>().codec(codec).build())

    fun <T> register(
        name: String, build: (DataComponentType.Builder<T>) -> DataComponentType<T>
    ): DataComponentType<T> =
        Registry.register(Registries.DATA_COMPONENT_TYPE, id(name), build(DataComponentType.builder()))

    fun init() {
        /* DefaultItemComponentEvents.MODIFY.register { ctx ->
             mods.forEach { modCtx ->
                 ctx.modify(modCtx.predicate) { builder, item ->
                     modCtx.addToBuilder(builder, item)
                 }
             }
             mods.clear()
         }*/
    }
}