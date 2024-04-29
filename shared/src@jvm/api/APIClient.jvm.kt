package api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

actual fun clientEngine(): HttpClientEngine = CIO.create()
