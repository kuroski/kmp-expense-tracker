package api.model

import kotlinx.serialization.Serializable

@Serializable
data class IconProperty(
    val emoji: String? = null,
    val type: String? = "emoji",
)
