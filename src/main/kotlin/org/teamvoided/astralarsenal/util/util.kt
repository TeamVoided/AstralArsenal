package org.teamvoided.astralarsenal.util

import arrow.core.Predicate
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.Holder
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.joml.Vector3f
import org.teamvoided.astralarsenal.components.KosmogliphsComponent
import org.teamvoided.astralarsenal.entity.FreezeShotEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralItemComponents.KOSMOGLIPHS
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.kosmogliph.logic.setShootVelocity
import org.teamvoided.astralarsenal.kosmogliph.ranged.BowKosmogliph
import kotlin.math.roundToInt

fun <T, R : Registry<T>> RegistryKey<R>.tag(id: Identifier) = TagKey.of(this, id)

fun <T> Registry<T>.registerHolder(id: Identifier, entry: T): Holder.Reference<T> =
    Registry.registerHolder(this, id, entry)

fun <T> Registry<T>.register(id: Identifier, entry: T): T = Registry.register(this, id, entry)

fun getKosmogliphsOnStack(stack: ItemStack): KosmogliphsComponent {
    val component = stack.components.get(KOSMOGLIPHS)
        ?: return KosmogliphsComponent()
    return component
}

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

// damage is the amount of damage the player would take if the shield didn't block it
// attackingEntity is the entity that does the damage e.g. a mob or a projectile
// sourceEntity is the entity that caused the attacking entity e.g. a player that shot an arrow
fun shieldDamage(target: Entity, attackingEntity: Entity?, sourceEntity: Entity?, damage: Float, source: DamageSource) {
    if (target is LivingEntity) {
        val shield = target.activeItem
        if (getKosmogliphsOnStack(shield).contains(AstralKosmogliphs.PARRY)) {
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
                        ), (damage * 2)
                    )
                }
            }
        } else if (getKosmogliphsOnStack(shield).contains(AstralKosmogliphs.FROST_THORNS)) {
            val num = (damage / 2).roundToInt() + 1
            repeat(num) {
                val freezeBallEntity = FreezeShotEntity(target.world, target)
                freezeBallEntity.setPosition(target.pos.x, target.pos.y + 1, target.pos.z)
                freezeBallEntity.setShootVelocity(target.pitch, target.yaw, 0.0f, 1.0f, 20f)
                target.world.spawnEntity(freezeBallEntity)
            }
        }
    }
}