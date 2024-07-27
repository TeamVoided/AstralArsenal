package org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams

import arrow.core.Predicate
import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleEffect
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.astralarsenal.item.RailgunItem
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import kotlin.math.roundToInt

abstract class AbstractRailgunKosmogliph(
    id: Identifier,
    applicationPredicate: Predicate<ItemStack> = { it.item is RailgunItem },
) : SimpleKosmogliph(id, applicationPredicate) {
    open fun raycast(
        world: World,
        player: PlayerEntity,
        maxDistance: Double,
        particle: ParticleEffect,
        particleCount: Int,
        particleDelta: Vec3d,
        particleSpeed: Double,
        extra: (delta: Double, hr: HitResult) -> Unit = { _, _ -> }
    ) = raycast(world, player, maxDistance) inner@{ delta, hr ->
        val pos = player.eyePos.lerp(hr.pos, delta)
        if (world.isClient || world !is ServerWorld) return@inner
        world.spawnParticles(
            particle,
            pos.x,
            pos.y,
            pos.z,
            particleCount,
            particleDelta.x,
            particleDelta.y,
            particleDelta.z,
            particleSpeed
        )
        extra(delta, hr)
    }

    open fun raycast(
        world: World,
        player: PlayerEntity,
        maxDistance: Double,
        intervalLoop: (delta: Double, hr: HitResult) -> Unit = { _, _ -> }
    ): Pair<List<Entity>, HitResult> {
        val result = player.raycast(maxDistance, 0f, false)
        val distance = player.eyePos.distanceTo(result.pos)
        val entities = mutableListOf<Entity>()

        val intervalCheck = (distance * 2).roundToInt()
        (0..intervalCheck).forEach { i ->
            val delta = i / intervalCheck.toDouble()
            val pos = player.eyePos.lerp(result.pos, delta)
            entities.addAll(world.getOtherEntities(player, Box(pos, pos)))
            intervalLoop(delta, result)
        }

        return entities to result
    }

    open fun List<Entity>.damageAll(source: DamageSource, amount: Float) {
        forEach { entity -> entity.damage(source, amount) }
    }
}