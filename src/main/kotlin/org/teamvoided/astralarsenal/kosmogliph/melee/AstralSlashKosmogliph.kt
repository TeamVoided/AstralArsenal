package org.teamvoided.astralarsenal.kosmogliph.melee

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.SlashEntity
import org.teamvoided.astralarsenal.item.AstralGreathammerItem
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

class AstralSlashKosmogliph(id: Identifier) :
    SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_ASTRAL_SLASH) && it.item !is AstralGreathammerItem }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack>? {
        if (!world.isClient) {
            var w = -20
            repeat(40) {
                val snowballEntity = SlashEntity(world, player)
                snowballEntity.setDmg(5f)
                setPropertiesTwo(snowballEntity, player.pitch, player.yaw + w, 0.0f, 2.0f, 0.0f)
                world.spawnEntity(snowballEntity)
                w++
            }
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
                SoundCategory.PLAYERS,
                1.0F,
                1.0f
            )
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE,
                SoundCategory.PLAYERS,
                1.0F,
                1.0f
            )
            if (!player.isCreative) {
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 200)
            }
        }
        return null
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}