package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AFTER_DEATH
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.VindicatorEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.world.ServerWorld
import org.teamvoided.astralarsenal.command.KosmogliphCommand

object FabricEvents {
    fun init() {
        AFTER_DEATH.register { player, source ->
            if ((player.hasStatusEffect(AstralEffects.UNHEALABLE_DAMAGE)
                        && player is PlayerEntity
                        && player.world is ServerWorld
                        && player.world.server != null
                        && !player.world.server!!.isSingleplayer)
                        ||
                        (player is VindicatorEntity
                        && player.world is ServerWorld
                        && player.world.server != null
                        && player.world.server!!.isSingleplayer
                        && player.hasStatusEffect(AstralEffects.UNHEALABLE_DAMAGE)
                        )
            ) {
                player.dropItem(AstralItems.CRYSTALINE_BLOOD)
            }
        }
    }
}
