package utils.form

sealed class FormStatus {
    data object Initial : FormStatus()
    data object Submitting : FormStatus()
    data object Submitted : FormStatus()
}

