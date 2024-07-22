package org.teamvoided.astralarsenal.item.kosmogliph.ranged

import com.google.common.base.MoreObjects
import com.google.common.base.Predicates
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ChargedProjectilesComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.item.CrossbowItem
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Arm
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.networking.LaserBeamPayload

class BeamKosmogliph(
    id: Identifier,
) : SimpleKosmogliph(id, { AstralArsenal.LOGGER.info("{}", it.item is CrossbowItem); it.item is CrossbowItem }), RangedWeaponKosmogliph {
    override fun preUse(world: World, player: PlayerEntity, hand: Hand) {
        if (world.isClient) return

        val stack = player.getStackInHand(hand)
        val chargedProjectiles = stack.get(DataComponentTypes.CHARGED_PROJECTILES)
        val fireLaser = chargedProjectiles != null && !chargedProjectiles.isEmpty
        if (fireLaser) {
            stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT)
            fireLaser(world as ServerWorld, player, stack)
        }
    }

    fun fireLaser(
        world: ServerWorld,
        user: LivingEntity,
        stack: ItemStack
    ): Boolean {
        val hit = compositeRaycast(world, user)
        val start = user.muzzlePos()
        val end = hit.pos
        val sqrDistance = start.distanceTo(end).toInt() * 2
        AstralArsenal.LOGGER.info("{} | {}", hit.pos, sqrDistance)

        val payload = LaserBeamPayload(start, end)
        world.players.forEach { player ->
            ServerPlayNetworking.send(player, payload)
        }

        if (hit is EntityHitResult) {
            val hitEnt = hit.entity
            hitEnt.damage(hitEnt.damageSources.lava(), 10f)
        }

        return true
    }

    fun compositeRaycast(world: World, user: LivingEntity): HitResult {
        val preStart = user.getLerpedEyePos(0f)
        val preEnd = user.getLerpedEyePos(0f).add(user.getRotationVec(0f).multiply(64.0))
        val start = user.getLerpedEyePos(0f)
        val max = user.getLerpedEyePos(0f).add(user.getRotationVec(0f).multiply(256.0))
        val bhr = world.raycast(
            RaycastContext(
                start,
                max,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                user
            ))
        val ehr = ProjectileUtil.getEntityCollision(
            world,
            user,
            start,
            bhr.pos,
            Box(start, max),
            Predicates.alwaysTrue()
        ).correct(start, max)

        return MoreObjects.firstNonNull(ehr, bhr)
    }

    fun EntityHitResult?.correct(start: Vec3d, end: Vec3d): EntityHitResult? {
        if (this == null) return null
        return EntityHitResult(entity, entity.bounds.expand(0.3).raycast(start, end).orElse(end))
    }

    fun Entity.muzzlePos(): Vec3d {
        val arm = if (this is LivingEntity) mainArm else Arm.RIGHT
        val eyes = getLerpedEyePos(0f)
        val look = getRotationVec(0f)
        var right = getRotationVector(0f, getYaw(0f)+90)
        val down = look.crossProduct(right)
        if (arm == Arm.LEFT) right = right.multiply(-1.0)
        return eyes.add(look.multiply(0.5)).add(right.multiply(0.25)).add(down.multiply(0.075))
    }

    class Data(
        val fireLaser: Boolean
    ) {
        companion object {
            val CODEC: Codec<Data> = RecordCodecBuilder.create { builder ->
                val group = builder.group(
                    Codec.BOOL.fieldOf("fireLaser").forGetter { it.fireLaser }
                )

                group.apply(builder, ::Data)
            }
        }
    }
}