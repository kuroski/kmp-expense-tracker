import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle

actual fun formatPrice(amount: Int): String {
    val formatter = NSNumberFormatter()
    formatter.minimumFractionDigits = 2u
    formatter.maximumFractionDigits = 2u
    formatter.numberStyle = NSNumberFormatterCurrencyStyle
    formatter.currencyCode = "EUR"
    return formatter.stringFromNumber(NSNumber(amount.toFloat() / 100))!!
}
