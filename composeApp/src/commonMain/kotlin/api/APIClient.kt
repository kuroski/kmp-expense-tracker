package api

import ExpenseId
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.json.Json

typealias DatabaseId = String

expect fun clientEngine(): HttpClientEngine

class APIClient(
    private val token: String,
) : Closeable {
    private val httpClient: HttpClient by lazy {
        HttpClient(clientEngine()) {
            defaultRequest {
                header(NOTION_HEADER, NOTION_HEADER_VERSION)
                contentType(ContentType.Application.Json)
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                    },
                )
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(accessToken = token, refreshToken = token)
                    }
                }
            }
        }
    }

    init {
        require(token.isNotEmpty()) { "Notion API token is required" }
    }

    companion object {
        const val NOTION_HEADER: String = "Notion-Version"
        const val NOTION_HEADER_VERSION: String = "2021-08-16"
        const val API_BASE_URL: String = "https://api.notion.com/v1"
    }

    suspend fun queryDatabaseOrThrow(
        databaseId: DatabaseId,
        query: QueryDatabaseRequest = QueryDatabaseRequest(),
    ): QueryDatabaseResponse =
        httpClient
            .post("$API_BASE_URL/databases/$databaseId/query") {
                setBody(query)
            }
            .body<QueryDatabaseResponse>()

    suspend fun createPageOrThrow(body: CreatePageRequest) =
        httpClient
            .post("$API_BASE_URL/pages") {
                setBody(body)
            }
            .body<ExpensePageResponse>()

    suspend fun updatePage(
        id: ExpenseId,
        body: UpdatePageRequest,
    ) = httpClient
        .patch("$API_BASE_URL/pages/$id") {
            setBody(body)
        }
        .body<ExpensePageResponse>()

    override fun close() = httpClient.close()
}
