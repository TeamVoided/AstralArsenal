package org.teamvoided.astralarsenal.item.kosmogliph.tools

import net.minecraft.block.BlockState
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.PickaxeItem
import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.logic.breakAndDropStacksAt
import org.teamvoided.astralarsenal.item.kosmogliph.logic.canSafelyBreak

class HammerKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.item is PickaxeItem }) {
    override fun postMine(stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miner: LivingEntity) {
        if (world.isClient() || world !is ServerWorld) return

        val mineablePositions = queryMinablePositions(stack, world, pos, miner)
        mineablePositions.breakAndDropStacksAt(world, pos, miner)

        stack.damageEquipment(mineablePositions.size, miner, EquipmentSlot.MAINHAND)
    }

    fun queryMinablePositions(stack: ItemStack, world: World, pos: BlockPos, miner: LivingEntity): Set<BlockPos> {
        val minablePositions = mutableSetOf(pos)
        val reach = if (miner.isInCreativeMode) 5.0 else 4.5

        val combined = miner.eyePos.add(miner.rotationVector.multiply(reach))

        //raycast always return a BlockHitResult
        val raycast = world.raycast(RaycastContext(miner.eyePos, combined, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, miner))
        val aoe = areaOfAffect(pos, raycast.side)

        aoe.allInside().forEach {
            if (stack.canSafelyBreak(world, world.getBlockState(it), it))
                minablePositions.add(it)
        }

        val durability = stack.maxDamage - stack.damage
        if (durability < minablePositions.size) {
            return minablePositions.toList().subList(0, durability - 1).toSet()
        }

        return minablePositions
    }

    fun BlockBox.allInside(): Set<BlockPos> {
        val positions = mutableSetOf<BlockPos>()

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    positions.add(BlockPos(x, y, z))
                }
            }
        }

        return positions
    }

    fun areaOfAffect(pos: BlockPos, direction: Direction): BlockBox {
        return when (direction) {
            Direction.DOWN, Direction.UP -> BlockBox(pos.x - 1, pos.y, pos.z - 1, pos.x + 1, pos.y, pos.z + 1)
            Direction.NORTH, Direction.SOUTH -> BlockBox(pos.x - 1, pos.y - 1, pos.z, pos.x + 1, pos.y + 1, pos.z)
            Direction.EAST, Direction.WEST -> BlockBox(pos.x, pos.y - 1, pos.z - 1, pos.x, pos.y + 1, pos.z + 1)
        }
    }
    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}