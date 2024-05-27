package ui.screens.expenses

import Expense
import api.APIClient
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.launch
import utils.Env

private val logger = KotlinLogging.logger {}

data class ExpensesScreenState(
    val data: List<Expense>,
) {
    val avgExpenses: String
        get() = data.map { it.price }.average().toString()
}

class ExpensesScreenViewModel(apiClient: APIClient) : StateScreenModel<ExpensesScreenState>(
    ExpensesScreenState(
        data = listOf(),
    ),
) {
    init {
        screenModelScope.launch {
            logger.info { "Fetching expenses" }
            val database = apiClient.queryDatabaseOrThrow(Env.NOTION_DATABASE_ID)
            val expenses = database.results.map {
                Expense(
                    id = it.id,
                    name = it.properties.expense.title.firstOrNull()?.plainText ?: "-",
                    icon = it.icon?.emoji,
                    price = it.properties.amount.number,
                )
            }
            mutableState.value = ExpensesScreenState(
                data = expenses
            )
        }
    }
}
