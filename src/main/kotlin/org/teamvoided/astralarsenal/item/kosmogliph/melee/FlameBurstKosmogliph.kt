package org.teamvoided.astralarsenal.item.kosmogliph.melee

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.LivingEntity
import net.minecraft.item.AxeItem
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.entity.FlameShotEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class FlameBurstKosmogliph (id: Identifier) : SimpleKosmogliph(id, { it.item is SwordItem || it.item is AxeItem }) {
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        target.setOnFireFor(200)
        val bursts: Int
        if(!target.isAlive){
            bursts = 20
            target.playSound(SoundEvents.ITEM_FIRECHARGE_USE,1.0f,1.0f)
        }
        else{
            bursts = 3
            target.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH,1.0f,1.0f)
        }
        repeat(bursts){
            val snowballEntity = FlameShotEntity(target.world, attacker)
            snowballEntity.setProperties(target, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
            snowballEntity.addVelocity(target.random.nextDouble().minus(0.5), target.random.nextDouble().times(0.5), target.random.nextDouble().minus(0.5))
            snowballEntity.setPosition(target.pos)
            target.world.spawnEntity(snowballEntity)
        }
        super.postHit(stack, target, attacker)
    }
    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.FIRE_ASPECT)
    }
}