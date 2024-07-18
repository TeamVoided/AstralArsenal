package org.teamvoided.astralarsenal.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.payload.CustomPayload
import org.teamvoided.astralarsenal.AstralArsenal

object JumpKosmogliphPayload : CustomPayload {
    override fun getId() = ID

    val CODEC = PacketCodec.unit<PacketByteBuf, JumpKosmogliphPayload>(JumpKosmogliphPayload)
    val ID = CustomPayload.Id<JumpKosmogliphPayload>(AstralArsenal.id("jump_kosmogliph_payload"))
}