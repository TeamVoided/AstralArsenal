package org.teamvoided.astralarsenal.item.kosmogliph

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.block.BlockState
import net.minecraft.client.item.TooltipConfig
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.loot.LootTable
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.item.components.KosmogliphsComponent.Companion.toComponent

interface Kosmogliph {
    fun modifyItemTooltip(stack: ItemStack, ctx: Item.TooltipContext, tooltip: MutableList<Text>, config: TooltipConfig)
    fun canBeAppliedTo(stack: ItemStack): Boolean
    fun onApply(stack: ItemStack) {}
    fun onUnapply(stack: ItemStack) {}
    fun onUse(world: World, player: PlayerEntity, hand: Hand) {}
    fun onUseOnBlock(ctx: ItemUsageContext) {}
    fun onUseOnEntity(stack: ItemStack, player: PlayerEntity, entity: LivingEntity, hand: Hand) {}
    fun postMine(stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miner: LivingEntity) {}
    fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {}
    fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {}
    fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {}
    fun shouldNegateDamage(stack: ItemStack, entity: LivingEntity, source: DamageSource, equipmentSlot: EquipmentSlot): Boolean = false
    fun modifyDamage(stack: ItemStack, entity: LivingEntity, damage: Float, source: DamageSource, equipmentSlot: EquipmentSlot): Float = damage
    fun modifyBlockBreakLoot(
        table: LootTable,
        parameters: LootContextParameterSet,
        world: ServerWorld,
        stack: ItemStack,
        original: ObjectArrayList<ItemStack>
    ): List<ItemStack> = original

    fun id() = REGISTRY.getId(this)!!
    fun translationKey(tooltip: Boolean = false) =
        id().toTranslationKey("kosmogliph${if (tooltip) ".tooltip" else ".name"}")

    data class Failure(val reason: Text)

    companion object {
        val REGISTRY_KEY: RegistryKey<Registry<Kosmogliph>> = RegistryKey.ofRegistry(AstralArsenal.id("kosmogliphs"))
        val REGISTRY: Registry<Kosmogliph> = FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister()

        val CODEC = REGISTRY.codec
        val PACKET_CODEC = PacketCodecs.fromCodec(REGISTRY.codec)

        fun addToComponent(stack: ItemStack, kosmogliph: Kosmogliph): Either<Failure, ItemStack> {
            val kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS)
                ?: return Failure(Text.translatable("kosmogliph.error.missing_component")).left()
            val mutableClone = kosmogliphs.toMutableSet()
            mutableClone.add(kosmogliph)
            stack.set(AstralItemComponents.KOSMOGLIPHS, mutableClone.toComponent())
            kosmogliph.onApply(stack)

            return stack.right()
        }

        fun removeFromComponent(stack: ItemStack, kosmogliph: Kosmogliph): Either<Failure, ItemStack> {
            val kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS)
                ?: return Failure(Text.translatable("kosmogliph.error.missing_component")).left()
            val mutableClone = kosmogliphs.toMutableSet()
            mutableClone.remove(kosmogliph)
            stack.set(AstralItemComponents.KOSMOGLIPHS, mutableClone.toComponent())
            kosmogliph.onUnapply(stack)

            return stack.right()
        }
    }
}
