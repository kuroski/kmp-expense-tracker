package utils

import platform.Foundation.NSProcessInfo

actual fun getEnv(key: String): String? {
    return NSProcessInfo.processInfo.environment[key]?.toString()
}
