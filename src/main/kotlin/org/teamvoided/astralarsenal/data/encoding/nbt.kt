@file:OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)

package org.teamvoided.astralarsenal.data.encoding

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.internal.NamedValueDecoder
import kotlinx.serialization.internal.NamedValueEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.nbt.NbtCompound
import kotlin.properties.Delegates

open class NbtFormat(override val serializersModule: SerializersModule): SerialFormat {
    companion object Default: NbtFormat(EmptySerializersModule())

    fun <T> encodeToNbtCompound(serializer: SerializationStrategy<T>, value: T): NbtCompound {
        val compound = NbtCompound()
        encodeIntoNbtCompound(serializer, value, compound)
        return compound
    }

    fun <T> encodeIntoNbtCompound(serializer: SerializationStrategy<T>, value: T, compound: NbtCompound) {
        val encoder = NbtEncoder(serializersModule, serializer.descriptor, compound)
        serializer.serialize(encoder, value)
    }

    fun <T> decodeFromNbtCompound(deserializer: DeserializationStrategy<T>, compound: NbtCompound): T {
        val decoder = NbtDecoder(serializersModule, deserializer.descriptor, compound)
        return deserializer.deserialize(decoder)
    }
}

inline fun <reified T> NbtFormat.encodeToNbtCompound(value: T): NbtCompound {
    return encodeToNbtCompound(serializersModule.serializer(), value)
}

inline fun <reified T> NbtFormat.encodeIntoNbtCompound(value: T, compound: NbtCompound) {
    encodeIntoNbtCompound(serializersModule.serializer(), value, compound)
}

inline fun <reified T> NbtFormat.decodeFromNbtCompound(compound: NbtCompound): T {
    return decodeFromNbtCompound(serializersModule.serializer(), compound)
}

fun SerialDescriptor.pushTags(pushTag: (String) -> Unit) {
    if (kind is PrimitiveKind) {
        pushTag(serialName)
        return
    }

    when (kind) {
        StructureKind.MAP, StructureKind.CLASS -> {
            for (i in 0..<elementsCount) {
                pushTag(getElementName(i))
            }
        }

        else -> {}
    }
}

class NbtListEncoder(
    override val serializersModule: SerializersModule,
    private val parent: NbtCompoundEncoder
): AbstractEncoder(), NbtCompoundEncoder {
    private val compound = NbtCompound()
    private var currentIndex by Delegates.notNull<Int>()

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        return when (descriptor.kind) {
            StructureKind.LIST -> NbtListEncoder(serializersModule, this)
            StructureKind.MAP, StructureKind.CLASS -> NbtEncoder(serializersModule, descriptor, NbtCompound(), this)
            else -> this
        }
    }

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        currentIndex = index
        return super.encodeElement(descriptor, index)
    }

    override fun encodeString(value: String) = compound.putString(currentIndex.toString(), value)
    override fun encodeChar(value: Char) = encodeString(value.toString())
    override fun encodeBoolean(value: Boolean) = compound.putBoolean(currentIndex.toString(), value)
    override fun encodeByte(value: Byte) = compound.putByte(currentIndex.toString(), value)
    override fun encodeShort(value: Short) = compound.putShort(currentIndex.toString(), value)
    override fun encodeInt(value: Int) = compound.putInt(currentIndex.toString(), value)
    override fun encodeLong(value: Long) = compound.putLong(currentIndex.toString(), value)
    override fun encodeFloat(value: Float) = compound.putFloat(currentIndex.toString(), value)
    override fun encodeDouble(value: Double) = compound.putDouble(currentIndex.toString(), value)
    override fun encodeNull() = compound.putString(currentIndex.toString(), "__null")
    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        val name = enumDescriptor.getElementName(index)
        compound.putString(currentIndex.toString(), name)
    }

    override fun encodeCompound(compound: NbtCompound) {
        this.compound.put(currentIndex.toString(), compound)
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        parent.encodeCompound(compound)
    }
}

class NbtListDecoder(override val serializersModule: SerializersModule, private val compound: NbtCompound): AbstractDecoder(),
    NbtCompoundDecoder {
    private val keys = compound.keys.iterator()
    private lateinit var currentKey: String

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        val key = keys.next()
        currentKey = key
        return descriptor.getElementIndex(key)
    }

    override fun decodeString(): String = compound.getString(currentKey)
    override fun decodeChar(): Char = decodeString()[0]
    override fun decodeBoolean(): Boolean = compound.getBoolean(currentKey)
    override fun decodeByte(): Byte = compound.getByte(currentKey)
    override fun decodeShort(): Short = compound.getShort(currentKey)
    override fun decodeInt(): Int = compound.getInt(currentKey)
    override fun decodeLong(): Long = compound.getLong(currentKey)
    override fun decodeFloat(): Float = compound.getFloat(currentKey)
    override fun decodeDouble(): Double = compound.getDouble(currentKey)
    override fun decodeNull(): Nothing? = null
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        val name = decodeString()
        return enumDescriptor.getElementIndex(name)
    }

    override fun decodeCompound(): NbtCompound = compound.getCompound(currentKey)
}

class NbtEncoder(
    override val serializersModule: SerializersModule,
    descriptor: SerialDescriptor,
    private val compound: NbtCompound,
    private val parent: NbtCompoundEncoder? = null
): NamedValueEncoder(), NbtCompoundEncoder {
    init {
        descriptor.pushTags { pushTag(it) }
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        return when (descriptor.kind) {
            StructureKind.LIST -> NbtListEncoder(serializersModule, this)
            StructureKind.MAP, StructureKind.CLASS -> NbtEncoder(serializersModule, descriptor, NbtCompound(), this)
            else -> this
        }
    }

    override fun endEncode(descriptor: SerialDescriptor) {
        parent?.encodeCompound(compound)
    }

    override fun encodeTaggedString(tag: String, value: String) = compound.putString(tag, value)
    override fun encodeTaggedBoolean(tag: String, value: Boolean) = compound.putBoolean(tag, value)
    override fun encodeTaggedChar(tag: String, value: Char) = encodeTaggedString(tag, value.toString())
    override fun encodeTaggedByte(tag: String, value: Byte) = compound.putByte(tag, value)
    override fun encodeTaggedShort(tag: String, value: Short) = compound.putShort(tag, value)
    override fun encodeTaggedInt(tag: String, value: Int) = compound.putInt(tag, value)
    override fun encodeTaggedLong(tag: String, value: Long) = compound.putLong(tag, value)
    override fun encodeTaggedFloat(tag: String, value: Float) = compound.putFloat(tag, value)
    override fun encodeTaggedDouble(tag: String, value: Double) = compound.putDouble(tag, value)
    override fun encodeTaggedNull(tag: String) {
        compound.putString(tag, "__null")
    }

    override fun encodeTaggedEnum(tag: String, enumDescriptor: SerialDescriptor, ordinal: Int) {
        val enumName = enumDescriptor.getElementName(ordinal)
        compound.putString(tag, enumName)
    }

    override fun encodeCompound(compound: NbtCompound) {
        val tag = popTag()
        this.compound.put(tag, compound)
    }
}

class NbtDecoder(
    override val serializersModule: SerializersModule,
    descriptor: SerialDescriptor,
    private val compound: NbtCompound
): NamedValueDecoder(), NbtCompoundDecoder {
    private val keys = compound.keys.iterator()

    init {
        descriptor.pushTags { pushTag(it) }
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return when (descriptor.kind) {
            StructureKind.LIST -> NbtListDecoder(serializersModule, decodeCompound())
            StructureKind.MAP, StructureKind.CLASS -> NbtDecoder(serializersModule, descriptor, decodeCompound())
            else -> this
        }
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (!keys.hasNext()) return CompositeDecoder.DECODE_DONE
        val key = keys.next()
        val idx = descriptor.getElementIndex(key)
        return idx
    }

    override fun decodeTaggedString(tag: String): String = compound.getString(tag)
    override fun decodeTaggedChar(tag: String): Char = decodeTaggedString(tag)[0]
    override fun decodeTaggedBoolean(tag: String): Boolean = compound.getBoolean(tag)
    override fun decodeTaggedByte(tag: String): Byte = compound.getByte(tag)
    override fun decodeTaggedShort(tag: String): Short = compound.getShort(tag)
    override fun decodeTaggedInt(tag: String): Int = compound.getInt(tag)
    override fun decodeTaggedLong(tag: String): Long = compound.getLong(tag)
    override fun decodeTaggedFloat(tag: String): Float = compound.getFloat(tag)
    override fun decodeTaggedDouble(tag: String): Double = compound.getDouble(tag)
    override fun decodeTaggedEnum(tag: String, enumDescriptor: SerialDescriptor): Int {
        val name = compound.getString(tag)
        return enumDescriptor.getElementIndex(name)
    }

    override fun decodeTaggedNull(tag: String): Nothing? {
        return null
    }

    override fun decodeCompound(): NbtCompound {
        val tag = popTag()
        return this.compound.getCompound(tag)
    }
}

interface NbtCompoundEncoder {
    fun encodeCompound(compound: NbtCompound)
}

interface NbtCompoundDecoder {
    fun decodeCompound(): NbtCompound
}
