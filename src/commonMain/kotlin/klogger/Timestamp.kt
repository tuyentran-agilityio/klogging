package klogger

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure

@Serializable(with = TimestampSerialiser::class)
data class Timestamp(val epochSeconds: Long, val nanos: Int) {
    override fun toString(): String {
        val ns = "000000000$nanos"
        return "$epochSeconds.${ns.substring(ns.length - 9)}"
    }
}

object TimestampSerialiser : KSerializer<Timestamp> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Timestamp) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Timestamp {
        val (epochSeconds, nanos) = decoder.decodeString().split(".")
        return Timestamp(epochSeconds.toLong(), nanos.toInt())
    }
}

