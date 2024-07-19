package org.teamvoided.astralarsenal.util

import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.init.AstralItemComponents.KOSMOGLIPHS
import org.teamvoided.astralarsenal.item.components.KosmogliphsComponent

fun <T, R: Registry<T>> RegistryKey<R>.tag(id: Identifier) = TagKey.of(this, id)

fun getKosmogliphsOnStack(stack: ItemStack): KosmogliphsComponent {
    val component = stack.components.get(KOSMOGLIPHS)
        ?: return KosmogliphsComponent()
    return component
}