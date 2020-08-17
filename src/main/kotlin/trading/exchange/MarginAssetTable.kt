package trading.exchange

import trading.Currency
import java.math.BigDecimal

interface MarginAssetTable {
    fun get(currency: Currency): MarginBalance

    fun getAll(): Map<Currency, MarginBalance>
}

data class MarginBalance(
    val total: BigDecimal,
    val interest: BigDecimal,
    val borrowed: BigDecimal
)