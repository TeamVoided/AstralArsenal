package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.entity.Entity

abstract class OwnedExplosionBehavior(val owner: Entity) : BlockSafeExplosionBehavior()