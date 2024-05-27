package ui.screens.expenses

import Expense
import ExpenseRepository
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import formatPrice
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.launch
import utils.RemoteData

private val logger = KotlinLogging.logger {}

data class ExpensesScreenState(
    val lastSuccessData: List<Expense> = emptyList(),
    val data: RemoteData<Throwable, List<Expense>>,
) {
    val avgExpenses: String
        get() = formatPrice(lastSuccessData.map { it.price }.average().toInt())
}

class ExpensesScreenViewModel(private val expenseRepository: ExpenseRepository) : StateScreenModel<ExpensesScreenState>(
    ExpensesScreenState(
        data = RemoteData.NotAsked,
    ),
) {
    init {
        fetchExpenses()
    }

    fun fetchExpenses(forceUpdate: Boolean = false) {
        mutableState.value = mutableState.value.copy(data = RemoteData.Loading)

        screenModelScope.launch {
            try {
                expenseRepository.all(forceUpdate).collect { expenses ->
                    logger.info { "Expenses list was updated" }
                    mutableState.value =
                        ExpensesScreenState(
                            lastSuccessData = expenses,
                            data = RemoteData.success(expenses),
                        )
                }
            } catch (cause: Throwable) {
                logger.error { "Cause ${cause.message}" }
                cause.printStackTrace()
                mutableState.value = mutableState.value.copy(data = RemoteData.failure(cause))
            }
        }
    }
}
