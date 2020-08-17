package trading.exchange

import trading.Pair
import java.math.BigDecimal

interface PriceTable {
    fun get(pair: Pair): PriceData

    fun getAll(): Map<Pair, PriceData>

    fun update(pair: Pair, price: PriceData)
}

data class PriceData(
        var price: BigDecimal
)