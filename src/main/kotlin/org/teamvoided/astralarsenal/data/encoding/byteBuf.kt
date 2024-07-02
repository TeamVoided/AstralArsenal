package org.teamvoided.astralarsenal.data.encoding

import io.netty.buffer.Unpooled
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf

open class ByteBufFormat(override val serializersModule: SerializersModule): SerialFormat {
    companion object Default: ByteBufFormat(EmptySerializersModule())

    fun <T> encodeToPacketByteBuf(serializer: SerializationStrategy<T>, value: T): PacketByteBuf {
        val buf = PacketByteBuf(Unpooled.buffer())
        encodeIntoPacketByteBuf(serializer, value, buf)
        return buf
    }

    fun <T> encodeIntoPacketByteBuf(serializer: SerializationStrategy<T>, value: T, buffer: PacketByteBuf) {
        val encoder = ByteBufEncoder(serializersModule, buffer)
        serializer.serialize(encoder, value)
    }

    fun <T> decodeFromPacketByteBuf(deserializer: DeserializationStrategy<T>, buffer: PacketByteBuf): T {
        val decoder = ByteBufDecoder(serializersModule, buffer)
        return deserializer.deserialize(decoder)
    }
}

inline fun <reified T> ByteBufFormat.encodeToPacketByteBuf(value: T): PacketByteBuf {
    return encodeToPacketByteBuf(serializersModule.serializer(), value)
}

inline fun <reified T> ByteBufFormat.encodeIntoPacketByteBuf(value: T, buf: PacketByteBuf) {
    encodeIntoPacketByteBuf(serializersModule.serializer(), value, buf)
}

inline fun <reified T> ByteBufFormat.decodeFromPacketByteBuf(buf: PacketByteBuf): T {
    return decodeFromPacketByteBuf(serializersModule.serializer(), buf)
}

@OptIn(ExperimentalSerializationApi::class)
class ByteBufEncoder(override val serializersModule: SerializersModule, private val buffer: PacketByteBuf):
    AbstractEncoder(), NbtCompoundEncoder {
    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
        return super.beginCollection(descriptor, collectionSize).also {
            buffer.writeInt(collectionSize)
        }
    }

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        val name = descriptor.getElementName(index)
        buffer.writeString(name)
        return true
    }

    override fun encodeBoolean(value: Boolean) {
        buffer.writeBoolean(value)
    }

    override fun encodeByte(value: Byte) {
        buffer.writeByte(value.toInt())
    }

    override fun encodeChar(value: Char) {
        buffer.writeChar(value.code)
    }

    override fun encodeDouble(value: Double) {
        buffer.writeDouble(value)
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        buffer.writeInt(index)
    }

    override fun encodeFloat(value: Float) {
        buffer.writeFloat(value)
    }

    override fun encodeInt(value: Int) {
        buffer.writeInt(value)
    }

    override fun encodeLong(value: Long) {
        buffer.writeLong(value)
    }

    @ExperimentalSerializationApi
    override fun encodeNull() {
        buffer.writeByte(0)
    }

    @ExperimentalSerializationApi
    override fun encodeNotNullMark() {
        buffer.writeByte(1)
    }

    override fun encodeShort(value: Short) {
        buffer.writeShort(value.toInt())
    }

    override fun encodeString(value: String) {
        buffer.writeString(value)
    }

    override fun encodeCompound(compound: NbtCompound) {
        buffer.writeNbt(compound)
    }
}

@OptIn(ExperimentalSerializationApi::class)
class ByteBufDecoder(override val serializersModule: SerializersModule, private val buffer: PacketByteBuf):
    AbstractDecoder(), NbtCompoundDecoder {
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        val name = buffer.readString()
        val index = descriptor.getElementIndex(name)
        return index
    }

    override fun decodeCompound(): NbtCompound = buffer.readNbtCompound()!!
    override fun decodeBoolean(): Boolean = buffer.readBoolean()
    override fun decodeByte(): Byte = buffer.readByte()
    override fun decodeChar(): Char = buffer.readChar()
    override fun decodeDouble(): Double = buffer.readDouble()
    override fun decodeFloat(): Float = buffer.readFloat()
    override fun decodeInt(): Int = buffer.readInt()
    override fun decodeLong(): Long = buffer.readLong()
    override fun decodeString(): String = buffer.readString()
    override fun decodeNotNullMark(): Boolean = (buffer.readByte().toInt() == 1)
    override fun decodeNull(): Nothing? {
        buffer.readByte()
        return null
    }
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = buffer.readInt()
    override fun decodeShort(): Short = buffer.readShort()
    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = buffer.readInt()
}
