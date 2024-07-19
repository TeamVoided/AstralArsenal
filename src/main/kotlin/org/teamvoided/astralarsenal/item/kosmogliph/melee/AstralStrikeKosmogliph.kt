package org.teamvoided.astralarsenal.item.kosmogliph.melee

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.dynamic.Codecs
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.BeamOfLightEntity
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.armor.JumpKosmogliph.Data

// I will fix this - Astra
class AstralStrikeKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.item is SwordItem }) {
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        val data = stack.get(AstralItemComponents.ASTRAL_STRIKE_DATA) ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        var hitTimes = data.hitTimes
        hitTimes++
            if(hitTimes >= 3){
                val beam = BeamOfLightEntity(attacker.world, attacker)
                beam.setPosition(target.pos)
                beam.WINDUP = 20
                beam.TIMEACTIVE = 200
                beam.side = 5
                beam.THRUST = 1.0
                beam.targetEntity = target
                beam.DOT = true
                beam.DMG = 1
                beam.trackTime = 10
                attacker.world.spawnEntity(beam)
                hitTimes = 0
            }
        stack.set(AstralItemComponents.ASTRAL_STRIKE_DATA, Data(hitTimes))
            return super.postHit(stack, target, attacker)
        }
    var hitTimes = 0
    data class Data(
        val hitTimes: Int,
    ) {
        companion object {
            val CODEC = Codecs.NONNEGATIVE_INT.listOf()
                .xmap(
                    { list -> Data(list[0])},
                    { data -> listOf(data.hitTimes) }
                )
        }
    }
}