package api

import ExpenseId
import api.model.ExpensePageProperties
import api.model.IconProperty
import kotlinx.serialization.Serializable

@Serializable
data class ExpensePageResponse(
    val id: ExpenseId,
    val icon: IconProperty? = null,
    val properties: ExpensePageProperties,
)
