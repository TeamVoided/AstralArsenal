package org.teamvoided.astralarsenal.kosmogliph.melee

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.FreezeShotEntity
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

class FreezeKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_FREEZE) }) {
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        target.frozenTicks += 340

        val bursts: Int
        if (target !is PlayerEntity || !target.isAlive) {
            if (!target.isAlive) {
                bursts = 20
                target.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0f, 1.0f)
            } else {
                bursts = 3
                target.playSound(SoundEvents.BLOCK_POWDER_SNOW_STEP, 1.0f, 1.0f)
            }
            repeat(bursts) {
                val freezeBallEntity = FreezeShotEntity(target.world, attacker)
                setPropertiesTwo(freezeBallEntity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                freezeBallEntity.addVelocity(
                    target.random.nextDouble().minus(0.5),
                    target.random.nextDouble().times(0.5),
                    target.random.nextDouble().minus(0.5)
                )
                freezeBallEntity.setPosition(target.pos)
                target.world.spawnEntity(freezeBallEntity)
            }
        }
        super.postHit(stack, target, attacker)
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.FIRE_ASPECT)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}