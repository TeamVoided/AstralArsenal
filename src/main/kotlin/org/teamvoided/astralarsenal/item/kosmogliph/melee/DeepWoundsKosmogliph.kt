package org.teamvoided.astralarsenal.item.kosmogliph.melee

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.AxeItem
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class DeepWoundsKosmogliph (id: Identifier) : SimpleKosmogliph(id, { it.item is SwordItem || it.item is AxeItem }) {
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
            if (!target.world.isClient) {
                val x = target.statusEffects.filter { it.effectType == AstralEffects.UNHEALABLE_DAMAGE}
                if (x.isNotEmpty()) {
                    for (balls in x) {
                        target.addStatusEffect(
                            StatusEffectInstance(
                                AstralEffects.UNHEALABLE_DAMAGE,
                                300, balls.amplifier + 1,
                                false, false, false
                            )
                        )
                    }
                }
                else{
                    target.addStatusEffect(
                        StatusEffectInstance(
                            AstralEffects.UNHEALABLE_DAMAGE,
                            300,  0,
                            false, false, false
                        )
                    )
                }
            }
        super.postHit(stack, target, attacker)
    }
}