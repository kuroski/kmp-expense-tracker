package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import ui.theme.Spacing
import utils.EMOJI_LIST

@Composable
fun EmojiPicker(
    value: String = "",
    onSelect: (String) -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { showBottomSheet = true }) {
            Text(
                text = value.ifBlank { "Select a icon" },
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
            ) {
                LazyVerticalGrid(
                    contentPadding = PaddingValues(horizontal = Spacing.Medium),
                    columns = GridCells.Fixed(15),
                ) {
                    items(EMOJI_LIST) { emoji ->
                        TextButton(
                            onClick = {
                                onSelect(emoji)
                                showBottomSheet = false
                            },
                        ) {
                            Text(emoji)
                        }
                    }
                }
            }
        }
    }
}
