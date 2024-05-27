package utils

import io.github.cdimascio.dotenv.dotenv

val dotenv = dotenv()

actual fun getEnv(key: String): String? = System.getenv(key) ?: dotenv[key]
