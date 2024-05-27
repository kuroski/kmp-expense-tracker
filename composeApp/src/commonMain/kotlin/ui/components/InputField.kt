package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import ui.theme.Spacing

@Composable
fun InputField(
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    label: String,
    placeholder: String = "",
    value: String?,
    onValueChange: (String) -> Unit,
    error: String?,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Small_100),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
        )
        TextField(
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = placeholder)
            },
            value = value ?: "",
            onValueChange = onValueChange,
            isError = error != null,
            singleLine = true,
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
