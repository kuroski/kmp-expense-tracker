package ui.screens.expenses

import Expense
import api.APIClient
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import formatPrice
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.launch
import utils.Env
import utils.RemoteData
import utils.getOrElse

private val logger = KotlinLogging.logger {}

data class ExpensesScreenState(
    val lastSuccessData: List<Expense> = emptyList(),
    val data: RemoteData<Throwable, List<Expense>>,
) {
    val avgExpenses: String
        get() = formatPrice(lastSuccessData.map { it.price }.average().toInt())
}

class ExpensesScreenViewModel(private val apiClient: APIClient) : StateScreenModel<ExpensesScreenState>(
    ExpensesScreenState(
        data = RemoteData.NotAsked,
    ),
) {
    init {
        fetchExpenses()
    }

    fun fetchExpenses() {
        mutableState.value = mutableState.value.copy(data = RemoteData.Loading)

        screenModelScope.launch {
            try {
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
                mutableState.value =
                    ExpensesScreenState(
                        lastSuccessData = expenses,
                        data = RemoteData.success(expenses),
                    )
            } catch (cause: Throwable) {
                logger.error { "Cause ${cause.message}" }
                cause.printStackTrace()
                mutableState.value = mutableState.value.copy(data = RemoteData.failure(cause))
            }
        }
    }
}
