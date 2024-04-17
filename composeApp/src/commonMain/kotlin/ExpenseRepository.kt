import api.APIClient
import api.toDomain
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.utils.io.core.*
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent

private val logger = KotlinLogging.logger {}

class ExpenseRepository(
    private val databaseId: String,
    private val apiClient: APIClient,
    private val expenseStorage: ExpenseStorage,
) : KoinComponent, Closeable {
    suspend fun all(forceUpdate: Boolean = false): StateFlow<List<Expense>> {
        val expenses = expenseStorage.getExpenses()

        if (forceUpdate || expenses.value.isEmpty()) {
            logger.debug { "Refreshing expenses" }
            val response = apiClient.queryDatabaseOrThrow(databaseId)
            expenseStorage.saveExpenses(response.toDomain())
        }

        logger.debug { "Loading all expenses" }
        return expenses
    }

    override fun close() {
        apiClient.close()
    }
}
