package org.teamvoided.astralarsenal.init

import com.mojang.serialization.Codec
import net.minecraft.component.DataComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.components.*
import org.teamvoided.astralarsenal.kosmogliph.armor.DashKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.armor.DodgeKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.armor.JumpKosmogliph

object AstralDataComponents {
    // KosmogliphsComponent()
//    { it is ToolItem || it is ArmorItem || it is RangedWeaponItem || it is RailgunItem || it is ElytraItem || it is TridentItem || it is ShieldItem || it is NailCannonItem || it is MaceItem || it is SupportTotemItem },
    @JvmField
    val KOSMOGLIPHS = register("kosmogliphs") { builder -> builder.codec(KosmogliphsComponent.CODEC).build() }

    // JumpKosmogliph.Data(0, 0, 0, 0)
    val JUMP_DATA = register("jump_data") { builder -> builder.codec(JumpKosmogliph.Data.CODEC).build() }

    // DashKosmogliph.Data(0, 0)
    val DASH_DATA = register("dash_data") { builder -> builder.codec(DashKosmogliph.Data.CODEC).build() }

    val DODGE_DATA = registerSimple("dodge_data", DodgeData.CODEC)

    val SLAM_DATA = registerSimple("slam_data", SlamData.CODEC)

    val ASTRAL_STRIKE_DATA = registerSimple("astral_strike_data", AstralStrikeData.CODEC)

    val MINIGUN_DATA = registerSimple("minigun_data", MinigunData.CODEC)

    val NAILGUN_DATA = registerSimple("nailgun_data", NailCannonDataV1.CODEC)
    val NAILGUN_COOLDOWN_DATA = registerSimple("nailgun_cooldown_data", NailCannonCooldownData.CODEC)

    val COMET_LAUNCHER_DATA = registerSimple("comet_launcher_data", CometLauncherData.CODEC)

    val GRAPPLE_DATA = registerSimple("grapple_data", GrappleData.CODEC)

    val ALCHEMIST_DATA = registerSimple("alchemist_data", AlchemistData.CODEC)

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