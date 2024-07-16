package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.entity.MorterEntity

object AstralEntities {
    val CANNONBALL_ENTITY = register(
    "cannonball",
    FabricEntityTypeBuilder.create(
        SpawnGroup.MISC,
        EntityType.EntityFactory(:: CannonballEntity)
    )
        .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
        .trackRangeBlocks(4).build()
)
    val MORTER_ENTITY = register(
        "morter",
        FabricEntityTypeBuilder.create(
            SpawnGroup.MISC,
            EntityType.EntityFactory(::MorterEntity)
        )
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .trackRangeBlocks(4).build()
    )

    fun serverInit() {

    }

    private fun <T : Entity> register(path: String, entry: EntityType<T>): EntityType<T> {
        return Registry.register(Registries.ENTITY_TYPE, id(path), entry)
    }
}