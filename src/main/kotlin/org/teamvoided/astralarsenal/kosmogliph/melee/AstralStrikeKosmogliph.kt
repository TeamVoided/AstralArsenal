package org.teamvoided.astralarsenal.kosmogliph.melee

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.BeamsOfLight.BeamOfLightEntity
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

class AstralStrikeKosmogliph(id: Identifier) : SimpleKosmogliph(id, AstralItemTags.SUPPORTS_ASTRAL_STRIKE) {
    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> = listOf(Enchantments.FIRE_ASPECT)
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        if (attacker is PlayerEntity && !attacker.itemCooldownManager.isCoolingDown(stack.item)) {
            BeamOfLightEntity(attacker.world, attacker).apply {
                setPosition(target.pos)
                WINDUP = 20
                TIMEACTIVE = 10
                side = 2
                THRUST = 1.0
                targetEntity = target
                DOT = false
                DMG = 5
                trackTime = 15
                hard_damage = 0
                attacker.world.spawnEntity(this)
            }
            attacker.itemCooldownManager.set(stack.item, 200)
        }
        return super.postHit(stack, target, attacker)
    }
}