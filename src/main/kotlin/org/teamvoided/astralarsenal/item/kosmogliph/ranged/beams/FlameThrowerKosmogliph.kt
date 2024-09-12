package org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.FlameThrowerEntity
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class FlameThrowerKosmogliph(id: Identifier) :
    SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_FLAME_THROWER) }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        if (!world.isClient) {
            for (i in 0..3) {
                val fire = FlameThrowerEntity(world, player)
                setPropertiesTwo(
                    fire,
                    player.pitch,
                    player.yaw,
                    0.0f, 1.0f, 5.0f
                )
                fire.setPosition(player.x, player.y + 1.0, player.z)
                world.spawnEntity(fire)
            }
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.ITEM_FIRECHARGE_USE,
                SoundCategory.PLAYERS,
                1.0F,
                1.0f
            )
        }
    }
//usagetick seems to do nothing, idk what to do here
//
//    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
//        println("ticked")
//        if (!world.isClient) {
//                val fire = FlameThrowerEntity(world, user)
//                setPropertiesTwo(
//                    fire,
//                    user.pitch,
//                    user.yaw,
//                    0.0f, 1.0f, 5.0f
//                )
//                fire.setPosition(user.x, user.y + 1.0, user.z)
//                world.spawnEntity(fire)
//        }
//        super.usageTick(world, user, stack, remainingUseTicks)
//    }
//
//    override fun getUseTicks(stack: ItemStack, livingEntity: LivingEntity): Int {
//        println("ticks used")
//        return 72000
//    }
//
//    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
//        println("used")
//    }
}