package org.teamvoided.astralarsenal.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.payload.CustomPayload
import org.teamvoided.astralarsenal.AstralArsenal

object SlideKosmogliphPayload : CustomPayload {
    override fun getId() = ID

    val CODEC = PacketCodec.unit<PacketByteBuf, SlideKosmogliphPayload>(SlideKosmogliphPayload)
    val ID = CustomPayload.Id<SlideKosmogliphPayload>(AstralArsenal.id("slide_kosmogliph_payload"))
}