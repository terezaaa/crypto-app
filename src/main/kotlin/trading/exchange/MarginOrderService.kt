package trading.exchange

import trading.OrderSide
import trading.Pair
import java.math.BigDecimal

interface MarginOrderService {
    suspend fun createMarketOrder(pair: Pair, amount: BigDecimal, side: OrderSide)
}