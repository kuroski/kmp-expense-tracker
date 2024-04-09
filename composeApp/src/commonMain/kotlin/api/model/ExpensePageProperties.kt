package api.model

import api.serializers.MoneySerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpensePageProperties(
    @SerialName("Expense")
    val expense: TitleProperty,
    @SerialName("Amount")
    val amount: NumberProperty,
)

@Serializable
class TitleProperty(val id: String, val title: List<Value>) {
    @Serializable
    data class Value(
        @SerialName("plain_text")
        val plainText: String,
    )
}

@Serializable
data class NumberProperty(
    @Serializable(with = MoneySerializer::class)
    val number: Int,
)
