package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralEntities

class BoomShooterEntity : Entity {

    constructor(entityType: EntityType<out BoomShooterEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.BOOM_SHOOTER_ENTITY as EntityType<out Entity?>, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.BOOM_SHOOTER_ENTITY as EntityType<out Entity?>, world)

    companion object {
        private val time: TrackedData<Int>? =
            DataTracker.registerData(BoomShooterEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }
    fun setTime(Time: Int) {
        dataTracker.set(time, Time)
    }

    fun getTime(): Int {
        return dataTracker.get(time) as Int
    }
    init{
        this.setNoGravity(true)
    }

    override fun tick() {
        var t = this.getTime()
        t++
        if(t.mod(5) == 0){
            if (!world.isClient) {
                val snowballEntity = BoomEntity(world, this.x,this.y,this.z)
                snowballEntity.setProperties(this, this.pitch, (this.yaw.plus(t.times(2))), 0.0f, 0.3f, 0.0f)
                snowballEntity.addVelocity(0.0, 1.0, 0.0)
                world.spawnEntity(snowballEntity)
            }
        }
        if(t >= 180){this.discard()}
        this.setTime(t)
        super.tick()
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
       // TODO("Not yet implemented")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
      //  TODO("Not yet implemented")
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(time, 0)
    }
}