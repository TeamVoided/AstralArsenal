package org.teamvoided.astralarsenal.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.payload.CustomPayload
import org.teamvoided.astralarsenal.AstralArsenal

object SlamKosmogliphPayload : CustomPayload {
    override fun getId() = ID

    val CODEC = PacketCodec.unit<PacketByteBuf, SlamKosmogliphPayload>(SlamKosmogliphPayload)
    val ID = CustomPayload.Id<SlamKosmogliphPayload>(AstralArsenal.id("slam_kosmogliph_payload"))
}