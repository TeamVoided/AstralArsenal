package org.teamvoided.astralarsenal.kosmogliph.armor.defensive

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.ElderGuardianEntity
import net.minecraft.entity.mob.GuardianEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.BeamOfLightEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.melee.mace.PulveriserKosmogliph
import java.lang.IllegalStateException
import kotlin.math.roundToInt
import kotlin.math.max
import kotlin.math.min

class CapacitanceKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_CAPACITANCE) }) {
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
        val data = stack.get(AstralItemComponents.CAPACITANCE_DATA_V1)
            ?: throw IllegalStateException("how the fuck?")
        var dmg = data.damage
        var outputDamage = damage
        if (source.isTypeIn(AstralDamageTypeTags.IS_PLASMA) || source.attacker is GuardianEntity || source.attacker is ElderGuardianEntity) {
            outputDamage = (outputDamage * 0.2).toFloat()
            dmg += (damage * 1.0).toFloat()
        } else {
            if (dmg >= 0.5 && source.attacker is LivingEntity && entity.world is ServerWorld && source.attacker != entity) {
                val attacker = source.attacker as LivingEntity
                val world = entity.world as ServerWorld
                val damageToDeal = if(attacker is PlayerEntity) min(15f, dmg/5) else dmg/5
                attacker.customDamage(AstralDamageTypes.NON_RAILED, damageToDeal, entity, entity)
                sillyLightningTime(entity.pos, attacker.pos, world)
                dmg -= damageToDeal
            }
        }
        stack.set(AstralItemComponents.CAPACITANCE_DATA_V1, Data(dmg))
        return outputDamage
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if(entity is LivingEntity && entity.getEquippedStack(EquipmentSlot.CHEST) == stack){
            val data = stack.get(AstralItemComponents.CAPACITANCE_DATA_V1)
                ?: throw IllegalStateException("how the fuck?")
            if(data.damage >= 0.5){
                val height = entity.height
                val width = entity.width
                if (world is ServerWorld) {
                    world.spawnParticles(
                        ParticleTypes.ELECTRIC_SPARK,
                        entity.x,
                        entity.y + (height/2),
                        entity.z,
                        max(1.0, (data.damage * 0.2)).roundToInt(),
                        width/2.5,
                        height/2.5,
                        width/2.5,
                        0.0
                    )
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    class Data(
        val damage: Float,
    ) {
        companion object {
            val CODEC: Codec<Data> = RecordCodecBuilder.create { builder ->
                val group = builder.group(
                    Codec.FLOAT.fieldOf("ticks").forGetter { it.damage },
                )
                group.apply(builder, CapacitanceKosmogliph::Data)
            }
        }
    }

    fun sillyLightningTime(pos1: Vec3d, pos2: Vec3d, world: ServerWorld) {
        val bends = world.random.rangeInclusive(3, 5)
        val bendPos = mutableListOf<Vec3d>()
        bendPos.add(pos1)
        for (i in 0..<bends) {
            val distance = pos1.distanceTo(pos2)
            val maxlerp: Double = 1.0 / bends
            val xrand = (pos1.x - pos2.x) / (bends / 2)
            val yrand = (pos1.y - pos2.y) / (bends / 2)
            val zrand = (pos1.z - pos2.z) / (bends / 2)
            val xmin = ((pos1.x - pos2.x) / (bends)) * i
            val ymin = ((pos1.y - pos2.y) / (bends)) * i
            val zmin = ((pos1.z - pos2.z) / (bends)) * i
            bendPos.add(
                Vec3d(
                    lerp(pos1.x, pos2.x, maxlerp) + xmin + world.random.nextDouble()
                        .minus(0.5).times(xrand),
                    lerp(pos1.y, pos2.y, maxlerp) + ymin + world.random.nextDouble()
                        .minus(0.5).times(yrand),
                    lerp(pos1.z, pos2.z, maxlerp) + zmin + world.random.nextDouble()
                        .minus(0.5).times(zrand)
                )
            )
        }
        bendPos.add(pos2)
        var count = 0
        for (i in 0..<(bendPos.size - 1)) {
            if (count > bendPos.size) {
                break
            }
            count++
            val a = bendPos[i]
            val b = bendPos[i + 1]
            val distance = a.distanceTo(b)
            val interval = (distance * 10)
            for (j in 0..interval.roundToInt()) {
                world.spawnParticles(
                    ParticleTypes.END_ROD,
                    (lerp(a.x, b.x, j / interval)),
                    (lerp(a.y + 1, b.y + 1, j / interval)),
                    (lerp(a.z, b.z, j / interval)),
                    1,
                    0.01,
                    0.01,
                    0.01,
                    0.0
                )
            }
        }
    }
}