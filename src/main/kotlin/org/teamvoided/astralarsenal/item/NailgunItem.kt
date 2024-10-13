package org.teamvoided.astralarsenal.item

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.dynamic.Codecs
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.nails.BaseNailEntity
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.util.getKosmogliphsOnStack

class NailgunItem(settings: Settings) : Item(settings) {
    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val data = stack.get(AstralItemComponents.NAILGUN_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        var uses = data.uses
        var cooldown = data.cooldown
        var firecooldown = data.firecooldown
        if (uses < setMaxUses(stack) && data.beingUsed != 1) {
            cooldown--
            if (cooldown <= 0) {
                uses++
                cooldown = setCooldown(stack)
            }
        }
        if (firecooldown > 0) {
            firecooldown--
        }
        stack.set(
            AstralItemComponents.NAILGUN_DATA,
            Data(uses, cooldown, firecooldown, data.beingUsed, data.usesSoFar)
        )
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    fun setMaxUses(stack: ItemStack): Int {
        return if (getKosmogliphsOnStack(stack).contains(AstralKosmogliphs.CAPACITY)) 100
        else 50
    }

    fun setCooldown(stack: ItemStack): Int{
        return if (getKosmogliphsOnStack(stack).contains(AstralKosmogliphs.CAPACITY)) 7
        else 10
    }

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack>? {
        player.setCurrentHand(hand)
        return TypedActionResult(ActionResult.CONSUME_PARTIAL, player.getStackInHand(hand))
    }

    data class Data(
        val uses: Int,
        val cooldown: Int,
        val firecooldown: Int,
        val beingUsed: Int, //please change this to a boolean ender, it doesn't like when I do it
        val usesSoFar: Int
    ) {
        companion object {
            val CODEC = Codecs.NONNEGATIVE_INT.listOf()
                .xmap(
                    { list -> Data(list[0], list[1], list[2], list[3], list[4]) },
                    { data -> listOf(data.uses, data.cooldown, data.firecooldown, data.beingUsed, data.usesSoFar) }
                )
        }
    }

    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        val data = stack.get(AstralItemComponents.NAILGUN_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        var cooldown = data.firecooldown
        if (data.uses > 0 && cooldown <= 0) {
            if (!world.isClient) {
                val nail = BaseNailEntity(world, user)
                setPropertiesTwo(
                    nail,
                    user.pitch,
                    user.yaw,
                    0.0f, 3.0f, 5.0f
                )
                val offset = user.eyePos.add(user.rotationVector.normalize().multiply(0.6))
                nail.setPosition(offset.x, offset.y - 0.5, offset.z)
                nail.pickupType = PickupPermission.DISALLOWED
                if(data.usesSoFar >= 10 && (getKosmogliphsOnStack(stack).contains(AstralKosmogliphs.OVER_HEAT))){
                    nail.fireNail = true
                }
                world.spawnEntity(nail)
                cooldown = 2
            }
            user.bodyYaw = user.yaw
            var uses = data.uses
            if(!(user as PlayerEntity).isCreative){
                uses--
            }
            stack.set(
                AstralItemComponents.NAILGUN_DATA,
                Data(uses, data.cooldown, cooldown, 1, data.usesSoFar + 1)
            )
        } else if (cooldown <= 0) {
            world.playSound(
                null,
                user.x,
                user.y,
                user.z,
                SoundEvents.ITEM_FLINTANDSTEEL_USE,
                SoundCategory.PLAYERS,
                1.0F,
                1.0f
            )
            cooldown = 8
            stack.set(
                AstralItemComponents.NAILGUN_DATA,
                Data(data.uses, data.cooldown, cooldown, 1, 0)
            )
        }
        super.usageTick(world, user, stack, remainingUseTicks)
    }

    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity?, remainingUseTicks: Int) {
        val data = stack.get(AstralItemComponents.NAILGUN_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        stack.set(
            AstralItemComponents.NAILGUN_DATA,
            Data(data.uses, data.cooldown, data.firecooldown, 0, 0)
        )
        if (!(user as PlayerEntity).isCreative) {
            user.itemCooldownManager.set(stack.item, 20)
        }
        if ((getKosmogliphsOnStack(stack).contains(AstralKosmogliphs.STATIC_RELEASE))){
            val nail = BaseNailEntity(world, user)
            setPropertiesTwo(
                nail,
                user.pitch,
                user.yaw,
                0.0f, 3.0f, 5.0f
            )
            val offset = user.eyePos.add(user.rotationVector.normalize().multiply(0.6))
            nail.setPosition(offset.x, offset.y - 0.5, offset.z)
            nail.pickupType = PickupPermission.DISALLOWED
            nail.chargedNail = true
            nail.chargedDamage = 0.1 * data.usesSoFar
            world.spawnEntity(nail)
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }

    override fun getUseTicks(stack: ItemStack, livingEntity: LivingEntity): Int {
        return 72000
    }

    fun setPropertiesTwo(
        entity: ProjectileEntity, pitch: Float, yaw: Float, roll: Float, speed: Float, modifierXYZ: Float
    ) {
        val f = -MathHelper.sin(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180))
        val g = -MathHelper.sin((pitch + roll) * (Math.PI.toFloat() / 180))
        val h = MathHelper.cos(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180))
        entity.setVelocity(f.toDouble(), g.toDouble(), h.toDouble(), speed, modifierXYZ)
    }

}