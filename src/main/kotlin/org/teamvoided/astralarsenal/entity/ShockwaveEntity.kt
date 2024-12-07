package org.teamvoided.astralarsenal.entity

import com.mojang.datafixers.optics.Lens
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEntities
import javax.swing.Box

class ShockwaveEntity : Entity {

    constructor(entityType: EntityType<out ShockwaveEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.SHOCKWAVE as EntityType<out Entity?>, world) {
        this.owner = owner
    }
    val BLOCK_RANGE = 1.0
    val BLOCK_WIDTH = 0.5

    var owner : Entity? = null
    var damage = 0.0f
    var knockback = 0.0
    var distance = 0
    var speed = 0

    var distance_yet = 0

    override fun tick() {
        if(this.distance_yet >= distance){
            this.discard()
        }
        var entities = mutableListOf<Entity>()
        entities.addAll(
            world.getOtherEntities(
                this, net.minecraft.util.math.Box(
                    this.pos.x + this.distance,
                    this.pos.y + this.BLOCK_WIDTH,
                    this.pos.z + this.distance,
                    this.pos.x - this.distance,
                    this.pos.y - this.BLOCK_WIDTH,
                    this.pos.z - this.distance
                )
            ).filter { it.distanceTo(this) > (distance_yet - BLOCK_RANGE) && it.distanceTo(this) < (distance_yet + BLOCK_RANGE) && it != this.owner }
        )
        if(entities.isNotEmpty()){
            for(entity in entities){
                if(this.damage > 0){
                    entity.customDamage(AstralDamageTypes.PULVERISED, this.damage, this, this.owner)
                }
                if(this.knockback > 0){
                    entity.addVelocity(0.0,this.knockback, 0.0)
                    entity.velocityDirty = true
                }
            }
        }
        //need to figure out how to do a ring of particles or what actual effect to give it.
        super.tick()
    }

    override fun initDataTracker(builder: DataTracker.Builder?) {
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }

}