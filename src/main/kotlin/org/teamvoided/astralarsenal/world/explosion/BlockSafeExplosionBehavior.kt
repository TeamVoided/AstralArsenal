package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior

abstract class BlockSafeExplosionBehavior : ExplosionBehavior() {
    override fun canDestroyBlock(e: Explosion, w: BlockView, p: BlockPos, s: BlockState, pow: Float): Boolean = false
}