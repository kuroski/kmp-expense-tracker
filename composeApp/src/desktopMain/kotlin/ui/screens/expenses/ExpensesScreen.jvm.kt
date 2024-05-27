package ui.screens.expenses

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.runtime.Composable

@Composable
actual fun ExpenseListItemContainer(
    onDeleteClicked: () -> Unit,
    children: @Composable () -> Unit,
) {
    ContextMenuArea(
        items = {
            listOf(
                ContextMenuItem("Delete", onDeleteClicked),
            )
        },
    ) {
        children()
    }
}
