package api

import Expense
import api.model.ExpensePageProperties
import api.model.IconProperty
import api.model.NumberProperty
import api.model.TitleProperty
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePageRequest(
    val properties: ExpensePageProperties<TitleProperty.ForRequest>,
    val icon: IconProperty? = null,
) {
    companion object {
        fun from(expense: Expense): UpdatePageRequest =
            UpdatePageRequest(
                icon = expense.icon?.let { IconProperty(expense.icon) },
                properties =
                ExpensePageProperties(
                    expense = TitleProperty.ForRequest.from(expense.name),
                    amount = NumberProperty(number = expense.price),
                ),
            )
    }
}
