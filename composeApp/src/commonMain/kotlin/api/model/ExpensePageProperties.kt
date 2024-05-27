package api.model

import api.serializers.MoneySerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpensePageProperties<T : TitleProperty>(
    @SerialName("Expense")
    val expense: T,
    @SerialName("Amount")
    val amount: NumberProperty,
)

@Serializable
sealed class TitleProperty {
    @Serializable
    data class ForResponse(val id: String, val title: List<Value>) : TitleProperty() {
        @Serializable
        data class Value(
            @SerialName("plain_text")
            val plainText: String,
        )
    }

    @Serializable
    data class ForRequest(
        val title: List<Value>,
    ) : TitleProperty() {
        companion object {
            fun from(text: String): ForRequest {
                val textProperty = TextProperty(text)
                val title = listOf(Value(textProperty))
                return ForRequest(title)
            }
        }

        @Serializable
        data class Value(
            val text: TextProperty,
        )

        @Serializable
        data class TextProperty(val content: String)
    }
}

@Serializable
data class NumberProperty(
    @Serializable(with = MoneySerializer::class)
    val number: Int,
)


