import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import ui.screens.expenses.ExpensesScreen
import ui.theme.AppTheme

@Composable
fun App() {
    KoinApplication(
        application = {
            modules(Koin.appModule)
        },
    ) {
        AppTheme {
            val snackbarHostState = koinInject<SnackbarHostState>()

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    },
                ) {
                    Navigator(ExpensesScreen) { navigator ->
                        SlideTransition(navigator)
                    }
                }
            }
        }
    }
}