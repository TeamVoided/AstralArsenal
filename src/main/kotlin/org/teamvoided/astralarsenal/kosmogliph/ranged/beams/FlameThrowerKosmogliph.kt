package org.teamvoided.astralarsenal.kosmogliph.ranged.beams

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.FlameThrowerEntity
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

class FlameThrowerKosmogliph(id: Identifier) :
    SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_FLAME_THROWER) }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
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

        player.setCurrentHand(hand)
        return TypedActionResult(ActionResult.CONSUME_PARTIAL, player.getStackInHand(hand))
    }

    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        if (!world.isClient) {
            val fire = FlameThrowerEntity(world, user)
            setPropertiesTwo(
                fire,
                user.pitch,
                user.yaw,
                0.0f, 1.0f, 5.0f
            )
            val offset = user.eyePos.add(user.rotationVector.normalize().multiply(0.6))
            fire.setPosition(offset.x, offset.y, offset.z)
            world.spawnEntity(fire)
        }
        user.bodyYaw = user.yaw
        super.usageTick(world, user, stack, remainingUseTicks)
    }

    override fun getUseTicks(stack: ItemStack, livingEntity: LivingEntity): Int {
        return 72000
    }
}