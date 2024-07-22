package org.teamvoided.astralarsenal.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.payload.CustomPayload
import net.minecraft.util.math.Vec3d
import org.teamvoided.astralarsenal.AstralArsenal

data class LaserBeamPayload(
    val start: Vec3d,
    val end: Vec3d
) : CustomPayload {
    companion object {
        val CODEC: PacketCodec<PacketByteBuf, LaserBeamPayload> = PacketCodec.create(::encode, ::decode)
        val ID = CustomPayload.Id<LaserBeamPayload>(AstralArsenal.id("laser_beam_payload"))

        fun encode(buf: PacketByteBuf, payload: LaserBeamPayload) {
            buf.writeVec3d(payload.start)
            buf.writeVec3d(payload.end)
        }

        fun decode(buf: PacketByteBuf): LaserBeamPayload {
            val start = buf.readVec3d()
            val end = buf.readVec3d()
            return LaserBeamPayload(start, end)
        }
    }

    override fun getId() = ID
}