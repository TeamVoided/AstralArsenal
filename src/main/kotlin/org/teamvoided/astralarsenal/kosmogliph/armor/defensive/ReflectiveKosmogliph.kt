package org.teamvoided.astralarsenal.kosmogliph.armor.defensive

import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralEntityTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.BeamOfLightArrowEntity
import org.teamvoided.astralarsenal.entity.nails.NailEntity
import org.teamvoided.astralarsenal.init.AstralEffects.BREACHED
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.mixin.PersistentProjectileEntityAccessor
import org.teamvoided.astralarsenal.util.randomBool
import kotlin.math.min

class ReflectiveKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_REFLECTIVE) }) {
    var entitiesHit = mutableListOf<Entity>()
    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot,
        stage: DamageModificationStage
    ): Float {
        if (stage != DamageModificationStage.POST_ARMOR) return super.modifyDamage(
            stack,
            entity,
            damage,
            source,
            equipmentSlot,
            stage
        )

        var outputDamage = damage
        if (source.isTypeIn(AstralDamageTypeTags.IS_PROJECTILE)) {
            val effects = entity.statusEffects.filter { breached.contains(it.effectType) }
            var multiplyer = 0.5
            for (effect in effects) {
                multiplyer = min(0.5 + (0.125 * (effect.amplifier + 1)), 1.0)
            }
            outputDamage = (outputDamage * multiplyer).toFloat()
        }
        return outputDamage
    }

    val breached = listOf(
        BREACHED
    )

    override fun inventoryTick(stack: ItemStack, world: World, barer: Entity, slot: Int, selected: Boolean) {
        if (slot == 2) {
            val entities = world.getOtherEntities(
                null, Box(
                    barer.pos.x + 3,
                    barer.pos.y + 3,
                    barer.pos.z + 3,
                    barer.pos.x - 3,
                    barer.pos.y - 1,
                    barer.pos.z - 3
                )
            ).filter { (it !is PersistentProjectileEntity || !(it as PersistentProjectileEntityAccessor).inGround) && it.type.isIn(
                AstralEntityTags.REFLECTABLE_PROJECTILES) }
            for (entity in entities) {
                if (entity is ProjectileEntity && !entitiesHit.contains(entity)) {
                    if (entity.owner != barer && entity !is BeamOfLightArrowEntity && barer is LivingEntity) {
                        val effects = barer.statusEffects.filter { breached.contains(it.effectType) }
                        var chance = 700
                        for (effect in effects) {
                            chance = chance - (175 * (effect.amplifier + 1))
                        }
                        if (randomBool(1000, chance, world)) {
                            entity.velocity = entity.velocity.multiply(-1.0, -1.0, -1.0)
                            entity.velocityModified = true
                            entity.owner = barer
                            if (entity is NailEntity){
                                entity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED
                            }
                            world.playSound(
                                null,
                                barer.x,
                                barer.y,
                                barer.z,
                                SoundEvents.ENTITY_BREEZE_DEFLECT,
                                SoundCategory.PLAYERS,
                                1.0F,
                                1.0F
                            )
                        } else {
                            entitiesHit.add(entity)
                        }
                    }
                }
            }
        }
        super.inventoryTick(stack, world, barer, slot, selected)
    }
}