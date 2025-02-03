package org.teamvoided.astralarsenal.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.starsludge.StarSludgeProjectileEntity
import org.teamvoided.astralarsenal.kosmogliph.logic.setShootVelocity

class StarSludgeItem (settings: Settings) : Item(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if(!world.isClient){
            val sludge = StarSludgeProjectileEntity(user.world, user)
            sludge.sludge = StarSludgeProjectileEntity.SludgeFlavour.UNASSIGNED
            sludge.setPosition(user.pos)
            sludge.setShootVelocity(user.pitch, user.yaw, 0.0f, 1f, 0f)
            world.spawnEntity(sludge)
        }
        return super.use(world, user, hand)
    }

}