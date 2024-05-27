package utils.form

sealed class FieldState {
    data object Empty : FieldState()

    data class Valid(val text: String) : FieldState()

    data class Invalid(val error: String) : FieldState()
}

fun FieldState.getOrNull(): String? =
    when (this) {
        is FieldState.Valid -> text
        else -> null
    }
