package org.teamvoided.astralarsenal.kosmogliph.melee

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.AstralProjectionEntity
import org.teamvoided.astralarsenal.entity.FlameShotEntity
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

class FlameBurstKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_FLAME_BURST) }) {
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        if (target !is PlayerEntity && (attacker !is PlayerEntity || !attacker.itemCooldownManager.isCoolingDown(stack.item))) {
            val bursts = 2
            repeat(bursts) {
                val random = target.world.random
                val x = if (random.nextInt() == 1) 1 else -1
                val z = if (random.nextInt() == 1) 1 else -1
                val flameBurstEntity = AstralProjectionEntity(target.world, attacker)
                flameBurstEntity.setPosition(Vec3d(target.x + x + (random.nextFloat() * 2) , target.y + 1 + (random.nextFloat() * 2), target.z + z + (random.nextFloat() * 2)))
                flameBurstEntity.countdown = 20 + ((10 + random.rangeInclusive(0,10)) * it)
                println(it)
                flameBurstEntity.owner = attacker
                target.world.spawnEntity(flameBurstEntity)
            }
            if (attacker is PlayerEntity){
                attacker.itemCooldownManager.set(stack.item, 50)
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