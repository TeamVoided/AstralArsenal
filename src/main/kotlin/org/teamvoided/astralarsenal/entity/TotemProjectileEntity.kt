package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralEntities
import java.util.*

class TotemProjectileEntity : ArrowEntity {
    constructor(entityType: EntityType<out TotemProjectileEntity?>?, world: World?) :
            super(entityType as EntityType<out ArrowEntity?>?, world)

    constructor(world: World, owner: LivingEntity) :
            super(AstralEntities.BEAM_OF_LIGHT_ARROW as EntityType<out ArrowEntity?>, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.BEAM_OF_LIGHT_ARROW as EntityType<out ArrowEntity?>, world)

    var entity: Int = 0

    //1 for strikers, 2 for snipers, 3 for idols.
    var target: UUID? = null
//    var owner : Entity? = null

    override fun onEntityHit(entityHitResult: EntityHitResult?) {

    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        when (entity) {
            1 -> {}
            2 -> {}
            3 -> {}
            else -> {}
        }
        this.discard()
        super.onBlockHit(blockHitResult)
    }
}