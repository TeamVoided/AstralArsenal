package org.teamvoided.astralarsenal.item.kosmogliph.armor.defensive

import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ElytraItem
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class ReflectiveKosmogliph (id: Identifier) : SimpleKosmogliph(id, {
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
        if (source.isTypeIn(AstralDamageTypeTags.IS_PROJECTILE)){
            outputDamage = (outputDamage * 0.5).toFloat()
        }
        return super.modifyDamage(stack, entity, outputDamage, source, equipmentSlot)
    }

    override fun inventoryTick(stack: ItemStack, world: World, barer: Entity, slot: Int, selected: Boolean) {
        if (slot == 2){
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
            for(entity in entities){
                if(entity is ProjectileEntity && !entitiesHit.contains(entity)){
                    if(entity.owner != barer){
                    val random = world.random.rangeInclusive(1,10)
                    if(random != 1 && random != 2 && random != 3){
                    entity.velocity = entity.velocity.multiply(-1.5,-1.5,-1.5)
                    entity.velocityModified = true
                        barer.playSound(SoundEvents.ENTITY_BREEZE_DEFLECT, 1.0f, 1.0f)
                    }
                    entitiesHit.add(entity)
                }
                }
            }
        }
        super.inventoryTick(stack, world, barer, slot, selected)
    }
}