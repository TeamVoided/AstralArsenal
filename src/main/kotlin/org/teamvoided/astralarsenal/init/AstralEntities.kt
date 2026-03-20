package org.teamvoided.astralarsenal.init

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.entity.*
import org.teamvoided.astralarsenal.entity.BeamsOfLight.BeamOfLightArrowEntity
import org.teamvoided.astralarsenal.entity.BeamsOfLight.BeamOfLightEntity
import org.teamvoided.astralarsenal.entity.Projectiles.CannonballEntity
import org.teamvoided.astralarsenal.entity.Projectiles.NailEntity
import org.teamvoided.astralarsenal.entity.Projectiles.VoidIceShardEntity
import org.teamvoided.astralarsenal.entity.entitiesThatAreJustHereCauseOtherShitDontWork.RichochetEntity

object AstralEntities {
    fun init() = Unit

    val CANNONBALL_ENTITY = register(
        "cannonball",
        EntityType.Builder.create(EntityType.EntityFactory(::CannonballEntity), SpawnGroup.MISC)
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
    val DEEP_WOUND_ENTITY = register(
        "deep_wound_entity",
        EntityType.Builder.create(EntityType.EntityFactory(::DeepWoundEntity), SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
    )
    val FLAME_SHOT_ENTITY = register(
        "flame_shot_entity",
        EntityType.Builder.create(EntityType.EntityFactory(::FlameShotEntity), SpawnGroup.MISC)
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
    val BEAM_RENDERER = register(
        "beam_renderer",
        EntityType.Builder.create(EntityType.EntityFactory(::BeamRenderEntity), SpawnGroup.MISC)
            .setDimensions(0.0f, 0.0f).maxTrackingRange(4).build()
    )
    val CONDUCTIVITY_ENGINE = register(
        "conductivity_engine",
        EntityType.Builder.create(EntityType.EntityFactory(::BeamRenderEntity), SpawnGroup.MISC)
            .setDimensions(0.0f, 0.0f).maxTrackingRange(4).build()
    )
    val ASTRAL_PROJECTION = register(
        "astral_projection",
        EntityType.Builder.create(EntityType.EntityFactory(::AstralProjectionEntity), SpawnGroup.MISC)
            .setDimensions(0.2f, 0.2f).maxTrackingRange(4).build()
    )
    val VOID_SHARD = register(
        "void_shard",
        EntityType.Builder.create(EntityType.EntityFactory(::VoidIceShardEntity), SpawnGroup.MISC)
            .setDimensions(0.2f, 0.2f).maxTrackingRange(4).build()
    )
//    val TOTEM_PROJECTILE = register(
//        "totem_projectile",
//        EntityType.Builder.create(EntityType.EntityFactory(::TotemProjectileEntity), SpawnGroup.MISC)
//            .setDimensions(0.2f, 0.2f).maxTrackingRange(4).build()
//    )

    private fun <T : Entity> register(path: String, entry: EntityType<T>): EntityType<T> {
        return Registry.register(Registries.ENTITY_TYPE, id(path), entry)
    }
}