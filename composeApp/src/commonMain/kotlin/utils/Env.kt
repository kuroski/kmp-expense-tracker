package utils

import org.expense.tracker.BuildKonfig

object Env {
    val NOTION_TOKEN: String
        get() {
            val notionToken = BuildKonfig.NOTION_TOKEN
            require(notionToken.isNotBlank()) { "You must provide a NOTION_TOKEN env variable" }
            return notionToken
        }

    val NOTION_DATABASE_ID: String
        get() {
            val notionDatabaseId = BuildKonfig.NOTION_DATABASE_ID
            require(notionDatabaseId.isNotBlank()) { "You must provide a NOTION_DATABASE_ID env variable" }
            return notionDatabaseId
        }
}
