package org.teamvoided.astralarsenal.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.entity.damage.DamageType
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.DamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import java.util.concurrent.CompletableFuture

class AstralDamageTypeTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<DamageType>(o, RegistryKeys.DAMAGE_TYPE, r) {

    override fun configure(wrapperLookup: HolderLookup.Provider) {
        getOrCreateTagBuilder(AstralDamageTypeTags.IS_MAGIC)
            .add(DamageTypes.DRAGON_BREATH)
            .add(DamageTypes.INDIRECT_MAGIC)
            .add(DamageTypes.MAGIC)
            .add(DamageTypes.WITHER)
            .add(AstralDamageTypes.BLEED)

        getOrCreateTagBuilder(AstralDamageTypeTags.IS_FIRE)
            .forceAddTag(DamageTypeTags.IS_FIRE)

        getOrCreateTagBuilder(AstralDamageTypeTags.IS_ICE)
            .add(DamageTypes.FREEZE)

        getOrCreateTagBuilder(AstralDamageTypeTags.IS_MELEE)
            .add(DamageTypes.THORNS)
            .add(DamageTypes.STING)
            .add(DamageTypes.CACTUS)
            .add(DamageTypes.FLY_INTO_WALL)
            .add(DamageTypes.FALLING_ANVIL)
            .add(DamageTypes.FALLING_BLOCK)
            .add(DamageTypes.PLAYER_ATTACK)
            .add(DamageTypes.FALLING_STALACTITE)
            .add(DamageTypes.MOB_ATTACK)
            .add(DamageTypes.MOB_ATTACK_NO_AGGRO)
            .add(DamageTypes.STING)

        getOrCreateTagBuilder(AstralDamageTypeTags.IS_PLASMA)
            .add(DamageTypes.LIGHTNING_BOLT)
            .add(AstralDamageTypes.BEAM_OF_LIGHT)


        getOrCreateTagBuilder(AstralDamageTypeTags.IS_EXPLOSION)
            .forceAddTag(DamageTypeTags.IS_EXPLOSION)
            .add(DamageTypes.FIREWORKS)
            .add(DamageTypes.SONIC_BOOM)
            .add(DamageTypes.WITHER_SKULL)
            .add(DamageTypes.PLAYER_EXPLOSION)
            .add(DamageTypes.EXPLOSION)
            .add(DamageTypes.BAD_RESPAWN_POINT)

        getOrCreateTagBuilder(AstralDamageTypeTags.IS_PROJECTILE)
            .add(DamageTypes.MOB_PROJECTILE)
            .add(DamageTypes.ARROW)
            .add(DamageTypes.LLAMA_SPIT)
            .add(DamageTypes.WIND_CHARGE)
            .add(AstralDamageTypes.CANNONBALL)

        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR)
            .add(AstralDamageTypes.BEAM_OF_LIGHT)
            .add(AstralDamageTypes.BLEED)

        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_SHIELD)
            .add(AstralDamageTypes.BEAM_OF_LIGHT)
    }
}