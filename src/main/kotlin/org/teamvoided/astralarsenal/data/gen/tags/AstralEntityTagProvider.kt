package org.teamvoided.astralarsenal.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.entity.EntityType
import net.minecraft.registry.HolderLookup
import org.teamvoided.astralarsenal.data.tags.AstralEntityTags
import org.teamvoided.astralarsenal.init.AstralEntities
import java.util.concurrent.CompletableFuture

class AstralEntityTagProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricTagProvider.EntityTypeTagProvider(output, registriesFuture) {
    override fun configure(wrapperLookup: HolderLookup.Provider) {
        getOrCreateTagBuilder(AstralEntityTags.MOUNTS_WITH_DASH)
            .add(EntityType.CAMEL)
            .add(EntityType.HORSE)
            .add(EntityType.SKELETON_HORSE)
            .add(EntityType.DONKEY)
            .add(EntityType.MULE)
            .add(EntityType.ZOMBIE_HORSE)

        getOrCreateTagBuilder(AstralEntityTags.PROTECTED_FROM_DEL)
            .add(EntityType.TRIDENT)
            .add(EntityType.ENDER_PEARL)
            .add(EntityType.EYE_OF_ENDER)
            //.add(AstralEntities.NAIL_ENTITY)

        getOrCreateTagBuilder(AstralEntityTags.UNAFFECTED_BY_LIGHT)
//            .add(AstralEntities.ASTRAL_STRIKER)

        getOrCreateTagBuilder(AstralEntityTags.WEAK_PARRYABLES)
            .add(EntityType.ARROW)
            .add(EntityType.EGG)
            .add(EntityType.EXPERIENCE_BOTTLE)
            .add(EntityType.SMALL_FIREBALL)
            .add(EntityType.SNOWBALL)
            .add(EntityType.SPECTRAL_ARROW)
            .add(EntityType.WIND_CHARGE)
            .add(EntityType.BREEZE_WIND_CHARGE)
            .add(AstralEntities.FLAME_SHOT_ENTITY)
            .add(AstralEntities.FREEZE_SHOT_ENTITY)
            .add(AstralEntities.NAIL_ENTITY)

        getOrCreateTagBuilder(AstralEntityTags.STRONG_PARRYABLES)
            .add(AstralEntities.CANNONBALL_ENTITY)
            .add(EntityType.FIREWORK_ROCKET)
            .add(EntityType.FIREBALL)
            .add(AstralEntities.BEAM_OF_LIGHT_ARROW)
            .add(AstralEntities.SLASH_ENTITY)
            .add(AstralEntities.DEEP_WOUND_ENTITY)

        getOrCreateTagBuilder(AstralEntityTags.VERY_STRONG_PARRYABLES)
            .add(EntityType.POTION)
            .add(EntityType.LLAMA_SPIT)
    }
}