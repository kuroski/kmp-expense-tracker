import java.text.NumberFormat
import java.util.Currency

actual fun formatPrice(amount: Int): String =
    (
        NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance("EUR")
        }
    ).format(amount.toFloat() / 100)
