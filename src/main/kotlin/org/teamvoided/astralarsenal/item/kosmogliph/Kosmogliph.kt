package org.teamvoided.astralarsenal.item.kosmogliph

import arrow.core.Either
import arrow.core.right
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.block.BlockState
import net.minecraft.client.item.TooltipConfig
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.loot.LootTable
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.screen.slot.Slot
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.ClickType
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.item.components.KosmogliphsComponent
import org.teamvoided.astralarsenal.item.components.KosmogliphsComponent.Companion.toComponent
import java.util.function.Consumer

interface Kosmogliph {
    fun modifyItemTooltip(
        stack: ItemStack, ctx: Item.TooltipContext, tooltipConsumer: Consumer<Text>, config: TooltipConfig
    ) = Unit

    fun canBeAppliedTo(stack: ItemStack): Boolean
    fun onApply(stack: ItemStack) {}
    fun onUnapply(stack: ItemStack) {}
    fun preUse(world: World, player: PlayerEntity, hand: Hand) {}
    fun onUse(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack>? = null
    fun onUseOnBlock(ctx: ItemUsageContext) {}
    fun onUseOnEntity(stack: ItemStack, player: PlayerEntity, entity: LivingEntity, hand: Hand) {}
    fun postMine(stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miner: LivingEntity) {}
    fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {}
    fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {}
    fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {}
    fun getUseTicks(stack: ItemStack, livingEntity: LivingEntity): Int = 0
    fun shouldNegateDamage(
        stack: ItemStack, entity: LivingEntity, source: DamageSource, equipmentSlot: EquipmentSlot
    ): Boolean = false

    fun modifyDamage(
        stack: ItemStack, entity: LivingEntity, damage: Float, source: DamageSource,
        equipmentSlot: EquipmentSlot, stage: DamageModificationStage
    ): Float = damage

    fun onStackClicked(
        stack: ItemStack, other: ItemStack, slot: Slot, clickType: ClickType,
        player: PlayerEntity, reference: StackReference
    ): Boolean = false

    fun modifyBlockBreakLoot(
        table: LootTable, parameters: LootContextParameterSet, world: ServerWorld,
        stack: ItemStack, original: ObjectArrayList<ItemStack>
    ): List<ItemStack> = original

    fun id() = REGISTRY.getId(this)!!
    fun translationText(tooltip: Boolean = false) = id().path.toString()

    fun translationKey(tooltip: Boolean = false) =
        id().toTranslationKey("kosmogliph${if (tooltip) ".tooltip" else ".name"}")

    fun setPropertiesTwo(
        entity: ProjectileEntity, pitch: Float, yaw: Float, roll: Float, speed: Float, modifierXYZ: Float
    ) {
        val f = -MathHelper.sin(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180))
        val g = -MathHelper.sin((pitch + roll) * (Math.PI.toFloat() / 180))
        val h = MathHelper.cos(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180))
        entity.setVelocity(f.toDouble(), g.toDouble(), h.toDouble(), speed, modifierXYZ)
    }

    fun requiredEnchantments(): List<RegistryKey<Enchantment>> = listOf()
    fun disallowedEnchantment(): List<RegistryKey<Enchantment>> = listOf()
    fun modifyAirStrafeSpeed(entity: LivingEntity, speed: Float) = speed

    data class Failure(val reason: Text)
    companion object {
        val REGISTRY_KEY: RegistryKey<Registry<Kosmogliph>> = RegistryKey.ofRegistry(AstralArsenal.id("kosmogliphs"))
        val REGISTRY: Registry<Kosmogliph> = FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister()

        val CODEC = REGISTRY.codec
        val PACKET_CODEC = PacketCodecs.fromCodec(REGISTRY.codec)

        fun addToComponent(stack: ItemStack, kosmogliph: Kosmogliph): Either<Failure, ItemStack> {
            val kosmogliphs = stack.getOrDefault(AstralItemComponents.KOSMOGLIPHS, KosmogliphsComponent())
            val mutableClone = kosmogliphs.toMutableSet()
            mutableClone.add(kosmogliph)
            stack.set(AstralItemComponents.KOSMOGLIPHS, mutableClone.toComponent())
            kosmogliph.onApply(stack)

            return stack.right()
        }

        fun removeFromComponent(stack: ItemStack, kosmogliph: Kosmogliph): Either<Failure, ItemStack> {
            val kosmogliphs = stack.getOrDefault(AstralItemComponents.KOSMOGLIPHS, KosmogliphsComponent())
            val mutableClone = kosmogliphs.toMutableSet()
            mutableClone.remove(kosmogliph)
            stack.set(AstralItemComponents.KOSMOGLIPHS, mutableClone.toComponent())
            kosmogliph.onUnapply(stack)

            return stack.right()
        }
    }
}
