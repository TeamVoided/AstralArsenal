package org.teamvoided.astralarsenal.item.kosmogliph.melee

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class FreezeKosmogliph (id: Identifier) : SimpleKosmogliph(id, { it.item is SwordItem }) {
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        if(target.frozenTicks <= 140){target.frozenTicks += 500}
        else{target.frozenTicks += 100 }
        if(!target.isAlive){
            if (!target.world.isClient) {
                val serverWorld = target.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    target.x,
                    target.y + 1,
                    target.z,
                    250,
                    target.world.random.nextDouble().minus(0.5).times(2),
                    target.world.random.nextDouble().minus(0.5).times(2),
                    target.world.random.nextDouble().minus(0.5).times(2),
                    0.0
                )
                target.playSound(SoundEvents.BLOCK_GLASS_BREAK,1.0f,1.0f)
                val entities = target.world.getOtherEntities(
                    null, Box(
                        target.pos.x + 3,
                        target.pos.y + 3,
                        target.pos.z + 3,
                        target.pos.x - 3,
                        target.pos.y - 3,
                        target.pos.z - 3)
                )
                for (entity in entities) {
                    if(entity != attacker && entity.isLiving){entity.frozenTicks += 500}
                }}
        }
        super.postHit(stack, target, attacker)
    }
}