package org.teamvoided.astralarsenal.init

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.AstralArsenal.id

object AstralSounds {
    val BEAM_BOOM = register("entity.beam_of_light.strike")
    val BEAM_VIBRATE = register("entity.beam_of_light.vibrate")
    val BEAM_WIND = register("entity.beam_of_light.wind")
    fun init() {
    }

    private fun register(id: String): SoundEvent {
        return register(id(id))
    }

    private fun register(id: Identifier): SoundEvent {
        return register(id, id)
    }

    private fun register(id: Identifier, soundId: Identifier): SoundEvent {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(soundId))
    }
}