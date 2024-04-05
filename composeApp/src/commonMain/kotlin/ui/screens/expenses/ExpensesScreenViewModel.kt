package ui.screens.expenses

import Expense
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val logger = KotlinLogging.logger {}

data class ExpensesScreenState(
    val data: List<Expense>,
) {
    val avgExpenses: String
        get() = data.map { it.price }.average().toString()
}

class ExpensesScreenViewModel : StateScreenModel<ExpensesScreenState>(
    ExpensesScreenState(
        data = listOf(),
    ),
) {
    init {
        screenModelScope.launch {
            logger.info { "Fetching expenses" }
            delay(3000)
            mutableState.value = ExpensesScreenState(
                data = listOf(
                    Expense(
                        id = "1",
                        name = "Rent",
                        icon = "🏠",
                        price = 102573,
                    ),
                    Expense(
                        id = "2",
                        name = "Apple one",
                        icon = "🍎",
                        price = 2595,
                    ),
                    Expense(
                        id = "3",
                        name = "Netflix",
                        icon = "📺",
                        price = 1299,
                    ),
                )
            )
        }
    }
}
