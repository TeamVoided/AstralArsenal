package org.teamvoided.astralarsenal.item

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.dynamic.Codecs
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.CometEntity
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.kosmogliph.logic.setShootVelocity
import org.teamvoided.astralarsenal.util.getKosmogliphsOnStack
import java.awt.Color
import java.lang.Math.clamp
import kotlin.math.round

class CometLauncherItem(settings: Settings) : Item(settings) {

    val BASE_DAMAGE = 15f
    val BASE_SPEED = 1.5f

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val data = stack.get(AstralDataComponents.COMET_LAUNCHER_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        val kosmogliphs = getKosmogliphsOnStack(stack)
        var cooldown = data.cooldown
        var uses = data.uses
        if(uses < 5){
            cooldown--
            if(cooldown <= 0){
                uses++
                cooldown = if(kosmogliphs.contains(AstralKosmogliphs.GENERATOR)) 200 else 300
            }
        }
        stack.set(AstralDataComponents.COMET_LAUNCHER_DATA, Data(uses, cooldown)
        )
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val data = user.getStackInHand(hand).get(AstralDataComponents.COMET_LAUNCHER_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        val kosmogliphs = getKosmogliphsOnStack(user.getStackInHand(hand))
        var cooldown = data.cooldown
        var uses = data.uses
        if(uses > 0){
            uses--
            val comet = CometEntity(world, user)
            comet.damage = if(kosmogliphs.contains(AstralKosmogliphs.TARGET)) BASE_DAMAGE * (2/3) else if(kosmogliphs.contains(AstralKosmogliphs.QUICKSHOT)) (BASE_DAMAGE * 0.75f) else BASE_DAMAGE
            comet.setNoGravity(true)
            comet.setPosition(user.x, user.eyeY, user.z)
            comet.setShootVelocity(user.pitch, user.yaw, 0f, if(kosmogliphs.contains(AstralKosmogliphs.QUICKSHOT)) (BASE_SPEED * 2f) else BASE_SPEED, 0.25f)
            if(kosmogliphs.contains(AstralKosmogliphs.TARGET)){
                comet.homing = true
            }
            world.spawnEntity(comet)
        }

        user.getStackInHand(hand).set(AstralDataComponents.COMET_LAUNCHER_DATA, Data(uses, cooldown))
        return super.use(world, user, hand)
    }

    override fun getItemBarColor(stack: ItemStack): Int {
        return if (getKosmogliphsOnStack(stack).contains(AstralKosmogliphs.TARGET)) Color.HSBtoRGB(340f, 0.34f, 0.59f)
        else if (getKosmogliphsOnStack(stack).contains(AstralKosmogliphs.QUICKSHOT)) Color.ORANGE.rgb
        else Color.MAGENTA.rgb
    }

    override fun getItemBarStep(stack: ItemStack): Int {
        val data = stack.get(AstralDataComponents.COMET_LAUNCHER_DATA)
        return if (data != null) funnyMath(5 - data.uses, 5) else BAR_LIMIT
    }

    override fun isItemBarVisible(stack: ItemStack): Boolean {
        val data = stack.get(AstralDataComponents.COMET_LAUNCHER_DATA)
        return data != null && data.uses < 5
    }
    val BAR_LIMIT = 10
    fun funnyMath(x: Int, y: Int): Int =
        clamp(round(BAR_LIMIT.toFloat() - x * BAR_LIMIT.toFloat() / y).toLong(), 0, BAR_LIMIT)


    data class Data(
        val uses: Int,
        val cooldown: Int
    ) {
        companion object {
            val CODEC = Codecs.NONNEGATIVE_INT.listOf().xmap(
                { list -> Data(list[0], list[1]) },
                { data -> listOf(data.uses, data.cooldown) }
            )
        }
    }
}