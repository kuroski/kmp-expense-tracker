package api

import Expense
import ExpenseId
import api.model.ExpensePageProperties
import api.model.IconProperty
import api.model.TitleProperty
import kotlinx.serialization.Serializable

@Serializable
data class ExpensePageResponse(
    val id: ExpenseId,
    val icon: IconProperty? = null,
    val properties: ExpensePageProperties<TitleProperty.ForResponse>,
)

fun ExpensePageResponse.toDomain(): Expense = Expense(
    id = id,
    name = properties.expense.title.firstOrNull()?.plainText ?: "-",
    icon = icon?.emoji,
    price = properties.amount.number,
)