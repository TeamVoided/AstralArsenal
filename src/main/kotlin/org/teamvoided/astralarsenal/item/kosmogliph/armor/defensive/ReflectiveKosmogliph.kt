package org.teamvoided.astralarsenal.item.kosmogliph.armor.defensive

import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ElytraItem
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.entity.BeamOfLightArrowEntity
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class ReflectiveKosmogliph(id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    (item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.CHESTPLATE) || item is ElytraItem
}) {
    var entitiesHit = mutableListOf<Entity>()
    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot
    ): Float {
        var outputDamage = damage
        if (source.isTypeIn(AstralDamageTypeTags.IS_PROJECTILE)) {
            outputDamage = (outputDamage * 0.5).toFloat()
        }
        return super.modifyDamage(stack, entity, outputDamage, source, equipmentSlot)
    }

    override fun inventoryTick(stack: ItemStack, world: World, barer: Entity, slot: Int, selected: Boolean) {
        if (slot == 2) {
            val entities = world.getOtherEntities(
                null, Box(
                    barer.pos.x + 2,
                    barer.pos.y + 2,
                    barer.pos.z + 2,
                    barer.pos.x - 2,
                    barer.pos.y - 2,
                    barer.pos.z - 2
                )
            )
            for (entity in entities) {
                if (entity is ProjectileEntity && !entitiesHit.contains(entity)) {
                    if (entity.owner != barer && entity !is BeamOfLightArrowEntity) {
                        val random = world.random.rangeInclusive(1, 10)
                        if (random != 1 && random != 2 && random != 3) {
                            entity.velocity = entity.velocity.multiply(-1.0, -1.0, -1.0)
                            entity.velocityModified = true
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
                        }
                        entitiesHit.add(entity)
                    }
                }
            }
        }
        super.inventoryTick(stack, world, barer, slot, selected)
    }
}