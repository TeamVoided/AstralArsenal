package org.teamvoided.astralarsenal.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.payload.CustomPayload
import org.teamvoided.astralarsenal.AstralArsenal

object DodgeKosmogliphPayload : CustomPayload {
    override fun getId() = ID

    val CODEC = PacketCodec.unit<PacketByteBuf, DodgeKosmogliphPayload>(DodgeKosmogliphPayload)
    val ID = CustomPayload.Id<DodgeKosmogliphPayload>(AstralArsenal.id("dodge_kosmogliph_payload"))
}