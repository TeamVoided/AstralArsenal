package org.teamvoided.astralarsenal.item

import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlotGroup
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterial
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World
import org.teamvoided.astralarsenal.components.NailCannonDataV1
import org.teamvoided.astralarsenal.components.StarScatterDataV1
import org.teamvoided.astralarsenal.entity.Projectiles.NailEntity
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.item.NailCannonItem.Companion.BOOSTED_SPEED
import org.teamvoided.astralarsenal.item.NailCannonItem.Companion.BOOSTED_SPREAD
import org.teamvoided.astralarsenal.item.NailCannonItem.Companion.SLOW_SPEED
import org.teamvoided.astralarsenal.item.NailCannonItem.Companion.SLOW_SPREAD
import org.teamvoided.astralarsenal.item.NailCannonItem.Companion.USE_TICKS
import org.teamvoided.astralarsenal.kosmogliph.logic.getXFromPitchAndYaw
import org.teamvoided.astralarsenal.kosmogliph.logic.getYFromPitchAndRoll
import org.teamvoided.astralarsenal.kosmogliph.logic.getZFromPitchAndYaw
import org.teamvoided.astralarsenal.kosmogliph.logic.setShootVelocity
import org.teamvoided.astralarsenal.util.getKosmogliphs
import org.teamvoided.astralarsenal.util.hasKosmogliph
import org.teamvoided.astralarsenal.util.hasKosmogliphs
import java.awt.Color
import java.lang.Math.clamp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class StarScatterItem(settings: Settings) : Item(settings) {

    private fun ItemStack.maxUses(): Int {
        return if (this.hasKosmogliph(AstralKosmogliphs.CAPACITY)) 20 else if (this.hasKosmogliph(AstralKosmogliphs.STRONGSHOT)) 1 else if (this.hasKosmogliph(
                AstralKosmogliphs.STATIC_SHOT
            )
        ) 3 else if (this.hasKosmogliph(AstralKosmogliphs.BLOWBACK)) 2 else 10
    }


    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        val data = stack.getOrDefault(AstralDataComponents.STARSCATTER_DATA, StarScatterDataV1.DEFAULT)
        var bullets = data.bullets
        if (attacker is PlayerEntity && stack.cooldown <= 0) {
            if (bullets < stack.maxUses()) bullets++
            attacker.itemCooldownManager.set(stack.item, 10)
        }
        stack.set(
            AstralDataComponents.STARSCATTER_DATA,
            StarScatterDataV1(bullets, data.spreadStage, data.spreadStageCooldown)
        )
        return super.postHit(stack, target, attacker)
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val data = stack.getOrDefault(AstralDataComponents.STARSCATTER_DATA, StarScatterDataV1.DEFAULT)
        var spreadStage = data.spreadStage
        var spreadStageCooldown = data.spreadStageCooldown
        val bullets = data.bullets
        if (0 < spreadStageCooldown) spreadStageCooldown--
        else if (spreadStage > 0) {spreadStage--; spreadStageCooldown = 30}
        stack.set(
            AstralDataComponents.STARSCATTER_DATA,
            StarScatterDataV1(bullets, spreadStage, spreadStageCooldown)
        )
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack?>? {
        val stack = user.getStackInHand(hand)
        val data = stack.getOrDefault(AstralDataComponents.STARSCATTER_DATA, StarScatterDataV1.DEFAULT)
        var bullets = data.bullets
        var spreadStage = data.spreadStage
        if (bullets > 0) {
            repeat(10) {
                val nail = NailEntity(world, user)
                val spread = (min(data.spreadStage, 2)) * 15F + 15F
                nail.setShootVelocity(user.pitch, user.yaw, 0.0f, 3F, spread)
                val offset = user.eyePos.add(user.rotationVector.normalize().multiply(0.6))
                nail.setPosition(offset.x, offset.y, offset.z)
                nail.pickupType = PickupPermission.DISALLOWED
                world.spawnEntity(nail)
            }
            repeat(20){
                world.addParticle(
                    ParticleTypes.GLOW,
                    true,
                    user.x,
                    user.eyeY,
                    user.z,
                    0.0, //getXFromPitchAndYaw(user.pitch, user.yaw).toDouble().times(0.1),
                    0.0, //getYFromPitchAndRoll(user.pitch, 0.0f).toDouble().times(0.1),
                    0.0 //getZFromPitchAndYaw(user.pitch, user.yaw).toDouble().times(0.1)
                )
            }
            bullets--
            if (spreadStage > 5) {user.itemCooldownManager.set(stack.item, 100) }
            else {spreadStage++}
            stack.set(
                AstralDataComponents.STARSCATTER_DATA,
                StarScatterDataV1(bullets, spreadStage, data.spreadStageCooldown)
            )
        }

        return super.use(world, user, hand)
    }

    override fun getItemBarColor(stack: ItemStack): Int {
        return Color.MAGENTA.rgb
    }

    override fun getItemBarStep(stack: ItemStack): Int {
        val data = stack.getOrDefault(AstralDataComponents.STARSCATTER_DATA, StarScatterDataV1.DEFAULT)
        return data?.let { funnyMath(stack.maxUses() - it.bullets, stack.maxUses()) } ?: BAR_LIMIT.toInt()
    }

    override fun isItemBarVisible(stack: ItemStack): Boolean {
        val data = stack.getOrDefault(AstralDataComponents.STARSCATTER_DATA, StarScatterDataV1.DEFAULT)
        return data != null && data.bullets < stack.maxUses()
    }

    override fun getUseAction(stack: ItemStack): UseAction = UseAction.BLOCK

    companion object {
        const val BAR_LIMIT = 10f
        fun funnyMath(x: Int, y: Int) = clamp(round(BAR_LIMIT - x * BAR_LIMIT / y).toLong(), 0, BAR_LIMIT.toInt())

        fun createAttributes(
            baseAttackDamageModifier: Int,
            attackSpeedModifier: Float,
        ): AttributeModifiersComponent {
            return AttributeModifiersComponent.builder().add(
                EntityAttributes.GENERIC_ATTACK_DAMAGE, EntityAttributeModifier(
                    BASE_ATTACK_DAMAGE,
                    (baseAttackDamageModifier.toFloat()).toDouble(),
                    EntityAttributeModifier.Operation.ADD_VALUE
                ), EquipmentSlotGroup.MAINHAND
            ).add(
                EntityAttributes.GENERIC_ATTACK_SPEED, EntityAttributeModifier(
                    BASE_ATTACK_SPEED, attackSpeedModifier.toDouble(), EntityAttributeModifier.Operation.ADD_VALUE
                ), EquipmentSlotGroup.MAINHAND
            ).build()
        }
    }

}