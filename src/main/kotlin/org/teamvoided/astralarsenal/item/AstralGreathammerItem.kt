package org.teamvoided.astralarsenal.item

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterials
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.BeamOfLightEntity
import org.teamvoided.astralarsenal.entity.CannonballEntity

// Please change the summoning functionality into a kosmogliph for cannonball and keep the sound functionality
class AstralGreathammerItem(settings: Item.Settings) : SwordItem(ToolMaterials.NETHERITE, settings) {
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
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
            beam.TIMEACTIVE = 20
            beam.side = 1
            beam.THRUST = 1.0
            attacker.world.spawnEntity(beam)
            hitTimes = 0
        }
        return super.postHit(stack, target, attacker)
    }

    override fun isEnchantable(stack: ItemStack?): Boolean {
        return true
    }
    // I need someone to take all the hitTimes logic, apply it to a kosmogliph,
    // and make it work per itemstack pls thx <3
    var hitTimes = 0
}