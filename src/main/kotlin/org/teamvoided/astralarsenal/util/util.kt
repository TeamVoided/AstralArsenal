package org.teamvoided.astralarsenal.util

import arrow.core.Predicate
import net.minecraft.component.DataComponentTypes
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.*
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.Holder
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.joml.Vector3f
import org.teamvoided.astralarsenal.components.PulveriserData
import org.teamvoided.astralarsenal.components.SlamData
import org.teamvoided.astralarsenal.entity.FreezeShotEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDataComponents.PULVERISER_DATA
import org.teamvoided.astralarsenal.init.AstralDataComponents.SLAM_DATA
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.init.AstralParticles
import org.teamvoided.astralarsenal.item.NailCannonItem
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.kosmogliph.logic.setShootVelocity
import org.teamvoided.astralarsenal.kosmogliph.ranged.BowKosmogliph
import org.teamvoided.astralarsenal.world.explosion.maceExplosions.MacePulverise
import org.teamvoided.astralarsenal.world.explosion.maceExplosions.MaceStrongPulverise
import org.teamvoided.astralarsenal.world.explosion.maceExplosions.MaceWeakPulverise
import kotlin.math.min

fun <T, R : Registry<T>> RegistryKey<R>.tag(id: Identifier) = TagKey.of(this, id)

fun <T> Registry<T>.registerHolder(id: Identifier, entry: T): Holder.Reference<T> =
    Registry.registerHolder(this, id, entry)

fun <T> Registry<T>.register(id: Identifier, entry: T): T = Registry.register(this, id, entry)

fun interface BPredicate<T> : Predicate<T>, java.util.function.Predicate<T> {
    override fun test(t: T): Boolean = this(t)
}

fun Iterable<Kosmogliph>.findFirstBow(): BowKosmogliph? {
    return this.firstOrNull { it is BowKosmogliph } as BowKosmogliph?
}


fun ItemStack.hasMultiShot(): Boolean = this.hasEnchantment(Enchantments.MULTISHOT)
fun ItemStack.hasEnchantment(enchantment: RegistryKey<Enchantment>): Boolean =
    this.enchantments.enchantments.any { it.isRegistryKey(enchantment) }

fun Vector3f.toVec3d(): Vec3d = Vec3d(x.toDouble(), y.toDouble(), z.toDouble())
fun ProjectileEntity.setVelocity(vec3f: Vector3f, speed: Float, divergence: Float) =
    this.setVelocity(vec3f.toVec3d(), speed, divergence)

fun ProjectileEntity.setVelocity(vec3d: Vec3d, speed: Float, divergence: Float) =
    this.setVelocity(vec3d.x, vec3d.y, vec3d.z, speed, divergence)

fun World.playSound(pos: Vec3d, soundEvent: SoundEvent, category: SoundCategory, volume: Float, pitch: Float) {
    this.playSound(null, pos.x, pos.y, pos.z, soundEvent, category, volume, pitch)
}

fun World.playSound(pos: Vec3d, soundEvent: Holder<SoundEvent>, category: SoundCategory, volume: Float, pitch: Float) {
    this.method_60511(null, pos.x, pos.y, pos.z, soundEvent, category, volume, pitch)
}

fun ServerCommandSource.message(string: String): Int {
    this.sendSystemMessage(Text.literal(string))
    return 0
}

fun ServerCommandSource.error(text: String): Int {
    this.sendError(Text.literal(text))
    return -1
}


val PARRY_DAMAGE_MULT = 1.25

// damage is the amount of damage the player would take if the shield didn't block it
// attackingEntity is the entity that does the damage e.g. a mob or a projectile
// sourceEntity is the entity that caused the attacking entity e.g. a player that shot an arrow
fun shieldDamage(target: Entity, attackingEntity: Entity?, sourceEntity: Entity?, damage: Float, source: DamageSource) {
    if (target is LivingEntity) {
        val shield = target.activeItem
        if (shield.hasKosmogliph(AstralKosmogliphs.PARRY)) {
            if (target.itemUseTime < 6) {
                target.world.playSound(
                    null,
                    target.x,
                    target.y,
                    target.z,
                    SoundEvents.ITEM_SHIELD_BREAK,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0f
                )
                target.world.playSound(
                    null,
                    target.x,
                    target.y,
                    target.z,
                    SoundEvents.BLOCK_ANVIL_PLACE,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0f
                )
                if (attackingEntity is LivingEntity) {
                    attackingEntity.damage(
                        DamageSource(
                            AstralDamageTypes.getHolder(attackingEntity.world.registryManager, AstralDamageTypes.PARRY),
                            target,
                            target
                        ), (damage * PARRY_DAMAGE_MULT).toFloat()
                    )
                }
            }
            if (target is PlayerEntity) {
                target.itemCooldownManager.set(shield.item, 10)
                target.stopUsingItem()
                if (source.attacker != target) {
                    target.heal(2f)
                }
            }
        } else if (shield.hasKosmogliph(AstralKosmogliphs.FROST_THORNS)) {
            repeat(5) {
                val freezeBallEntity = FreezeShotEntity(target.world, target)
                freezeBallEntity.setPosition(target.pos.x, target.pos.y + 1, target.pos.z)
                freezeBallEntity.setShootVelocity(target.pitch, target.yaw, 0.0f, 1.0f, 20f)
                target.world.spawnEntity(freezeBallEntity)
            }
        }
    }
}

fun fall(fallDistance: Double, onGround: Boolean, entity: LivingEntity, landedPos: BlockPos) {
    val faller = entity as PlayerEntity
    val serverFaller = if (faller is ServerPlayerEntity) {
        faller
    } else null
    if (faller.isOnGround) {
        val stack: ItemStack? = if (faller.getStackInHand(Hand.MAIN_HAND).hasKosmogliph(AstralKosmogliphs.PULVERISER)) {
            faller.getStackInHand(Hand.MAIN_HAND)
        } else if (faller.getStackInHand(Hand.OFF_HAND).hasKosmogliph(AstralKosmogliphs.PULVERISER)) {
            faller.getStackInHand(Hand.OFF_HAND)
        } else null
        if (stack != null) {
            val pulveriserData = stack.getOrDefault(PULVERISER_DATA, PulveriserData.DEFAULT)
            val ticks = pulveriserData.ticks
            if (pulveriserData.slamming) {
                val explosionBehavior = (
                        if (ticks >= 100) MaceStrongPulverise(faller)
                        else if (ticks >= 50) MacePulverise(faller)
                        else MaceWeakPulverise(faller)
                        )
                val sound = (
                        if (ticks >= 100 || (ticks >= 50 && entity.fallDistance >= 10.0)) SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY
                        else if (ticks >= 50 || entity.fallDistance >= 10.0) SoundEvents.ITEM_MACE_SMASH_GROUND
                        else SoundEvents.ITEM_MACE_SMASH_AIR
                        )
                val gustSize = (
                        if (ticks >= 100 || (ticks >= 50 && entity.fallDistance >= 10.0)) ParticleTypes.GUST_EMITTER_LARGE
                        else ParticleTypes.GUST_EMITTER_SMALL
                        )
                val power = min((2.0 + (0.03 * faller.fallDistance)).toFloat(), 5.0f)
                faller.world.createExplosion(
                    faller, faller.damageSources.explosion(null, faller),
                    explosionBehavior,
                    faller.x,
                    faller.y,
                    faller.z,
                    power,
                    false,
                    World.ExplosionSourceType.MOB,
                    ParticleTypes.GUST,
                    gustSize,
                    SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST
                )
                faller.playSound(sound)
                stack.set(PULVERISER_DATA, PulveriserData(0, false))
                if (!faller.isCreative) {
                    faller.itemCooldownManager.set(stack.item, 200)
                }
                faller.resetFallDistance()
            }
        }
    }
    var stack: ItemStack? = null
    if (faller.getEquippedStack(EquipmentSlot.HEAD).hasKosmogliph(AstralKosmogliphs.SLAM)) {
        stack = faller.getEquippedStack(EquipmentSlot.HEAD)
    }
    if (stack != null) {
        if (faller.isOnGround) {
            val isSlamming = stack.getOrDefault(SLAM_DATA, SlamData.DEFAULT).slamming
            if (isSlamming) {
                if (faller.isOnGround) {
                    faller.playSound(SoundEvents.ITEM_MACE_SMASH_GROUND)
                    faller.addStatusEffect(
                        StatusEffectInstance(
                            AstralEffects.SLAM_JUMP,
                            20,
                            //(faller.fallDistance + 2).roundToInt(),  [previous amplifier equation in case we want to bring it back - Astra]
                            5,
                            false,
                            false,
                            true
                        )
                    )
                    stack.set(SLAM_DATA, SlamData(0.0f, false))
//                    if(entity.world is ServerWorld){
//                        val serverWorld = entity.world as ServerWorld
//                        val slamEntity = ShockwaveEntity(entity.world, entity)
//                        slamEntity.speed = 0.1
//                        slamEntity.distance = 10.0
//                        slamEntity.knockback = 3.0
//                        slamEntity.setPosition(entity.pos)
//                        serverWorld.spawnEntity(slamEntity)
//                    } [This will come back and be tweaked for 2.0

                    faller.resetFallDistance()
                } else {
                    faller.setVelocity(0.0, -5.0, 0.0)
                    faller.velocityModified
                    faller.addStatusEffect(
                        StatusEffectInstance(
                            AstralEffects.SLAM_JUMP,
                            20,
                            //(faller.fallDistance + 2).roundToInt(),
                            5,
                            false,
                            false,
                            true
                        )
                    )
                }
            }
        }
    }
}

fun tickMovement(freezer: LivingEntity) {
    if (freezer.world is ServerWorld && freezer.age % 4 == 0 && freezer.canFreeze() && freezer.frozenTicks > 0) {
        val serverWorld = freezer.world as ServerWorld
        serverWorld.spawnParticles(
            AstralParticles.SNOWFLAKE,
            freezer.x,
            freezer.y + (freezer.height) / 2,
            freezer.z,
            1,
            (freezer.width / 2).toDouble(),
            (freezer.height / 2).toDouble(),
            (freezer.width / 2).toDouble(),
            0.0
        )
    }
    if (freezer.world is ServerWorld && freezer.age % 40 == 0 && freezer.canFreeze() && freezer.isFrozen) {
        val serverWorld = freezer.world as ServerWorld
        serverWorld.spawnParticles(
            AstralParticles.SNOWFLAKE,
            freezer.x,
            freezer.y + (freezer.height) / 2,
            freezer.z,
            5,
            (freezer.width / 2).toDouble(),
            (freezer.height / 2).toDouble(),
            (freezer.width / 2).toDouble(),
            0.1
        )
    }
}

// Only available client side
// 0.2 is regular speed while using an item, 1 is regular player speed
// This should be changed to use item tags.
fun modifyItemUseSpeed(player: PlayerEntity, stack: ItemStack): Float {
    if (stack.item is BowItem) {
        return 0.5f
    } else if (stack.item is ShieldItem) {
        return if (stack.hasKosmogliph(AstralKosmogliphs.PARRY)) 1f else 0.3f
    } else if (stack.item is SwordItem || stack.item is AxeItem) {
        return if (stack.hasKosmogliph(AstralKosmogliphs.DEEP_WOUNDS)) 0.4f else 1f
    } else if (stack.item is CrossbowItem) {
        return 0.5f
    } else if (stack.item is NailCannonItem) {
        return 0.8f
    } else if (stack.item is MaceItem) {
        return 0.7f
    } else if (stack.item is PotionItem) {
        return 0.4f
    } else if (stack.item.components.contains(DataComponentTypes.FOOD)){
        return 0.4f
    }

    return 0.5f
}