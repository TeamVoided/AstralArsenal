package org.teamvoided.astralarsenal.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.payload.CustomPayload
import org.teamvoided.astralarsenal.AstralArsenal

object DashKosmogliphPayload : CustomPayload {
    override fun getId() = ID

    val CODEC = PacketCodec.unit<PacketByteBuf, DashKosmogliphPayload>(DashKosmogliphPayload)
    val ID = CustomPayload.Id<DashKosmogliphPayload>(AstralArsenal.id("dash_kosmogliph_payload"))
}