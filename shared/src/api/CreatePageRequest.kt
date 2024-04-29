package api

import Expense
import api.model.ExpensePageProperties
import api.model.IconProperty
import api.model.NumberProperty
import api.model.TitleProperty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import utils.Env

@Serializable
data class CreatePageRequest(
    val parent: ParentDatabase,
    val properties: ExpensePageProperties<TitleProperty.ForRequest>,
    val icon: IconProperty? = null,
) {
    companion object {
        fun from(expense: Expense): CreatePageRequest =
            CreatePageRequest(
                parent =
                ParentDatabase(
                    databaseId = Env.NOTION_DATABASE_ID,
                ),
                icon = expense.icon?.let { IconProperty(expense.icon) },
                properties =
                ExpensePageProperties(
                    expense = TitleProperty.ForRequest.from(expense.name),
                    amount = NumberProperty(number = expense.price),
                ),
            )
    }
}

@Serializable
data class ParentDatabase(
    @SerialName("database_id")
    val databaseId: DatabaseId,
)
