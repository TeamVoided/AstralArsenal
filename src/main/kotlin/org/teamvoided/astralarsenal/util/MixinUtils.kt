package org.teamvoided.astralarsenal.util

import net.minecraft.entity.LivingEntity
import org.teamvoided.astralarsenal.mixin.LastDamageTakenAccessor

val LivingEntity.lastDamageTaken: Float
    get() = (this as LastDamageTakenAccessor).getLastDamageTaken()