package trading.exchange.binance

import trading.Pair
import trading.exchange.PriceData
import trading.exchange.PriceTable
import java.util.concurrent.ConcurrentHashMap

class BinancePriceTable : PriceTable {
    private val prices = ConcurrentHashMap<Pair, PriceData>()

    override fun get(pair: Pair): PriceData = prices[pair]!!

    override fun getAll(): Map<Pair, PriceData> = prices

    override fun update(pair: Pair, price: PriceData) {
        prices[pair] = price
    }
}