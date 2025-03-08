package org.teamvoided.astralarsenal.kosmogliph.armor

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.components.SlamData
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.kosmogliph.KosmogliphWithData

class SlamKosmogliph(id: Identifier) :
    KosmogliphWithData(id, AstralDataComponents.SLAM_DATA, AstralItemTags.SUPPORTS_SLAM) {
    fun handleSlam(stack: ItemStack, player: PlayerEntity) {
        val data = stack.getOrDefault(AstralDataComponents.SLAM_DATA, SlamData.DEFAULT)
        if (!player.isOnGround && !data.slamming && !player.isSwimming) {
            stack.set(AstralDataComponents.SLAM_DATA, SlamData(0.0f, true))
            player.setVelocity(0.0, -5.0, 0.0)
            player.velocityModified = true
        }
    }

//    override fun modifyDamage(
//        stack: ItemStack,
//        entity: LivingEntity,
//        damage: Float,
//        source: DamageSource,
//        equipmentSlot: EquipmentSlot,
//        stage: DamageModificationStage
//    ): Float {
//        if (stage != DamageModificationStage.POST_EFFECT) return super.modifyDamage(
//            stack,
//            entity,
//            damage,
//            source,
//            equipmentSlot,
//            stage
//        )

//        val data = stack.get(AstralItemComponents.SLAM_DATA) ?: return damage
//        return if (data.slamming && source.isTypeIn(DamageTypeTags.IS_FALL)) damage / 2 else damage
//    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (entity !is PlayerEntity) return
        val data = stack.getOrDefault(AstralDataComponents.SLAM_DATA, SlamData.DEFAULT)
//        val currentFallDistance = entity.fallDistance
        val slamming = data.slamming
        if (slamming) {
//            if (entity.velocity.y >= 0 && !world.isClient) {
//                stack.set(SLAM_DATA, Data(0.0f, false))
//            } else if(entity.velocity.y < 0f && world.isClient) {
//                entity.setVelocity(0.0, -5.0, 0.0)
//                entity.velocityModified = true
//            }
            if (world.isClient) {
                entity.setVelocity(0.0, -5.0, 0.0)
            }
        }
//        if (slamming && currentFallDistance <= 0f && data.lastFallDistance > 0f) {
////            entity.playSound(SoundEvents.ITEM_MACE_SMASH_GROUND)v
////            entity.addStatusEffect(
////                StatusEffectInstance(
////                    AstralEffects.SLAM_JUMP,
////                    10,
////                    (data.lastFallDistance + 2).roundToInt(),
////                    false,
////                    false,
////                    true
////                )
////            )
//
//            slamming = false
//        }

//        if (entity.velocity.y >= 0) {
//            slamming = false
//        }

        //stack.set(AstralItemComponents.SLAM_DATA, Data(currentFallDistance, slamming))
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}