package org.teamvoided.astralarsenal.networking

import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.payload.CustomPayload
import org.teamvoided.astralarsenal.AstralArsenal

class UpdateHexRingPayload(val entityId: Int, val color: Int) : CustomPayload {

    override fun getId(): CustomPayload.Id<out CustomPayload?>? {
        return ID
    }

    companion object {

        val ID = CustomPayload.Id<UpdateHexRingPayload>(AstralArsenal.id("update_hex_ring"))
        val PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INT, UpdateHexRingPayload::entityId,
            PacketCodecs.INT, UpdateHexRingPayload::color,
            ::UpdateHexRingPayload
        )
    }
}