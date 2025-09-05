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
import org.teamvoided.astralarsenal.entity.Projectiles.VoidIceShardEntity
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

class FreezeKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_FREEZE) }) {
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        if(target.frozenTicks < 140) target.frozenTicks = 140
        if(target.frozenTicks + 50 < 540) target.frozenTicks += 50
        if (attacker !is PlayerEntity || !attacker.itemCooldownManager.isCoolingDown(stack.item)) {
            if (!target.isAlive) {
                target.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0f, 1.0f)
            } else {
                target.playSound(SoundEvents.BLOCK_POWDER_SNOW_STEP, 1.0f, 1.0f)
            }
            val velocity = target.pos.subtract(attacker.pos).normalize().multiply(0.25)
            repeat(1) {
                val freezeBallEntity = VoidIceShardEntity(target.world, attacker)
                setPropertiesTwo(freezeBallEntity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                freezeBallEntity.addVelocity(velocity)
                freezeBallEntity.setPosition(target.pos.x, target.eyePos.y, target.pos.z)
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