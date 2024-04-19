package ui.screens.edit

import Expense
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import ui.components.EmojiPicker
import ui.components.InputField
import ui.theme.Spacing
import utils.form.CurrencyAmountInputVisualTransformation
import utils.form.FieldState
import utils.form.FormStatus

private val logger = KotlinLogging.logger {}

data class EditExpenseScreen(val expense: Expense? = null) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<EditExpenseViewModel> { parametersOf(expense) }
        val state by viewModel.state.collectAsState()
        val coroutineScope = rememberCoroutineScope()

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
                        coroutineScope.launch {
                            try {
                                viewModel.submit()
                                navigator.pop()
                            } catch (e: Throwable) {
                                logger.error { e.message ?: "Something went wrong" }
                            }
                        }
                    },
                    shape = MaterialTheme.shapes.extraLarge,
                ) {
                    Text(
                        text = "Submit", style = MaterialTheme.typography.bodyLarge,
                    )
                }
            },
        ) { innerPadding ->
            Surface(
                modifier =
                Modifier
                    .padding(innerPadding),
            ) {
                Form(state = state, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun Form(
    state: EditExpenseScreenState,
    viewModel: EditExpenseViewModel,
) {
    Column(
        modifier =
        Modifier
            .padding(horizontal = Spacing.ExtraLarge)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Spacing.Large),
    ) {
        EmojiPicker(
            value = (state.formData.icon as? FieldState.Valid)?.text ?: "",
            onSelect = viewModel.onFieldChange(FormData::validateIcon),
        )

        InputField(
            label = "Name",
            placeholder = "Please type the expense name",
            value = (state.formData.name as? FieldState.Valid)?.text,
            onValueChange = viewModel.onFieldChange(FormData::validateName),
            error =
            if (state.formStatus !is FormStatus.Submitted) {
                null
            } else {
                (state.formData.name as? FieldState.Invalid)?.error
            },
        )

        InputField(
            keyboardType = KeyboardType.NumberPassword,
            visualTransformation = CurrencyAmountInputVisualTransformation(),
            label = "Price",
            placeholder = "Please type the expense price",
            value = (state.formData.price as? FieldState.Valid)?.text,
            onValueChange = viewModel.onFieldChange(FormData::validatePrice),
            error =
            if (state.formStatus !is FormStatus.Submitted) {
                null
            } else {
                (state.formData.price as? FieldState.Invalid)?.error
            },
        )
    }
}
