package org.teamvoided.astralarsenal.util

import arrow.core.Predicate
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.Holder
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import org.teamvoided.astralarsenal.init.AstralItemComponents.KOSMOGLIPHS
import org.teamvoided.astralarsenal.item.components.KosmogliphsComponent
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.BowKosmogliph

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

// damage is the amount of damage the player would take if the shield didn't block it
// attackingEntity is the entity that does the damage e.g. a mob or a projectile
// sourceEntity is the entity that caused the attacking entity e.g. a player that shot an arrow
fun shieldDamage(target: Entity, attackingEntity: Entity?, sourceEntity: Entity?, damage: Float, source: DamageSource) {
}