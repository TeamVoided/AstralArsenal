package org.teamvoided.astralarsenal.kosmogliph.armor

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import kotlin.math.roundToInt

class SlamKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_SLAM) }) {
    fun handleSlam(stack: ItemStack, player: PlayerEntity) {
        val data = stack.get(AstralItemComponents.SLAM_DATA) ?: return
        if (!player.isOnGround && !data.slamming) {
            stack.set(AstralItemComponents.SLAM_DATA, Data(data.lastFallDistance, true))
            player.setVelocity(0.0, -20.0, 0.0)
            player.velocityModified = true
        }
    }

    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot,
        stage: DamageModificationStage
    ): Float {
        if (stage != DamageModificationStage.POST_EFFECT) return super.modifyDamage(
            stack,
            entity,
            damage,
            source,
            equipmentSlot,
            stage
        )

        val data = stack.get(AstralItemComponents.SLAM_DATA) ?: return damage
        return if (data.slamming && source.isTypeIn(DamageTypeTags.IS_FALL)) damage / 2 else damage
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (entity !is LivingEntity) return
        val data = stack.get(AstralItemComponents.SLAM_DATA) ?: return
        val currentFallDistance = entity.fallDistance
        var slamming = data.slamming

        if (slamming && currentFallDistance <= 0f && data.lastFallDistance > 0f) {
            entity.playSound(SoundEvents.ITEM_MACE_SMASH_GROUND)
            entity.addStatusEffect(
                StatusEffectInstance(
                    AstralEffects.SLAM_JUMP,
                    10,
                    (data.lastFallDistance + 2).roundToInt(),
                    false,
                    false,
                    true
                )
            )

            slamming = false
        }

        if (entity.velocity.y >= 0) {
            slamming = false
        }

        stack.set(AstralItemComponents.SLAM_DATA, Data(currentFallDistance, slamming))
    }

    class Data(
        val lastFallDistance: Float,
        val slamming: Boolean
    ) {
        companion object {
            val CODEC: Codec<Data> = RecordCodecBuilder.create { builder ->
                val group = builder.group(
                    Codec.FLOAT.fieldOf("lastFallDistance").forGetter { it.lastFallDistance },
                    Codec.BOOL.fieldOf("slamming").forGetter { it.slamming }
                )

                group.apply(builder, SlamKosmogliph::Data)
            }
        }
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}