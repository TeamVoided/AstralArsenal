package org.teamvoided.astralarsenal.effects

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.TridentEntity
import net.minecraft.particle.ParticleEffect
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import org.joml.Vector2d
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.mixin.PersistentProjectileEntityAccessor
import org.teamvoided.astralarsenal.mixin.TridentEntityAccessor
import org.teamvoided.astralarsenal.util.sillyLightningTime
import kotlin.math.absoluteValue
import kotlin.math.sign

class MagneticStatusEffect : AstralStatusEffect {
    constructor(type: StatusEffectType, color: Int, showTomeRing: Boolean) : super(type, color, showTomeRing)
    constructor(type: StatusEffectType, color: Int, particle: ParticleEffect) : super(type, color, particle)

    val preventors = listOf(
        AstralEffects.IMMORTAL
    )

    override fun shouldApplyUpdateEffect(tick: Int, amplifier: Int): Boolean {
        return true
    }

    val range = 5
    val strength = 0.25

    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int): Boolean {
        val effects = entity.statusEffects.filter { preventors.contains(it.effectType) }
        if (effects.isEmpty()) {
            val nearbyProjectiles = mutableListOf<Entity>()
            nearbyProjectiles.addAll(
                entity.world.getOtherEntities(
                    entity, Box(
                        entity.x + range,
                        entity.eyeY + range,
                        entity.z + range,
                        entity.x - range,
                        entity.eyeY - range,
                        entity.z - range
                    )
                )
                    .filter {
                        it is ProjectileEntity && (it !is PersistentProjectileEntity || !(it as PersistentProjectileEntityAccessor).inGround) && (entity.eyePos.distanceTo(
                            it.pos
                        ) <= range)
                    })
            for (projectile in nearbyProjectiles) {
                if ((projectile !is CannonballEntity || projectile.getDmg() < 15)
                    && (projectile is ProjectileEntity && projectile.owner != entity)
                    && (projectile !is TridentEntity || !(projectile as TridentEntityAccessor).dealtDamage())
                ) {
                    val str = strength * (amplifier + 1.0)
                    val desiredVec = entity.eyePos.subtract(projectile.eyePos)
                    val change = desiredVec.subtract(projectile.velocity).normalize().multiply(str)
                    projectile.addVelocity(change)
                    projectile.velocityModified
                }
            }
            sparkNearbyEntities(entity, entity)
        }
        return super.applyUpdateEffect(entity, amplifier)
    }

    fun sparkNearbyEntities(cause: Entity, base: Entity) {
        val entities = mutableListOf<Entity>()
        entities.addAll(
            base.world.getOtherEntities(
                cause, Box(
                    base.x + range,
                    base.eyeY + range,
                    base.z + range,
                    base.x - range,
                    base.eyeY - range,
                    base.z - range
                )
            ).filter {
                it is ProjectileEntity && (it !is PersistentProjectileEntity || !(it as PersistentProjectileEntityAccessor).inGround) && it != cause && it != base && base.eyePos.distanceTo(
                    it.pos
                ) <= range
            }
        )
        if (entities.isNotEmpty()) {
            for (entiity in entities) {
                if ((entiity !is CannonballEntity || entiity.getDmg() < 15)
                    && (entiity is ProjectileEntity && entiity.owner != base)
                    && (entiity !is TridentEntity || !(entiity as TridentEntityAccessor).dealtDamage())
                ) {
                    if (base.world is ServerWorld) {
                        sillyLightningTime(
                            Vec3d(base.pos.x, base.pos.y + (base.height / 2f), base.pos.z),
                            Vec3d(entiity.pos.x, entiity.pos.y + (entiity.height / 2f), entiity.pos.z),
                            ((base.world as ServerWorld)),
                            2,
                            5,
                            1,
                            0.03f,
                            0.5
                        )
                    }
                }
            }
        }
    }
}