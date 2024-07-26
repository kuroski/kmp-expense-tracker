package ui.screens.edit

import Expense
import ExpenseId
import ExpenseRepository
import cafe.adriel.voyager.core.model.StateScreenModel
import io.github.oshai.kotlinlogging.KotlinLogging
import utils.form.FieldState
import utils.form.FormStatus
import utils.form.getOrNull

private val logger = KotlinLogging.logger {}

data class FormData(
    val name: FieldState = FieldState.Empty,
    val icon: FieldState = FieldState.Empty,
    val price: FieldState = FieldState.Empty,
) {
    val isValid get() = listOf(name, icon, price).all { it is FieldState.Valid }

    val intPrice
        get() =
            when (price) {
                is FieldState.Valid -> price.text.toIntOrNull() ?: 0
                else -> 0
            }

    fun validateName(value: String = "") =
        copy(
            name =
            when {
                value.isBlank() -> FieldState.Invalid("This field is required")
                else -> FieldState.Valid(value)
            },
        )

    fun validatePrice(value: String = "") =
        copy(
            price =
            when {
                (value.toIntOrNull() ?: 0) < 0 -> FieldState.Invalid("Please provide a positive number")
                else -> FieldState.Valid(value)
            },
        )

    fun validateIcon(value: String = "") =
        copy(
            icon = FieldState.Valid(value),
        )

    fun validate() =
        FormData(
            name = validateName(name.getOrNull() ?: "").name,
            price = validatePrice(price.getOrNull() ?: "").price,
            icon = validateIcon(icon.getOrNull() ?: "").icon,
        )
}

data class EditExpenseScreenState(
    val expense: Expense?,
    val formData: FormData,
    val formStatus: FormStatus,
)

class EditExpenseViewModel(
    private val expense: Expense?,
    private val expenseRepository: ExpenseRepository,
) : StateScreenModel<EditExpenseScreenState>(
    EditExpenseScreenState(
        expense = expense,
        formStatus = FormStatus.Initial,
        formData =
        FormData(
            name = FieldState.Valid(expense?.name ?: ""),
            icon = FieldState.Valid(expense?.icon ?: ""),
            price = FieldState.Valid((expense?.price ?: "").toString()),
        ),
    ),
) {
    suspend fun archive() {
        expense?.let { expenseRepository.archiveOrThrow(it) }
    }

    fun onFieldChange(fieldValidator: FormData.(String) -> FormData): (String) -> Unit {
        return { newValue: String ->
            val newFormData = fieldValidator(state.value.formData, newValue)
            mutableState.value = state.value.copy(formData = newFormData)
        }
    }

    suspend fun submit() {
        val formData = state.value.formData.validate()
        mutableState.value =
            state.value.copy(
                formData = formData,
                formStatus = FormStatus.Submitting,
            )

        if (!formData.isValid) {
            mutableState.value = state.value.copy(formStatus = FormStatus.Submitted)
            throw Exception("The form is invalid")
        }

        val icon = (formData.icon as FieldState.Valid).text

        if (expense != null) {
            expenseRepository.updateOrThrow(
                expense.copy(
                    name = (formData.name as FieldState.Valid).text,
                    price = formData.intPrice,
                    icon = icon.ifEmpty { null },
                ),
            )
        } else {
            expenseRepository.createOrThrow(
                Expense(
                    id = ExpenseId(""),
                    name = (formData.name as FieldState.Valid).text,
                    price = formData.intPrice,
                    icon = icon.ifEmpty { null },
                ),
            )
        }

        mutableState.value = state.value.copy(formStatus = FormStatus.Submitted)
    }
}
