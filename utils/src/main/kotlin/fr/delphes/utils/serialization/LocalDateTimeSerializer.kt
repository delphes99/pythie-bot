package fr.delphes.utils.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME

object LocalDateTimeSerializer : AbsLocalDateTimeSerializer(ISO_LOCAL_DATE_TIME)
object LocalDateTimeAsInstantSerializer : AbsLocalDateTimeSerializer(ISO_ZONED_DATE_TIME)

sealed class AbsLocalDateTimeSerializer(
    private val formatter: DateTimeFormatter
) : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val string = decoder.decodeString()
        return LocalDateTime.parse(string, formatter)
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val format = value.format(formatter)
        encoder.encodeString(format)
    }
}