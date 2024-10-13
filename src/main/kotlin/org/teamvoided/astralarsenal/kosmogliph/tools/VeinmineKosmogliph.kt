package org.teamvoided.astralarsenal.kosmogliph.tools

import net.minecraft.block.BlockState
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.logic.breakAndDropStacksAt
import org.teamvoided.astralarsenal.kosmogliph.logic.queryMineableVeinPositions
import kotlin.math.min

class VeinmineKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_VEIN_MINER) }) {
    override fun postMine(stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miner: LivingEntity) {
        if (world.isClient() || world !is ServerWorld) return
        if (miner.isSneaking) return
        val mineablePositions =
            queryMineableVeinPositions(
                stack,
                world,
                state,
                pos,
                30.0,
                min(64, stack.maxDamage - (stack.damage - 1))
            )
        if (mineablePositions.isEmpty()) return
        mineablePositions.breakAndDropStacksAt(world, pos, miner, stack)

        stack.damageEquipment(mineablePositions.size - 1, miner, EquipmentSlot.MAINHAND)
    }
}
