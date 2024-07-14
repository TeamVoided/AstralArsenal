package org.teamvoided.astralarsenal.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.thrown.EnderPearlEntity
import net.minecraft.entity.projectile.thrown.SnowballEntity
import net.minecraft.item.*
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.init.AstralEntities
// Please change the summoning functionality into a kosmogliph for cannonball
//Incomplete, still needs to push the cannonball up and in front a small ammount
class AstralGreathammerItem(settings: Item.Settings) : SwordItem(ToolMaterials.NETHERITE, settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient){
            val snowballEntity = CannonballEntity(world, user)
            snowballEntity.setProperties(user, user.pitch, user.yaw, 0.0f, 0.0f, 0.0f)
            world.spawnEntity(snowballEntity)
            user.itemCooldownManager.set(this, 100)
        }
        return super.use(world, user, hand)
    }
}