package ui.screens.edit

import Expense
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.oshai.kotlinlogging.KotlinLogging
import ui.theme.Spacing

private val logger = KotlinLogging.logger {}

data class EditExpenseScreen(val expense: Expense? = null) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text =
                            expense.let {
                                if (it == null) {
                                    "Create expense"
                                } else {
                                    "Edit ${it.name}"
                                }
                            },
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() },
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    },
                )
            },
            bottomBar = {
                Button(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 32.dp)
                        .height(48.dp),
                    onClick = {
                        logger.info { "Form submitted" }
                        navigator.pop()
                    },
                    shape = MaterialTheme.shapes.extraLarge,
                ) {
                    Text(
                        text = "Submit",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            },
        ) { innerPadding ->
            Surface(
                modifier =
                Modifier
                    .padding(innerPadding),
            ) {
                Form()
            }
        }
    }
}

@Composable
fun Form(
) {
    Column(
        modifier =
        Modifier
            .padding(horizontal = Spacing.ExtraLarge)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Spacing.Large),
    ) {
        Text("Emoji picker")

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.Small_100),
        ) {
            Text(
                text = "Name",
                style = MaterialTheme.typography.bodyLarge,
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Please type the expense name")
                },
                value = "",
                onValueChange = { logger.info { "Name field changed" } },
                singleLine = true,
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.Small_100),
        ) {
            Text(
                text = "Price",
                style = MaterialTheme.typography.bodyLarge,
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Please type the expense price")
                },
                value = "",
                onValueChange = { logger.info { "Price field changed" } },
                singleLine = true,
            )
        }
    }
}
