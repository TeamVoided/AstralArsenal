package org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams

import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import net.minecraft.util.math.Box
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.entity.MortarEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.RailgunItem
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.world.explosion.StrongExplosionBehavior
import org.teamvoided.astralarsenal.world.explosion.WeakExplosionBehavior
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class BasicRailgunKosmogliph(id: Identifier) :
    SimpleKosmogliph(id, { it.item is RailgunItem }){

    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        val result = player.raycast(100.0, 1f, false)
        val distance = sqrt(sqrt((player.eyePos.x - result.pos.x).pow(2) + (player.eyePos.z - result.pos.z).pow(2)).pow(2) + ((player.eyePos.y - 0.5) - result.pos.y).pow(2))
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
                    10,
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
            if(entity is CannonballEntity || entity is MortarEntity){
                world.createExplosion(entity,
                    entity.damageSources.explosion(entity, player),
                    StrongExplosionBehavior(),
                    entity.x,
                    entity.y,
                    entity.z,
                    2.0f,
                    false,
                    World.ExplosionSourceType.TNT)
                entity.discard()
            }
            val rand = world.random.rangeInclusive(1,10)
            if (entity is LivingEntity) {
            if(rand == 1){
            entity.damage(
                DamageSource(
                    AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.RAILED),
                    player,
                    player
                ), 10f)
            }
            else{
                entity.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.NON_RAILED),
                        player,
                        player
                    ), 10f)
            }
        }}
            if(!player.isCreative){
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 600)
        }
    }

}