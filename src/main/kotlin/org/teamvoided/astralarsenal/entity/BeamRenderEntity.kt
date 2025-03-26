package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World
import org.joml.Vector3f
import org.teamvoided.astralarsenal.init.AstralEntities

class BeamRenderEntity : Entity {

    constructor(entityType: EntityType<out BeamRenderEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.BEAM_RENDERER as EntityType<out Entity?>, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.BEAM_RENDERER as EntityType<out Entity?>, world)

    override fun tick() {
        if (this.dataTracker.get(LiveTime) > this.dataTracker.get(ShrinkTime)) {
            this.dataTracker.set(LiveTime, this.dataTracker.get(LiveTime) - 1)
            this.dataTracker.set(OuterThickness, this.dataTracker.get(MaxOuterThickness))
        } else if (this.dataTracker.get(LiveTime) > 0) {
            this.dataTracker.set(LiveTime, this.dataTracker.get(LiveTime) - 1)
            this.dataTracker.set(
                OuterThickness, getThickness(
                    this.dataTracker.get(ShrinkTime), this.dataTracker.get(
                        LiveTime
                    ), this.dataTracker.get(MaxOuterThickness)
                )
            )
        } else if (this.dataTracker.get(LiveTime) <= 0) {
            this.discard()
        }
        super.tick()
    }

    fun getThickness(ShrinkTime: Int, currentTime: Int, MaxSize: Float): Float {
        if (currentTime > ShrinkTime) {
            return MaxSize
        } else {
            return ((currentTime.toFloat() / ShrinkTime.toFloat()) * MaxSize)
        }
    }

    override fun shouldRender(distance: Double): Boolean {
        return true
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(InterColour, 0xffffffff.toInt())
        builder.add(OuterColour, 0xffffffff.toInt())
        builder.add(TargetPos, Vector3f(0f, 0f, 0f))
        builder.add(MaxOuterThickness, 0f)
        builder.add(OuterThickness, 0f)
        builder.add(ShrinkTime, 0)
        builder.add(LiveTime, 0)
        builder.add(InnerCubes, 0)
        builder.add(Opacity, 0.3f)
        builder.add(OriginPos, Vector3f(0f, 0f, 0f))
    }

    companion object {
        val InterColour: TrackedData<Int> =
            DataTracker.registerData(BeamRenderEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val OuterColour: TrackedData<Int> =
            DataTracker.registerData(BeamRenderEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val TargetPos: TrackedData<Vector3f> =
            DataTracker.registerData(BeamRenderEntity::class.java, TrackedDataHandlerRegistry.VECTOR3F)
        val MaxOuterThickness: TrackedData<Float> =
            DataTracker.registerData(BeamRenderEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val OuterThickness: TrackedData<Float> =
            DataTracker.registerData(BeamRenderEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val ShrinkTime: TrackedData<Int> =
            DataTracker.registerData(BeamRenderEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val LiveTime: TrackedData<Int> =
            DataTracker.registerData(BeamRenderEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val InnerCubes: TrackedData<Int> =
            DataTracker.registerData(BeamRenderEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val Opacity: TrackedData<Float> =
            DataTracker.registerData(BeamRenderEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val OriginPos: TrackedData<Vector3f> =
            DataTracker.registerData(BeamRenderEntity::class.java, TrackedDataHandlerRegistry.VECTOR3F)
    }


    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }
}