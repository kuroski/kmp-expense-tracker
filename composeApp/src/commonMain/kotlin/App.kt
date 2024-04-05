import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Scaffold {
                Navigator(HelloWorldScreen) { navigator ->
                    SlideTransition(navigator)
                }
            }
        }
    }
}

object HelloWorldScreen : Screen {

    @Composable
    override fun Content() {
        Column {
            Text("Hello World!")
            Button(onClick = {}) {
                Text("Click here!")
            }
        }
    }
}