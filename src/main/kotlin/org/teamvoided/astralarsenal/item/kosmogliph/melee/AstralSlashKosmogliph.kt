package org.teamvoided.astralarsenal.item.kosmogliph.melee

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.SwordItem
import net.minecraft.registry.RegistryKey
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.coroutine.mcCoroutineTask
import org.teamvoided.astralarsenal.coroutine.ticks
import org.teamvoided.astralarsenal.entity.SlashEntity
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.AstralGreathammerItem
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import kotlin.time.Duration.Companion.seconds

class AstralSlashKosmogliph (id: Identifier) : SimpleKosmogliph(id, { it.item is SwordItem && it.item !is AstralGreathammerItem }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        if (!world.isClient) {
            var w = -20
            repeat(40){
                val snowballEntity = SlashEntity(world, player)
                snowballEntity.setDmg(3f)
                snowballEntity.setProperties(player, player.pitch, player.yaw + w, 0.0f, 2.0f, 0.0f)
                world.spawnEntity(snowballEntity)
                w++
            }
            world.playSound(null,
                player.x,
                player.y,
                player.z,
                SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
                SoundCategory.PLAYERS,
                1.0F,
                1.0f)
            world.playSound(null,
                player.x,
                player.y,
                player.z,
                SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE,
                SoundCategory.PLAYERS,
                1.0F,
                1.0f)
            if (!player.isCreative) {
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 200)
            }
        }
    }
    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}