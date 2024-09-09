package org.teamvoided.astralarsenal.item.kosmogliph.melee

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.FlameShotEntity
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class FlameBurstKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_FLAME_BURST) }) {
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        target.setOnFireFor(200)
        if (target !is PlayerEntity) {
            val bursts: Int
            if (!target.isAlive) {
                bursts = 20
                target.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0f, 1.0f)
            } else {
                bursts = 3
                target.playSound(SoundEvents.BLOCK_FIRE_AMBIENT, 1.0f, 1.0f)
            }
            repeat(bursts) {
                val flameBurstEntity = FlameShotEntity(target.world, attacker)
                setPropertiesTwo(flameBurstEntity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                flameBurstEntity.addVelocity(
                    target.random.nextDouble().minus(0.5),
                    target.random.nextDouble().times(0.5),
                    target.random.nextDouble().minus(0.5)
                )
                flameBurstEntity.setPosition(target.pos)
                target.world.spawnEntity(flameBurstEntity)
            }
        }
        super.postHit(stack, target, attacker)
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}