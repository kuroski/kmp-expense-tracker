import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.expense.tracker.database.MyDatabase

private val logger = KotlinLogging.logger {}

interface ExpenseStorage {
    suspend fun saveExpenses(newExpenses: List<Expense>)

    suspend fun createExpense(newExpense: Expense)

    suspend fun updateExpense(newExpense: Expense)

    suspend fun removeExpense(expense: Expense)

    suspend fun getExpenses(): StateFlow<List<Expense>>
}

class InMemoryExpenseStorage : ExpenseStorage {
    private val storedExpenses = MutableStateFlow(emptyList<Expense>())

    override suspend fun saveExpenses(newExpenses: List<Expense>) {
        logger.debug { "Replacing expenses on storage" }
        storedExpenses.value = newExpenses
    }

    override suspend fun createExpense(newExpense: Expense) =
        storedExpenses.update { expenses: List<Expense> ->
            expenses + newExpense
        }

    override suspend fun updateExpense(newExpense: Expense) =
        storedExpenses.update { expenses: List<Expense> ->
            expenses.map {
                if (it.id == newExpense.id) {
                    newExpense
                } else {
                    it
                }
            }
        }

    override suspend fun removeExpense(expense: Expense) =
        storedExpenses.update { expenses: List<Expense> ->
            expenses.filter { it.id != expense.id }
        }

    override suspend fun getExpenses(): StateFlow<List<Expense>> = storedExpenses.asStateFlow()
}

class SQLiteExpenseStorage(private val database: MyDatabase) : ExpenseStorage {
    override suspend fun saveExpenses(newExpenses: List<Expense>) {
        database.expensesQueries.transaction {
            logger.debug { "Replacing expenses on storage" }
            database.expensesQueries.deleteAll()

            newExpenses.forEach { expense ->
                database.expensesQueries.upsert(
                    id = expense.id,
                    name = expense.name,
                    icon = expense.icon,
                    price = expense.price.toLong()
                )
            }
        }
    }

    override suspend fun createExpense(newExpense: Expense) {
        database.expensesQueries.upsert(
            id = newExpense.id,
            name = newExpense.name,
            icon = newExpense.icon,
            price = newExpense.price.toLong()
        )
    }

    override suspend fun updateExpense(newExpense: Expense) {
        database.expensesQueries.upsert(
            id = newExpense.id,
            name = newExpense.name,
            icon = newExpense.icon,
            price = newExpense.price.toLong()
        )
    }

    override suspend fun removeExpense(expense: Expense) {
        database.expensesQueries.delete(expense.id)
    }

    override suspend fun getExpenses(): StateFlow<List<Expense>> {
        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

        return database.expensesQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { databaseExpenses ->
                databaseExpenses.map { Expense(it.id, it.name, it.icon, it.price.toInt()) }
            }
            .stateIn(scope)
    }

}
