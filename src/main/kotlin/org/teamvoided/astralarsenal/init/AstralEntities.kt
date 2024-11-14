package org.teamvoided.astralarsenal.init

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.entity.*
import org.teamvoided.astralarsenal.entity.astralenemies.*
import org.teamvoided.astralarsenal.entity.nails.NailEntity

object AstralEntities {

    fun init() {
        FabricDefaultAttributeRegistry.register(ASTRAL_STRIKER, AstralStrikerEntity.createMobAttributes())
        FabricDefaultAttributeRegistry.register(ASTRAL_SNIPER, AstralSniperEntity.createMobAttributes())
        FabricDefaultAttributeRegistry.register(ASTRAL_SLASHER, AstralSlasherEntity.createMobAttributes())
    }

    val CANNONBALL_ENTITY = register(
        "cannonball",
        EntityType.Builder.create(EntityType.EntityFactory(::CannonballEntity), SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
    )
    val MORTAR_ENTITY = register(
        "mortar",
        EntityType.Builder.create(EntityType.EntityFactory(::MortarEntity), SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
    )
    val BEAM_OF_LIGHT = register(
        "beam_of_light",
        EntityType.Builder.create(EntityType.EntityFactory(::BeamOfLightEntity), SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
    )
    val RICHOCHET = register(
        "richochet",
        EntityType.Builder.create(EntityType.EntityFactory(::RichochetEntity), SpawnGroup.MISC)
            .setDimensions(0.0f, 0.0f).maxTrackingRange(4).build()
    )
    val BLACK_HOLE_ENTITY = register(
        "black_hole",
        EntityType.Builder.create(EntityType.EntityFactory(::BlackHoleEntity), SpawnGroup.MISC)
            .setDimensions(3f, 3f).maxTrackingRange(4).build()
    )
    val BEAM_OF_LIGHT_ARROW = register(
        "beam_of_light_arrow",
        EntityType.Builder.create(EntityType.EntityFactory(::BeamOfLightArrowEntity), SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
    )
    val SLASH_ENTITY = register(
        "slash_entity",
        EntityType.Builder.create(EntityType.EntityFactory(::SlashEntity), SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
    )
    val BULLET_ENTITY = register(
        "bullet_entity",
        EntityType.Builder.create(EntityType.EntityFactory(::BulletEntity), SpawnGroup.MISC)
            .setDimensions(0.25f, 0.25f).maxTrackingRange(4).build()
    )
    val DEEP_WOUND_ENTITY = register(
        "deep_wound_entity",
        EntityType.Builder.create(EntityType.EntityFactory(::DeepWoundEntity), SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
    )
    val BOOM_SHOOTER_ENTITY = register(
        "boom_shooter_entity",
        EntityType.Builder.create(EntityType.EntityFactory(::BoomShooterEntity), SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
    )
    val BOOM_ENTITY = register(
        "boom_entity",
        EntityType.Builder.create(EntityType.EntityFactory(::BoomEntity), SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
    )
    val FLAME_SHOT_ENTITY = register(
        "flame_shot_entity",
        EntityType.Builder.create(EntityType.EntityFactory(::FlameShotEntity), SpawnGroup.MISC)
            .setDimensions(0.25f, 0.25f).maxTrackingRange(4).build()
    )
    val FLAME_THROWER_ENTITY = register(
        "flame_thrower_entity",
        EntityType.Builder.create(EntityType.EntityFactory(::FlameThrowerEntity), SpawnGroup.MISC)
            .setDimensions(0.25f, 0.25f).maxTrackingRange(4).build()
    )
    val FREEZE_SHOT_ENTITY = register(
        "freeze_shot_entity",
        EntityType.Builder.create(EntityType.EntityFactory(::FreezeShotEntity), SpawnGroup.MISC)
            .setDimensions(0.25f, 0.25f).maxTrackingRange(4).build()
    )
    val NAIL_ENTITY = register(
        "nail_entity",
        EntityType.Builder.create(EntityType.EntityFactory(::NailEntity), SpawnGroup.MISC)
            .setDimensions(0.25f, 0.25f).maxTrackingRange(4).build()
    )

    val COMET_ENTITY = register(
        "comet_entity",
        EntityType.Builder.create(EntityType.EntityFactory(::CometEntity), SpawnGroup.MISC)
            .setDimensions(0.75f, 0.75f).maxTrackingRange(4).build()
    )

    val ASTRAL_STRIKER = register(
        "astral_striker",
        EntityType.Builder.create(EntityType.EntityFactory(::AstralStrikerEntity), SpawnGroup.MISC)
            .setDimensions(2f, 2f).maxTrackingRange(100).build()
    )
    val ASTRAL_SNIPER = register(
        "astral_sniper",
        EntityType.Builder.create(EntityType.EntityFactory(::AstralSniperEntity), SpawnGroup.MISC)
            .setDimensions(2f, 2f).maxTrackingRange(100).build()
    )
    val ASTRAL_SLASHER = register(
        "astral_slasher",
        EntityType.Builder.create(EntityType.EntityFactory(::AstralSlasherEntity), SpawnGroup.MISC)
            .setDimensions(1f, 2f).maxTrackingRange(30).build()
    )

    private fun <T : Entity> register(path: String, entry: EntityType<T>): EntityType<T> {
        return Registry.register(Registries.ENTITY_TYPE, id(path), entry)
    }
}