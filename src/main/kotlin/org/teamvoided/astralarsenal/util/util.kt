package org.teamvoided.astralarsenal.util

import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

fun <T, R: Registry<T>> RegistryKey<R>.tag(id: Identifier) = TagKey.of(this, id)
