package org.teamvoided.astralarsenal.kosmogliph.ranged.beams

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.entity.MortarEntity
import org.teamvoided.astralarsenal.entity.RichochetEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.world.explosion.WeakExplosionBehavior
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class RicochetKosmogliph(id: Identifier) :
    SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_BASIC_RAILGUN) }) {

    //Modify this to change how many times it richochets
    val COUNTDOWN = 20

    //Modify this to change how much damage it does per hit, remember this will ignore cooldowns.
    val DAMAGE = 1.0

    override fun onUse(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack>? {
        val entitiesHit = mutableListOf<Entity>()
        entitiesHit.add(player)
        val combined = player.eyePos.add(player.rotationVector.multiply(100.0))
        val result = world.raycast(
            RaycastContext(
                player.eyePos, combined, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player
            )
        )
        val distance = sqrt(
            sqrt((player.eyePos.x - result.pos.x).pow(2) + (player.eyePos.z - result.pos.z).pow(2)).pow(2) + ((player.eyePos.y - 0.5) - result.pos.y).pow(
                2
            )
        )
        val entities = mutableListOf<Entity>()
        val interval = (distance.times(2))
        for (i in 0..interval.roundToInt()) {
            entities.addAll(
                world.getOtherEntities(
                    player, Box(
                        (lerp(player.eyePos.x, result.pos.x, i / interval)) + 0.5,
                        (lerp(player.eyePos.y - 0.5, result.pos.y, i / interval)) + 0.5,
                        (lerp(player.eyePos.z, result.pos.z, i / interval)) + 0.5,
                        (lerp(player.eyePos.x, result.pos.x, i / interval)) - 0.5,
                        (lerp(player.eyePos.y - 0.5, result.pos.y, i / interval)) - 0.5,
                        (lerp(player.eyePos.z, result.pos.z, i / interval)) - 0.5
                    )
                )
            )
            if (!player.world.isClient) {
                val serverWorld = player.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.END_ROD,
                    (lerp(player.eyePos.x, result.pos.x, i / interval)),
                    (lerp(player.eyePos.y - 0.5, result.pos.y, i / interval)),
                    (lerp(player.eyePos.z, result.pos.z, i / interval)),
                    1,
                    0.2,
                    0.2,
                    0.2,
                    0.0
                )

            }
        }
        world.playSound(
            null,
            player.x,
            player.y,
            player.z,
            AstralSounds.RAILGUN,
            SoundCategory.PLAYERS,
            1.0F,
            1.0f
        )
        for (entity in entities) {
            if (entity is CannonballEntity || entity is MortarEntity) {
                world.createExplosion(
                    entity,
                    entity.damageSources.explosion(entity, player),
                    WeakExplosionBehavior(),
                    entity.x,
                    entity.y,
                    entity.z,
                    2.0f,
                    false,
                    World.ExplosionSourceType.TNT
                )
                entity.discard()
            }
            if (entity is LivingEntity && !entitiesHit.contains(entity)) {
                if (!entitiesHit.contains(entity))
                    entity.damage(
                        DamageSource(
                            AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.RICHOCHET),
                            player,
                            player
                        ), DAMAGE.toFloat()
                    )
                entitiesHit.add(entity)
            }
        }
        if (!player.isCreative) {
            player.itemCooldownManager.set(player.getStackInHand(hand).item, 300)
        }
        if (result.type != HitResult.Type.MISS) {
            val richochet = RichochetEntity(world, player)
            richochet.pitch = player.pitch
            richochet.yaw = player.yaw
            richochet.COUNTDOWN = COUNTDOWN
            richochet.dmg = DAMAGE
            richochet.owner = player
            val y = richochet.yaw
            when (result.side) {
                Direction.DOWN, Direction.UP -> {
                    richochet.pitch *= -1
                    if (result.side == Direction.UP) richochet.setPosition(
                        result.pos.x,
                        result.pos.y + 0.1,
                        result.pos.z
                    )
                    else richochet.setPosition(result.pos.x, result.pos.y - 0.1, result.pos.z)
                }
                // North and sound send it back 180 degrees instead of doing what they should be doing.
                Direction.SOUTH -> {
                    if (richochet.yaw >= 0) richochet.yaw = ((180) - y)
                    else richochet.yaw = ((-180) - y)
                    richochet.setPosition(result.pos.x, result.pos.y, result.pos.z + 0.1)
                }

                Direction.NORTH -> {
                    if (richochet.yaw > 0) richochet.yaw = ((180) - y)
                    else richochet.yaw = ((-180) - y)
                    richochet.setPosition(result.pos.x, result.pos.y, result.pos.z - 0.1)
                }
                //East and west send it further into the block instead of having it richochet.
                Direction.WEST -> {
                    richochet.yaw = y * -1
                    richochet.setPosition(result.pos.x - 0.1, result.pos.y, result.pos.z)
                }

                Direction.EAST -> {
                    richochet.yaw = y * -1
                    richochet.setPosition(result.pos.x + 0.1, result.pos.y, result.pos.z)
                }
                null -> println("fucking die")
            }
            world.spawnEntity(richochet)
        }
        return null
    }
}