package org.teamvoided.astralarsenal.item

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.components.NailCannonCooldownData
import org.teamvoided.astralarsenal.components.NailCannonDataV1
import org.teamvoided.astralarsenal.components.SludgeCooldownData
import org.teamvoided.astralarsenal.entity.starsludge.StarSludgeProjectileEntity
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.item.NailCannonItem.Companion
import org.teamvoided.astralarsenal.kosmogliph.logic.setShootVelocity
import org.teamvoided.astralarsenal.util.*
import java.awt.Color
import java.lang.Math.clamp
import kotlin.math.round

class StarSludgeItem(settings: Settings) : Item(settings) {

    val COOLDOWN_TICKS = 900

    fun maxUses(stack: ItemStack): Int {
        return if (!stack.hasKosmogliphs()) 2
        else if (stack.hasKosmogliph(AstralKosmogliphs.STATIC_CORE)) 1
        else 4
    }

    fun setSludgeType(stack: ItemStack, sludge: StarSludgeProjectileEntity) {
        if (stack.hasKosmogliph(AstralKosmogliphs.STATIC_CORE)) {
            sludge.sludge = StarSludgeProjectileEntity.SludgeFlavour.STATIC
        } else if (stack.hasKosmogliph(AstralKosmogliphs.FLAMING_CORE)) {
            sludge.sludge = StarSludgeProjectileEntity.SludgeFlavour.FIRE
        } else if (stack.hasKosmogliph(AstralKosmogliphs.FROZEN_CORE)) {
            sludge.sludge = StarSludgeProjectileEntity.SludgeFlavour.ICE
        } else if (stack.hasKosmogliph(AstralKosmogliphs.ENCHANTED_CORE)) {
            sludge.sludge = StarSludgeProjectileEntity.SludgeFlavour.MAGIC
            val random = sludge.random.rangeInclusive(0, 2)
            sludge.magic = StarSludgeProjectileEntity.MagicEffect.getById(random)
        } else if (stack.hasKosmogliph(AstralKosmogliphs.HEAVY_CORE)) {
            sludge.sludge = StarSludgeProjectileEntity.SludgeFlavour.STRONG
        } else {
            sludge.sludge = StarSludgeProjectileEntity.SludgeFlavour.UNASSIGNED
        }
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        val data = stack.getOrDefault(AstralDataComponents.SLUDGE_DATA, SludgeCooldownData.DEFAULT)
        var uses = data.uses
        if (uses > 0 || user.isCreative) {
            if(uses > 0 ) uses--
            if (world is ServerWorld) {
                val sludgeBomb = StarSludgeProjectileEntity(world, user)
                sludgeBomb.setShootVelocity(user.pitch, user.yaw, 0.0f, 1f, 0f)
                setSludgeType(stack, sludgeBomb)
                sludgeBomb.setPosition(user.x, user.y + 1f, user.z)
                world.spawnEntity(sludgeBomb)
            }
            if(!user.isCreative){
                val cooldown = if (stack.hasKosmogliph(AstralKosmogliphs.STATIC_CORE)) 300 else 40
                user.itemCooldownManager.set(stack.item, cooldown)
            }
        }
        stack.set(AstralDataComponents.SLUDGE_DATA, SludgeCooldownData(data.cooldown, uses))
        return super.use(world, user, hand)
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val data = stack.getOrDefault(AstralDataComponents.SLUDGE_DATA, SludgeCooldownData.DEFAULT)
        var cooldown = data.cooldown
        var uses = data.uses
        if (uses < maxUses(stack)) {
            if (cooldown > COOLDOWN_TICKS) {
                cooldown = 0
                uses += 1
            } else {
                cooldown++
            }
        }
        if(uses < 0) uses = 0
        stack.set(AstralDataComponents.SLUDGE_DATA, SludgeCooldownData(cooldown, uses))
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    override fun getItemBarColor(stack: ItemStack): Int {
        return if (stack.hasKosmogliph(AstralKosmogliphs.STATIC_CORE)) {
            Color.white.rgb
        } else if (stack.hasKosmogliph(AstralKosmogliphs.FLAMING_CORE)) {
            Color.red.rgb
        } else if (stack.hasKosmogliph(AstralKosmogliphs.FROZEN_CORE)) {
            Color.CYAN.rgb
        } else if (stack.hasKosmogliph(AstralKosmogliphs.ENCHANTED_CORE)) {
            Color.HSBtoRGB(280f, 100f, 60f)
        } else if (stack.hasKosmogliph(AstralKosmogliphs.HEAVY_CORE)) {
            Color.GRAY.rgb
        } else {
            Color.GRAY.rgb
        }
    }

    override fun getItemBarStep(stack: ItemStack): Int {
        val data = stack.getOrDefault(AstralDataComponents.SLUDGE_DATA, SludgeCooldownData.DEFAULT)
        if(stack.hasKosmogliph(AstralKosmogliphs.STATIC_CORE)){
            return data?.let {
                funnyMath(
                    COOLDOWN_TICKS - it.cooldown,
                    COOLDOWN_TICKS
                )
            } ?: BAR_LIMIT.toInt()
        }
        return data?.let {
            funnyMath(
                maxUses(stack) - it.uses,
                maxUses(stack)
            )
        } ?: BAR_LIMIT.toInt()
    }

    override fun isItemBarVisible(stack: ItemStack): Boolean {
        val data = stack.getOrDefault(AstralDataComponents.SLUDGE_DATA, SludgeCooldownData.DEFAULT)
        return data != null && data.uses < maxUses(stack)
    }

    companion object {
        const val BAR_LIMIT = 12f
        fun funnyMath(x: Int, y: Int) = clamp(round(BAR_LIMIT - x * BAR_LIMIT / y).toLong(), 0, BAR_LIMIT.toInt())
    }
}