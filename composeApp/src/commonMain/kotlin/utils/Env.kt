package utils

import api.DatabaseId
import org.expense.tracker.BuildKonfig
import kotlin.jvm.JvmInline

object Env {
    val NOTION_TOKEN: String
        get() {
            val notionToken = BuildKonfig.NOTION_TOKEN
            require(notionToken.isNotBlank()) { "You must provide a NOTION_TOKEN env variable" }
            return notionToken
        }

    val NOTION_DATABASE_ID: DatabaseId
        get() {
            val notionDatabaseId = BuildKonfig.NOTION_DATABASE_ID
            require(notionDatabaseId.isNotBlank()) { "You must provide a NOTION_DATABASE_ID env variable" }
            return DatabaseId(notionDatabaseId)
        }
}
