package org.teamvoided.astralarsenal.entity.astralenemies.goals

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.util.math.Box
import org.teamvoided.astralarsenal.entity.BeamOfLightEntity
import org.teamvoided.astralarsenal.entity.astralenemies.AstralStrikerEntity

class StrikeGoal(val entity: AstralStrikerEntity) : Goal() {

    override fun canStart(): Boolean {
        return (entity.target != null && entity.distanceTo(entity.target) <= 50)
    }

    override fun start() {
        super.start()
    }

    override fun stop() {
        entity.enraged = false
        super.stop()
    }

    override fun shouldContinue(): Boolean {
        return (entity.target != null && !entity.target!!.isAlive && entity.distanceTo(entity.target) <= 100)
    }

    override fun tick() {
        if (entity.cooldown <= 0) {
            val random = entity.world.random.rangeInclusive(1, 3)
            when (random) {
                1 -> singleStrike(entity, entity.strikesOnTarget)
                2 -> trippleStrike(entity, entity.strikesOnTarget)
                3 -> largeStrike(entity, entity.strikesOnTarget)
            }
            entity.strikesOnTarget++
            if (entity.strikesOnTarget >= entity.getStrikesBeforeEnrage(entity.world) && getNearbyStrikers(entity) <2) {
                entity.enraged = true
            }
            entity.cooldown = if (entity.enraged) 75 else 150
        } else {
            entity.cooldown--
        }
        super.tick()
    }

    fun singleStrike(entity: AstralStrikerEntity, strikes: Int) {
        val world = entity.world
        if (entity.target != null) {
            val target = entity.target
            val beam = BeamOfLightEntity(entity.world, entity)
            beam.setPosition(target!!.pos)
            beam.WINDUP = 60
            beam.TIMEACTIVE = 40 + entity.getTimeStrikeLasts(world)
            beam.side = 2
            beam.THRUST = 1.0
            beam.targetEntity = target
            beam.DOT = false
            beam.DMG = 5
            beam.trackTime = 50
            beam.hard_damage = 0
            beam.enraged = ((strikes > entity.getStrikesBeforeEnrage(world) && getNearbyStrikers(entity) <2) || entity.enraged)
            entity.world.spawnEntity(beam)
        }
    }

    fun trippleStrike(entity: AstralStrikerEntity, strikes: Int) {
        val world = entity.world
        if (entity.target != null) {
            for (i in 1..3) {
                val target = entity.target
                val beam = BeamOfLightEntity(entity.world, entity)
                beam.setPosition(target!!.pos)
                beam.WINDUP = (i * 10) + 20
                beam.TIMEACTIVE = 50 + entity.getTimeStrikeLasts(world)
                beam.side = 1
                beam.THRUST = 0.3
                beam.targetEntity = target
                beam.DOT = false
                beam.DMG = 3
                beam.trackTime = (i * 10) + 10
                beam.hard_damage = 0
                beam.enraged = ((strikes > entity.getStrikesBeforeEnrage(world) && getNearbyStrikers(entity) <2) || entity.enraged)
                entity.world.spawnEntity(beam)
            }
        }
    }

    fun largeStrike(entity: AstralStrikerEntity, strikes: Int) {
        val world = entity.world
        if (entity.target != null) {
            val target = entity.target
            val beam = BeamOfLightEntity(entity.world, entity)
            beam.setPosition(target!!.pos)
            beam.WINDUP = 100
            beam.TIMEACTIVE = 50 + entity.getTimeStrikeLasts(world)
            beam.side = 5
            beam.THRUST = 2.0
            beam.targetEntity = target
            beam.DOT = false
            beam.DMG = 15
            beam.trackTime = 75
            beam.hard_damage = 0
            beam.enraged = ((strikes > entity.getStrikesBeforeEnrage(world) && getNearbyStrikers(entity) <2) || entity.enraged)
            entity.world.spawnEntity(beam)
        }
    }

    fun getNearbyStrikers(entity: AstralStrikerEntity): Int {
        var strikers = 0
        val list = mutableListOf<Entity>()
        list.addAll(
            entity.world.getOtherEntities(
                entity, Box(
                    entity.x + 50,
                    entity.y + 50,
                    entity.z + 50,
                    entity.x - 50,
                    entity.y - 50,
                    entity.z - 50
                )
            ).filter { it is AstralStrikerEntity }
        )
        for (x in list){
            strikers++
        }

        return strikers
    }
}