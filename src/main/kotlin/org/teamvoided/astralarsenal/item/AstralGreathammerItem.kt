package org.teamvoided.astralarsenal.item

import net.minecraft.block.BlockState
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.EquipmentSlotGroup
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.item.ToolMaterial
import net.minecraft.item.ToolMaterials
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class AstralGreathammerItem(settings: Settings) : ToolItem(ToolMaterials.NETHERITE, settings) {
    override fun isEnchantable(stack: ItemStack?): Boolean = true
    override fun postHit(stack: ItemStack?, target: LivingEntity?, attacker: LivingEntity): Boolean {
        val world = attacker.world
        if (world is ServerWorld) {
            world.playSound(
                null,
                attacker.x,
                attacker.y,
                attacker.z,
                SoundEvents.ITEM_MACE_SMASH_AIR,
                attacker.soundCategory,
                1.0f,
                1.5f
            )
        }
        super.postHit(stack, target, attacker)
        return true
    }

    override fun postDamageEntity(stack: ItemStack, target: LivingEntity?, attacker: LivingEntity?) {
        stack.damageEquipment(1, attacker, EquipmentSlot.MAINHAND)
    }

    override fun canMine(state: BlockState?, world: World?, pos: BlockPos?, miner: PlayerEntity): Boolean {
        return !miner.isCreative
    }

    companion object {
        fun createAttributes(
            material: ToolMaterial,
            baseAttackDamageModifier: Int,
            attackSpeedModifier: Float,
        ): AttributeModifiersComponent {
            return AttributeModifiersComponent.builder().add(
                EntityAttributes.GENERIC_ATTACK_DAMAGE, EntityAttributeModifier(
                    BASE_ATTACK_DAMAGE,
                    (baseAttackDamageModifier.toFloat() + material.attackDamage).toDouble(),
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