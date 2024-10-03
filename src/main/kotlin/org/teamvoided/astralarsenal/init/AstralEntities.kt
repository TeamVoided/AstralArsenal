package org.teamvoided.astralarsenal.init

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.entity.*

object AstralEntities {

    fun init() = Unit

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

    private fun <T : Entity> register(path: String, entry: EntityType<T>): EntityType<T> {
        return Registry.register(Registries.ENTITY_TYPE, id(path), entry)
    }
}