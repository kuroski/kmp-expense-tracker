package api

import kotlinx.serialization.Serializable

@Serializable
data class ArchivePageRequest(
    val archived: Boolean,
)
