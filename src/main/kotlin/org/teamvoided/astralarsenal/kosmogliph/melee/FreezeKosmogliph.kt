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
import org.teamvoided.astralarsenal.entity.Projectiles.VoidIceShardEntity
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

class FreezeKosmogliph(id: Identifier) : SimpleKosmogliph(id, AstralItemTags.SUPPORTS_FREEZE) {
    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> = listOf(Enchantments.FIRE_ASPECT)
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        if (target.frozenTicks < 140) target.frozenTicks = 140
        if (target.frozenTicks + 50 < 540) target.frozenTicks += 50
        if (attacker !is PlayerEntity || !attacker.itemCooldownManager.isCoolingDown(stack.item)) {
            if (!target.isAlive) {
                target.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0f, 1.0f)
            } else {
                target.playSound(SoundEvents.BLOCK_POWDER_SNOW_STEP, 1.0f, 1.0f)
            }
        }
        super.postHit(stack, target, attacker)
    }
}