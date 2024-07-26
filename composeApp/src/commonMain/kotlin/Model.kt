import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

expect fun formatPrice(amount: Int): String

@Serializable
@JvmInline
value class ExpenseId(private val value: String) {
    override fun toString(): String {
        return value
    }
}

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
