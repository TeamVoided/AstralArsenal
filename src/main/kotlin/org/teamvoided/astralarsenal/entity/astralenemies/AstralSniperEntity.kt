package org.teamvoided.astralarsenal.entity.astralenemies

import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.TargetGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.FlyingEntity
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Vec3d
import net.minecraft.world.Difficulty
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.entity.astralenemies.goals.*
import org.teamvoided.astralarsenal.entity.astralenemies.movecontrol.AstralFlyingEntityMoveControl

class AstralSniperEntity(entityType: EntityType<out AstralSniperEntity>, world: World) : AstralFlyingEnemyEntity(entityType, world), Monster {


    var snipeType
        get() = SnipeType.getById(dataTracker.get(SNIPE_TYPE))
        set(value) = dataTracker.set(SNIPE_TYPE, value.id)
    var snipesOnTarget = 0
    var cooldown = 0
    var timeBeforeShot = 80
    var shotBufferTime = 20
    var enraged = false
    var takenSecondShot = false
    var targetPoint : Vec3d? = null
    var isShooting = false

    override fun cannotDespawn(): Boolean {
        return true
    }

    override fun initGoals() {
        goalSelector.add(5, HoverRandomlyGoal(this, 40.0, 5.0))
        goalSelector.add(2, SnipeGoal(this))
        goalSelector.add(2, LookAtPointGoal(this))
        targetSelector.add(
            1, TargetGoal(
                this, PlayerEntity::
                class.java, 1, false, false
            ) { ((it.distanceTo(this)) <= 100) }
        )
    }

    enum class SnipeType(val id: Int) {
        UNASSIGNED(0), SINGLE(1), DOUBLE(2), EXPLOSIVE(3), RANCID(4), ICY(5);

        companion object {
            fun getById(id: Int): SnipeType = entries.first { it.id == id }
        }
    }
    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(SNIPE_TYPE, 0)
        super.initDataTracker(builder)
    }
    companion object {
        val SNIPE_TYPE: TrackedData<Int> =
            DataTracker.registerData(AstralSniperEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
//        val GARBAGE: TrackedData<Int> =
//            DataTracker.registerData(AstralSniperEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        fun createMobAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 100.0)
                .add(EntityAttributes.GENERIC_ARMOR, 5.0)
        }
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        nbt.putInt("snipe_type", snipeType.id)
        super.writeCustomDataToNbt(nbt)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        this.snipeType = SnipeType.getById(nbt.getInt("snipe_type"))
        super.readCustomDataFromNbt(nbt)
    }

    override fun tick(){
        if(this.snipeType == SnipeType.UNASSIGNED){
            val random = this.world.random.rangeInclusive(1,5)
            this.snipeType = SnipeType.getById(random)
        }
        if (this.target == null) {
            this.cooldown = 0
            this.snipesOnTarget = 0
            this.enraged = false
            this.targetPoint = null
            this.timeBeforeShot = 80
            this.shotBufferTime = 20
        }
        super.tick()
    }
    init {
        this.moveControl = AstralFlyingEntityMoveControl(this, 30.0)
    }

    fun getShotsBeforeEnrage(world: World) : Int{
        return if(world.difficulty == Difficulty.HARD) 1 else if(world.difficulty == Difficulty.NORMAL) 2 else 8
    }

    override fun applyEnchantmentsToDamage(source: DamageSource, amount: Float): Float {
        var outputDamage = amount
        if(source.isTypeIn(AstralDamageTypeTags.IS_PLASMA)) outputDamage *= 0f
        if(source.isTypeIn(AstralDamageTypeTags.IS_MELEE)) outputDamage *= 1.5f
        return outputDamage
    }
    override fun isInvulnerableTo(source: DamageSource): Boolean {
        if(source.isTypeIn(AstralDamageTypeTags.IS_PLASMA)) return true
        return super.isInvulnerableTo(source)
    }
}