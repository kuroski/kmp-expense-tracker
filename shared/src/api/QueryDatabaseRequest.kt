package api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueryDatabaseRequest(
    @SerialName("start_cursor")
    val startCursor: String? = null,
    @SerialName("page_size")
    val pageSize: Int? = 100,
) {
    init {
        pageSize?.let {
            require(it in 1..100) { "Illegal property, pageSize must be between 1 and 100" }
        }
    }
}
