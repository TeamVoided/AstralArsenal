package org.teamvoided.astralarsenal.item.kosmogliph

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.BeamOfLightEntity
import org.teamvoided.astralarsenal.entity.CannonballEntity
// I will fix this - Astra
class AstralStrikeKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.item is SwordItem }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        if (!world.isClient) {
            val snowballEntity = CannonballEntity(world, player)
            snowballEntity.setProperties(player, player.pitch, player.yaw, 0.0f, 0.1f, 0.0f)
            snowballEntity.addVelocity(0.0, 0.1, 0.0)
            world.spawnEntity(snowballEntity)
            if (!player.isCreative) {
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 100)
            }
        }
    }

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
            target.world.playSound(
                null,
                target.x,
                target.y,
                target.z,
                SoundEvents.ITEM_MACE_SMASH_GROUND,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F
            )
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
            return super.postHit(stack, target, attacker)
        }
    var hitTimes = 0
}