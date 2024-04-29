import kotlinx.serialization.Serializable

expect fun formatPrice(amount: Int): String

typealias ExpenseId = String

@Serializable
data class Expense(
    val id: ExpenseId,
    val name: String,
    val icon: String?,
    val price: Int,
) {
    val formattedPrice: String
        get() = formatPrice(price)
}
