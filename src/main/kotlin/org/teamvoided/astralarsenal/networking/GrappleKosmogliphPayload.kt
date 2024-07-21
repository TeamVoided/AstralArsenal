package org.teamvoided.astralarsenal.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.payload.CustomPayload
import org.teamvoided.astralarsenal.AstralArsenal

object GrappleKosmogliphPayload : CustomPayload {
    override fun getId() = ID

    val CODEC = PacketCodec.unit<PacketByteBuf, GrappleKosmogliphPayload>(GrappleKosmogliphPayload)
    val ID = CustomPayload.Id<GrappleKosmogliphPayload>(AstralArsenal.id("grapple_kosmogliph_payload"))
}