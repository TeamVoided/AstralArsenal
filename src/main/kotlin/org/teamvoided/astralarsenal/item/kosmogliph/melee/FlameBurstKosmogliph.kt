package org.teamvoided.astralarsenal.item.kosmogliph.melee

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class FlameBurstKosmogliph (id: Identifier) : SimpleKosmogliph(id, { it.item is SwordItem }) {
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        target.setOnFireFor(200)
        if(!target.isAlive){
            if (!target.world.isClient) {
                val serverWorld = target.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.FLAME,
                    target.x,
                    target.y + 1,
                    target.z,
                    250,
                    target.world.random.nextDouble().minus(0.5).times(2),
                    target.world.random.nextDouble().minus(0.5).times(2),
                    target.world.random.nextDouble().minus(0.5).times(2),
                    0.0
                )
                target.playSound(SoundEvents.ITEM_FIRECHARGE_USE,1.0f,1.0f)
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
                        if(entity != attacker && entity.isLiving){entity.setOnFireFor(60)}
                }}
        }
        super.postHit(stack, target, attacker)
    }
}