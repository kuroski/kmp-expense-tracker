import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.sqldelight)
}

buildkonfig {
    packageName = "org.expense.tracker"

    defaultConfigs {
        buildConfigField(STRING, "NOTION_TOKEN", env.fetchOrNull("NOTION_TOKEN") ?: "")
        buildConfigField(STRING, "NOTION_DATABASE_ID", env.fetchOrNull("NOTION_DATABASE_ID") ?: "")
    }
}

sqldelight {
    databases {
        create("MyDatabase") {
            srcDirs("sqldelight")
            packageName.set("org.expense.tracker.database")
        }
    }
}