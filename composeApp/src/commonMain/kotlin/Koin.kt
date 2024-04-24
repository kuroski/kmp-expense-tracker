import androidx.compose.material3.SnackbarHostState
import api.APIClient
import org.expense.tracker.database.MyDatabase
import org.koin.dsl.module
import ui.screens.edit.EditExpenseViewModel
import ui.screens.expenses.ExpensesScreenViewModel
import utils.Env

expect fun createDb(): MyDatabase

object Koin {
    val appModule =
        module {
            single<SnackbarHostState> { SnackbarHostState() }
            single<APIClient> { APIClient(Env.NOTION_TOKEN) }
            single<ExpenseStorage> { InMemoryExpenseStorage() }
            single { ExpenseRepository(Env.NOTION_DATABASE_ID, get(), get(), get()) }
            single<MyDatabase> { createDb() }

            factory { ExpensesScreenViewModel(expenseRepository = get()) }
            factory { (expense: Expense?) -> EditExpenseViewModel(expense = expense, expenseRepository = get()) }
        }
}
