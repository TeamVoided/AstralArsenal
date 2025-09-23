package org.teamvoided.astralarsenal.init

import com.mojang.serialization.Codec
import net.minecraft.component.DataComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.components.*

object AstralDataComponents {
    fun init() = Unit

    @JvmField
    val KOSMOGLIPHS = register("kosmogliphs") { builder -> builder.codec(KosmogliphsComponent.CODEC).build() }

    val JUMP_DATA = registerSimple("jump_data", JumpData.CODEC)

    val DASH_DATA = registerSimple("dash_data", DashData.CODEC)
    val DODGE_DATA = registerSimple("dodge_data", DodgeData.CODEC)

    val SLAM_DATA = registerSimple("slam_data", SlamData.CODEC)

    val ASTRAL_STRIKE_DATA = registerSimple("astral_strike_data", AstralStrikeData.CODEC)

    val NAILGUN_DATA = registerSimple("nailgun_data", NailCannonDataV1.CODEC)
    val NAILGUN_COOLDOWN_DATA = registerSimple("nailgun_cooldown_data", NailCannonCooldownData.CODEC)

    val TOME_OF_HEXES_DATA = registerSimple("tome_of_hexes_data", TomeOfHexesData.CODEC)

    val ALCHEMIST_DATA = registerSimple("alchemist_data", AlchemistData.CODEC)

    val PULVERISER_DATA = registerSimple("pulveriser_data", PulveriserData.CODEC)

    @JvmField
    val ASTRAL_RAIN_DATA = registerSimple("astral_rain_data", AstralRainData.CODEC)

    val SNIPE_DATA_V1 = registerSimple("snipe_data_v1", SnipeDataV1.CODEC)

    // (ender) When Cap is updated v3 should include all of v1 and v2 data and they should be deleted
    val CAPACITANCE_DATA_V1 = registerSimple("capacitance_data_v1", CapacitanceDataV1.CODEC)
    val CAPACITANCE_DATA_V2 = registerSimple("capacitance_data_v2", CapacitanceDataV2.CODEC)

    val ENDURANCE_DATA = registerSimple("endurance_data", EnduranceData.CODEC)

    val TOTEM_DATA = registerSimple("totem_data", TotemData.CODEC)

    val SLUDGE_DATA = registerSimple("sludge_data", SludgeCooldownData.CODEC)

    fun <T : SimpleStorageComponent> registerSimple(name: String, codec: Codec<T>): DataComponentType<T> =
        Registry.register(Registries.DATA_COMPONENT_TYPE, id(name), DataComponentType.builder<T>().codec(codec).build())

    fun <T> register(
        name: String, build: (DataComponentType.Builder<T>) -> DataComponentType<T>
    ): DataComponentType<T> =
        Registry.register(Registries.DATA_COMPONENT_TYPE, id(name), build(DataComponentType.builder()))
}