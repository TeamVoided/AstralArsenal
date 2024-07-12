package org.teamvoided.astralarsenal.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.thrown.EnderPearlEntity
import net.minecraft.entity.projectile.thrown.SnowballEntity
import net.minecraft.item.*
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralEntities

class AstralGreathammerItem(settings: Item.Settings) : SwordItem(ToolMaterials.NETHERITE, settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient){
            val snowballEntity = SnowballEntity(world, user)
            snowballEntity.setProperties(user, user.pitch, user.yaw, 0.0f, 1.5f, 1.0f)
            world.spawnEntity(snowballEntity)

        }
        return super.use(world, user, hand)
    }
}