package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralEntities

class SlashEntity : ThrownItemEntity {

    constructor(entityType: EntityType<out SlashEntity?>?, world: World?) :
            super(entityType as EntityType<out ThrownItemEntity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.SLASH_ENTITY as EntityType<out ThrownItemEntity?>, owner, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.SLASH_ENTITY as EntityType<out ThrownItemEntity?>, x, y, z, world)

    override fun getDefaultItem(): Item {
        return Items.AIR
    }

    companion object {
        private val DMG: TrackedData<Int>? =
            DataTracker.registerData(CannonballEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        private val time: TrackedData<Int>? =
            DataTracker.registerData(CannonballEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(DMG, 5)
        builder.add(time,0)
    }

    init{this.setNoGravity(true)}

    override fun tick() {

        super.tick()
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        this.discard()
        super.onBlockHit(blockHitResult)
    }

    fun setDmg(dmg: Int) {
        dataTracker.set(DMG, dmg)
    }

    fun getDmg(): Int {
        return dataTracker.get(DMG) as Int
    }
}