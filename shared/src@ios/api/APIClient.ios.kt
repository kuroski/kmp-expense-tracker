package api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual fun clientEngine(): HttpClientEngine = Darwin.create()
