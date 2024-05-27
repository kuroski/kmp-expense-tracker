package utils.form

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CurrencyAmountInputVisualTransformation(
    private val numberOfDecimals: Int = 2,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val thousandsSeparator = '.'
        val decimalSeparator = ','
        val zero = '0'

        val inputText = text.text

        val intPart =
            inputText
                .dropLast(numberOfDecimals)
                .reversed()
                .chunked(3)
                .joinToString(thousandsSeparator.toString())
                .reversed()
                .ifEmpty {
                    zero.toString()
                }

        val fractionPart: String =
            inputText
                .takeLast(numberOfDecimals)
                .padStart(numberOfDecimals, zero)

        val formattedNumber = "$intPart$decimalSeparator$fractionPart"

        return TransformedText(
            AnnotatedString(
                text = formattedNumber,
                spanStyles = text.spanStyles,
                paragraphStyles = text.paragraphStyles,
            ),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int = formattedNumber.length

                override fun transformedToOriginal(offset: Int): Int = inputText.length
            },
        )
    }
}
