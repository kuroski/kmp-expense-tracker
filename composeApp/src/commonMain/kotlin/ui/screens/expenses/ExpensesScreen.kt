package ui.screens.expenses

import Expense
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.compose.koinInject
import ui.screens.edit.EditExpenseScreen
import ui.theme.BorderRadius
import ui.theme.IconSize
import ui.theme.Spacing
import ui.theme.Width
import utils.RemoteData

private val logger = KotlinLogging.logger {}

object ExpensesScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val snackbarHostState = koinInject<SnackbarHostState>()
        val viewModel = getScreenModel<ExpensesScreenViewModel>()
        val state by viewModel.state.collectAsState()
        val onExpenseClicked: (Expense) -> Unit = { navigator.push(EditExpenseScreen(it)) }

        LaunchedEffect(state.data) {
            val remoteData = state.data
            if (remoteData is RemoteData.Failure) {
                snackbarHostState.showSnackbar(remoteData.error.message ?: "Something went wrong")
            }
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(
                            enabled = state.data !is RemoteData.Loading,
                            onClick = { viewModel.fetchExpenses(forceUpdate = true) },
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                        }
                    },
                    title = {
                        Text("My subscriptions", style = MaterialTheme.typography.titleMedium)
                    },
                )
            },
            bottomBar = {
                BottomAppBar(
                    contentPadding = PaddingValues(horizontal = Spacing.Large),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column {
                            Text(
                                "Average expenses",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                "Per month".uppercase(),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Text(
                            state.avgExpenses,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            },
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (val remoteData = state.data) {
                    is RemoteData.NotAsked, is RemoteData.Loading -> {
                        Column {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(Spacing.Small_100),
                                verticalArrangement = Arrangement.spacedBy(Spacing.Small_100),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.width(Width.Medium),
                                )
                            }
                            ExpenseList(
                                state.lastSuccessData,
                                onExpenseClicked,
                            )
                        }
                    }

                    is RemoteData.Failure -> {
                        if (state.lastSuccessData.isNotEmpty()) {
                            ExpenseList(
                                state.lastSuccessData,
                                onExpenseClicked,
                            )
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(
                                    Spacing.Small,
                                    alignment = Alignment.CenterVertically
                                ),
                            ) {
                                Text("Oops, something went wrong", style = MaterialTheme.typography.titleMedium)
                                Text("Try refreshing")
                                FilledIconButton(
                                    onClick = { viewModel.fetchExpenses() },
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null)
                                }
                            }
                        }
                    }

                    is RemoteData.Success -> {
                        ExpenseList(remoteData.data, onExpenseClicked)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpenseList(
    expenses: List<Expense>,
    onClick: (expense: Expense) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Spacing.Small_100),
    ) {
        items(
            items = expenses,
            key = { it.id },
        ) { expense ->
            ExpenseListItem(
                expense = expense,
                onClick = {
                    logger.info { "Clicked on ${expense.name}" }
                    onClick(expense)
                },
            )
        }

        item {
            Spacer(Modifier.height(Spacing.Medium))
        }
    }
}

@Composable
private fun ExpenseListItem(
    expense: Expense,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.Medium)
            .defaultMinSize(minHeight = 56.dp),
        onClick = onClick,
        shape = RoundedCornerShape(BorderRadius.small),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Row(
            modifier =
            Modifier
                .padding(
                    horizontal = Spacing.Medium_100,
                    vertical = Spacing.Small_100,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.Large),
        ) {
            Text(
                text = expense.icon ?: "",
                fontSize = IconSize.Medium,
                modifier = Modifier.defaultMinSize(minWidth = 24.dp),
            )
            Text(
                text = expense.name,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier.weight(1f),
            )
            Text(
                text = (expense.formattedPrice),
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            )
        }
    }
}
