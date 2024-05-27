
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.*

private val logger = KotlinLogging.logger {}

interface ExpenseStorage {
    suspend fun saveExpenses(newExpenses: List<Expense>)

    suspend fun createExpense(newExpense: Expense)

    suspend fun updateExpense(newExpense: Expense)

    suspend fun removeExpense(expense: Expense)

    fun findExpense(id: ExpenseId): Flow<Expense?>

    fun getExpenses(): StateFlow<List<Expense>>
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

    override fun findExpense(id: ExpenseId): Flow<Expense?> =
        storedExpenses.map { expenses ->
            expenses.find { it.id == id }
        }

    override fun getExpenses(): StateFlow<List<Expense>> = storedExpenses.asStateFlow()
}
