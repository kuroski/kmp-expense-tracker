import kotlinx.serialization.Serializable

typealias ExpenseId = String

@Serializable
data class Expense(
    val id: ExpenseId,
    val name: String,
    val icon: String?,
    val price: Int,
)
