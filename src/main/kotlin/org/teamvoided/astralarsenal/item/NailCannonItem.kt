package org.teamvoided.astralarsenal.item

import com.mojang.serialization.Codec
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.util.dynamic.Codecs
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.nails.NailEntity
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.kosmogliph.logic.setShootVelocity
import org.teamvoided.astralarsenal.util.getKosmogliphsOnStack
import org.teamvoided.astralarsenal.util.playSound
import java.awt.Color
import java.lang.Math.clamp
import kotlin.math.round

class NailCannonItem(settings: Settings) : Item(settings) {
    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val data = stack.get(AstralItemComponents.NAILGUN_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        val cooldownData = stack.get(AstralItemComponents.NAILGUN_COOLDOWN_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        var uses = data.uses
        var cooldown = cooldownData.cooldown
        var fireCooldown = cooldownData.fireCooldown
        var beingUsed = data.beingUsed
        if (uses < stack.maxUses() && beingUsed != 1) {
            cooldown--
            if (cooldown <= 0) {
                uses++
                cooldown = stack.cooldown()
            }
        } else if (uses > stack.maxUses()) {
            uses = stack.maxUses()
        } else if (beingUsed == 1) {
            if (entity is LivingEntity && entity.getStackInHand(Hand.MAIN_HAND) != stack && entity.getStackInHand(Hand.OFF_HAND) != stack) {
                beingUsed = 0
            }

        }
        if (fireCooldown > 0) {
            fireCooldown--
        }
        stack.set(AstralItemComponents.NAILGUN_DATA, Data(uses, beingUsed))
        stack.set(AstralItemComponents.NAILGUN_COOLDOWN_DATA, CooldownData(cooldown, fireCooldown))
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    private fun ItemStack.maxUses(): Int {
        return if (getKosmogliphsOnStack(this).contains(AstralKosmogliphs.CAPACITY)) 200 else 100
    }

    private fun ItemStack.cooldown(): Int {
        return if (getKosmogliphsOnStack(this).contains(AstralKosmogliphs.CAPACITY)) 7 else 10
    }

    private fun shouldBoost(remainingUseTicks: Int, isSneaking: Boolean): Boolean {
        val usedTicks = USE_TICKS - remainingUseTicks
        return if (usedTicks >= TICKS_BEFORE_BOOST && !isSneaking) true else false
    }

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        player.setCurrentHand(hand)
        return TypedActionResult(ActionResult.CONSUME_PARTIAL, player.getStackInHand(hand))
    }


    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        val data = stack.get(AstralItemComponents.NAILGUN_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        val cooldownData = stack.get(AstralItemComponents.NAILGUN_COOLDOWN_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        var cooldown = cooldownData.fireCooldown
        if ((data.uses > 0 && cooldown <= 0) || (((USE_TICKS - remainingUseTicks) % 20) == 0 && USE_TICKS - remainingUseTicks > 10 && getKosmogliphsOnStack(
                stack
            ).contains(
                AstralKosmogliphs.STATIC_RELEASE
            ))
        ) {
            if (!world.isClient) {
                val nail = NailEntity(world, user)
                val spread =
                    if (shouldBoost(remainingUseTicks, user.isSneaking)) BOOSTED_SPREAD else SLOW_SPREAD
                val speed =
                    if (shouldBoost(remainingUseTicks, user.isSneaking)) BOOSTED_SPEED else SLOW_SPEED
                nail.setShootVelocity(user.pitch, user.yaw, 0.0f, speed, spread)
                val offset = user.eyePos.add(user.rotationVector.normalize().multiply(0.6))
                nail.setPosition(offset.x, offset.y, offset.z)
                nail.pickupType = PickupPermission.DISALLOWED
                if (shouldBoost(remainingUseTicks, user.isSneaking)
                    && (getKosmogliphsOnStack(stack).contains(AstralKosmogliphs.OVER_HEAT))
                ) nail.nailType = NailEntity.NailType.FIRE
                else if (((USE_TICKS - remainingUseTicks) % 20 == 0) && getKosmogliphsOnStack(stack).contains(
                        AstralKosmogliphs.STATIC_RELEASE
                    )
                ) nail.nailType = NailEntity.NailType.CHARGED
                world.spawnEntity(nail)
                cooldown = if (shouldBoost(remainingUseTicks, user.isSneaking)) BOOSTED_FIRE_INTERVAL else FIRE_INTERVAL
                if (USE_TICKS - remainingUseTicks == 60) {
                    world.playSound(
                        user.pos, SoundEvents.ITEM_TRIDENT_RETURN, SoundCategory.PLAYERS, 1.0F, 1.0f
                    )
                }
            }
            user.bodyYaw = user.yaw
            var uses = data.uses
            if (!(user as PlayerEntity).isCreative) {
                uses--
            }
            stack.set(AstralItemComponents.NAILGUN_DATA, Data(uses, 1))
            stack.set(AstralItemComponents.NAILGUN_COOLDOWN_DATA, CooldownData(cooldownData.cooldown, cooldown))
            world.playSound(user.pos, SoundEvents.BLOCK_VAULT_INSERT_ITEM_FAIL, SoundCategory.PLAYERS, 0.4F, 0.3f)
        } else if (cooldown <= 0) {
            world.playSound(user.pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 2.0f)
            cooldown = 8
            stack.set(AstralItemComponents.NAILGUN_DATA, Data(data.uses, 1))
            stack.set(AstralItemComponents.NAILGUN_COOLDOWN_DATA, CooldownData(cooldownData.cooldown, cooldown))
        }
        super.usageTick(world, user, stack, remainingUseTicks)
    }

    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity?, remainingUseTicks: Int) {
        val data = stack.get(AstralItemComponents.NAILGUN_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        stack.set(AstralItemComponents.NAILGUN_DATA, Data(data.uses, 0))
        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }

    override fun getUseTicks(stack: ItemStack, livingEntity: LivingEntity): Int = USE_TICKS

    override fun getItemBarColor(stack: ItemStack): Int {
        return if (getKosmogliphsOnStack(stack).contains(AstralKosmogliphs.STATIC_RELEASE)) Color.LIGHT_GRAY.rgb
        else if (getKosmogliphsOnStack(stack).contains(AstralKosmogliphs.OVER_HEAT)) Color.ORANGE.rgb
        else Color.MAGENTA.rgb
    }

    override fun getItemBarStep(stack: ItemStack): Int {
        val data = stack.get(AstralItemComponents.NAILGUN_DATA)
        return if (data != null) funnyMath(stack.maxUses() - data.uses, stack.maxUses()) else BAR_LIMIT
    }

    override fun isItemBarVisible(stack: ItemStack): Boolean {
        val data = stack.get(AstralItemComponents.NAILGUN_DATA)
        return data != null && data.uses < stack.maxUses()
    }

    data class Data(
        val uses: Int, val beingUsed: Int
    ) {
        companion object {
            val CODEC = Codecs.NONNEGATIVE_INT.listOf().xmap(
                { list -> Data(list[0], list[1]) },
                { data -> listOf(data.uses, data.beingUsed) }
            )
        }
    }

    data class CooldownData(val cooldown: Int, val fireCooldown: Int) {
        companion object {
            val CODEC: Codec<CooldownData> = Codecs.NONNEGATIVE_INT.listOf().xmap(
                { list -> CooldownData(list[0], list[1]) },
                { data -> listOf(data.cooldown, data.fireCooldown) }
            )
        }
    }

    override fun getUseAction(stack: ItemStack): UseAction = UseAction.BOW

    companion object {
        const val USE_TICKS = 72000

        // boost is the fast firing mode, it activates after NAILS_BEFORE_EXTRA nails have been fired.
        const val TICKS_BEFORE_BOOST = 60
        const val FIRE_INTERVAL = 3
        const val BOOSTED_FIRE_INTERVAL = 1
        const val SLOW_SPREAD = 5.0f
        const val BOOSTED_SPREAD = 10.0f
        const val SLOW_SPEED = 3.0f
        const val BOOSTED_SPEED = 1.5f


        const val BAR_LIMIT = 12
        fun funnyMath(x: Int, y: Int): Int =
            clamp(round(BAR_LIMIT.toFloat() - x * BAR_LIMIT.toFloat() / y).toLong(), 0, BAR_LIMIT)

    }
}
