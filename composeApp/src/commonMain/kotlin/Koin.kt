import androidx.compose.material3.SnackbarHostState
import api.APIClient
import org.koin.dsl.module
import ui.screens.expenses.ExpensesScreenViewModel
import utils.Env

object Koin {
    val appModule =
        module {
            single<SnackbarHostState> { SnackbarHostState() }
            single<APIClient> { APIClient(Env.NOTION_TOKEN) }

            factory { ExpensesScreenViewModel(apiClient = get()) }
        }
}
