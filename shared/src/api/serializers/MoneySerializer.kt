package api.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object MoneySerializer : KSerializer<Int> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Money", PrimitiveKind.INT)

    override fun serialize(
        encoder: Encoder,
        value: Int,
    ) = encoder.encodeFloat(value.toFloat() / 100)

    override fun deserialize(decoder: Decoder): Int = (decoder.decodeFloat() * 100).toInt()
}
