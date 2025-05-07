package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AFTER_DEATH
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandManager.literal
import org.teamvoided.astralarsenal.command.KosmogliphCommand

object FabricEvents {
    fun init() {
        AFTER_DEATH.register { player, source ->
            if (player.hasStatusEffect(AstralEffects.UNHEALABLE_DAMAGE) && player is PlayerEntity) {
                player.dropItem(AstralItems.ASTRAL_GREATHAMMER)
            }
        }
    }
}
