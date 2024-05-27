package api

import Expense
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueryDatabaseResponse(
    val results: List<ExpensePageResponse>,
    @SerialName("next_cursor")
    val nextCursor: String? = null,
    @SerialName("has_more")
    val hasMore: Boolean,
)

fun QueryDatabaseResponse.toDomain(): List<Expense> =
    results.map { it.toDomain() }