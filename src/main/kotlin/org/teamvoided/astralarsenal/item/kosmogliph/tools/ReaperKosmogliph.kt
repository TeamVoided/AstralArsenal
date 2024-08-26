package org.teamvoided.astralarsenal.item.kosmogliph.tools

import net.minecraft.block.BlockState
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.item.TillingActions
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.logic.*

class ReaperKosmogliph(id: Identifier) : SimpleKosmogliph(id, AstralItemTags.SUPPORTS_REAPER) {
    override fun postMine(stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miner: LivingEntity) {
        if (world.isClient() || world !is ServerWorld) return
        if (miner.isSneaking) return

        val mineablePositions = queryReaperMineablePositions(stack, world, pos, state)
        if (mineablePositions.isEmpty()) return
        mineablePositions.breakAndDropStacksAt(world, pos, miner, stack)

        stack.damageEquipment(mineablePositions.size - 1, miner, EquipmentSlot.MAINHAND)
    }


    override fun onUseOnBlock(ctx: ItemUsageContext) {
        val pos = ctx.blockPos
        val world = ctx.world
        val playerEntity = ctx.player
        if (playerEntity?.isSneaking == true) return
        for (blockPos in areaOfAffect(pos, ctx.side).allInside().filter { it != pos }) {
            if (!blockPos.isInWorld(world)) continue
            val predicateConsumer = TillingActions.get[world.getBlockState(blockPos).block] ?: continue
            val customContext =
                ItemUsageContext(ctx.player, ctx.hand, BlockHitResult(ctx.hitPos, ctx.side, blockPos, false))
            if (predicateConsumer.first.test(customContext)) {
                world.playSound(playerEntity, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0f, 1.0f)
                if (!world.isClient) {
                    predicateConsumer.second.accept(customContext)
                    playerEntity?.let {
                        it.swingHand(ctx.hand, true)
                        ctx.stack.damageEquipment(1, it, LivingEntity.getHand(ctx.hand))
                    }
                }
            }
        }
    }
}
