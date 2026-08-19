package org.teamvoided.astralarsenal.kosmogliph.melee

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.AstralProjectionEntity
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

class FlameBurstKosmogliph(id: Identifier) : SimpleKosmogliph(id, AstralItemTags.SUPPORTS_FLAME_BURST) {
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        if (attacker !is PlayerEntity || !attacker.itemCooldownManager.isCoolingDown(stack.item)) {
            val bursts = 4
            repeat(bursts) {
                val random = target.world.random
                val flameBurstEntity = AstralProjectionEntity(target.world, attacker)
                flameBurstEntity.setPosition(Vec3d(target.x + ((random.nextFloat() - 0.5f) * 4) , target.y + 1 + ((random.nextFloat() - 0.5f) * 2), target.z + ((random.nextFloat() - 0.5f) * 4)))
                flameBurstEntity.countdown = 20 + ((10 + random.rangeInclusive(0,10)) * it)
                flameBurstEntity.owner = attacker
                target.world.spawnEntity(flameBurstEntity)
            }
            if (attacker is PlayerEntity) {
                attacker.itemCooldownManager.set(stack.item, 100)
            }
        }
        super.postHit(stack, target, attacker)
    }
}