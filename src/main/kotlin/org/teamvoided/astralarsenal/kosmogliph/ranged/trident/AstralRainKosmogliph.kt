package org.teamvoided.astralarsenal.kosmogliph.ranged.trident

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.item.NailCannonItem.Companion.BAR_LIMIT
import org.teamvoided.astralarsenal.item.NailCannonItem.Companion.funnyMath
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.util.getKosmogliphsOnStack
import java.awt.Color
import java.lang.Math.clamp
import java.util.*
import kotlin.math.round

class AstralRainKosmogliph(id: Identifier) :
    SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_ASTRAL_RAIN) }) {
    class Data(
        val charges: Int,
    ) {
        companion object {
            val CODEC: Codec<Data> = RecordCodecBuilder.create { builder ->
                val group = builder.group(
                    Codec.INT.fieldOf("ticks").forGetter { it.charges }
                )
                group.apply(builder, AstralRainKosmogliph::Data)
            }
        }
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val charges = Objects.requireNonNull(stack.get(AstralItemComponents.ASTRAL_RAIN_DATA))?.charges ?: 0
        if(entity is PlayerEntity && entity.isTouchingWaterOrRain && charges < 3){
            stack.set(AstralItemComponents.ASTRAL_RAIN_DATA, Data(3))
        }
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.RIPTIDE)
    }

}