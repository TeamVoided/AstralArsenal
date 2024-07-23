package org.teamvoided.astralarsenal.item.kosmogliph.armor

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class GrappleKosmogliph (id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.HELMET
}) {

    fun handleJump(stack: ItemStack, player: PlayerEntity) {
        val data = stack.get(AstralItemComponents.GRAPPLE_DATA) ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        val world = player.world
        var jumps = data.jumps
        var timer = data.timer
        if(jumps > 0) {
            println("inside")
            timer += 20
            player.setVelocity(player.movement.x, 0.6, player.movement.z)
            player.velocityModified = true
            jumps--
            stack.set(AstralItemComponents.GRAPPLE_DATA, Data(jumps, timer, true))
    }}
// i don't know why this doesn't work, someone pls fix, thank you :3
    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (slot == 3) {
            val data = stack.get(AstralItemComponents.GRAPPLE_DATA)
                ?: throw IllegalStateException("Erm, how the fuck did you manage this")
            var jumps = data.jumps
            var timer = data.timer
            var negateFallDamage = data.negateFallDamage
            if (entity.isOnGround) {
                jumps = 2

                if (negateFallDamage) negateFallDamage = false
            }
            if(jumps > 0 && !entity.isOnGround && entity.horizontalCollision && entity.velocity.y < -0.1 && timer < 1) {
                entity.setVelocity(0.0, (entity.movement.y - 0.006).coerceAtLeast(-0.1), 0.0)
                entity.velocityModified = true
                entity.resetFallDistance()
            }
            if (jumps < 1){
                world.playSound(
                    null,
                    entity.x,
                    entity.y,
                    entity.z,
                    AstralSounds.BEAM_VIBRATE,
                    SoundCategory.PLAYERS,
                    0.5F,
                    0.1f
                )
            }
            if(timer > 0) timer--
            stack.set(AstralItemComponents.GRAPPLE_DATA, Data(jumps, timer, negateFallDamage))
        }
    }

    override fun shouldNegateDamage(
        stack: ItemStack,
        entity: LivingEntity,
        source: DamageSource,
        equipmentSlot: EquipmentSlot
    ): Boolean {
        val data = stack.get(AstralItemComponents.GRAPPLE_DATA) ?: return false

        if (data.negateFallDamage) {
            stack.set(AstralItemComponents.GRAPPLE_DATA, Data(data.jumps, data.timer, false))
            return true
        }

        return false
    }

    data class Data(
        val jumps: Int,
        val timer: Int,
        val negateFallDamage: Boolean
    ) {
        companion object {
            val CODEC: Codec<Data> = RecordCodecBuilder.create { builder ->
                val group = builder.group(
                    Codec.INT.fieldOf("jumps").orElse(0).forGetter(Data::jumps),
                    Codec.INT.fieldOf("timer").orElse(0).forGetter(Data::timer),
                    Codec.BOOL.fieldOf("negateFallDamage").orElse(false).forGetter(Data::negateFallDamage)
                )

                group.apply(builder, ::Data)
            }
        }
    }
}