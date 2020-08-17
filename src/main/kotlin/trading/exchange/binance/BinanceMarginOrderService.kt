package trading.exchange.binance

import io.ktor.client.request.post
import trading.OrderSide
import trading.OrderType
import trading.Pair
import trading.exchange.MarginOrderService
import java.math.BigDecimal
import java.math.RoundingMode

class BinanceMarginOrderService(
    binanceHttpClientFactory: BinanceHttpClientFactory,
    private val apiConfig: BinanceApiConfig
) : MarginOrderService {

    private val httpClient = binanceHttpClientFactory.create()

    override suspend fun createMarketOrder(pair: Pair, amount: BigDecimal, side: OrderSide) {
        httpClient.post<PlaceOrderResponse>(
            "${apiConfig.baseUrl}${PLACE_ORDER}?${getQueryStringWithSignature(
                mapOf(
                    "symbol" to "${pair.base}${pair.counter}",
                    "side" to "$side",
                    "type" to "${OrderType.MARKET}",
                    "quantity" to "${amount.setScale(currencyScale, RoundingMode.DOWN)}",
                    "timestamp" to System.currentTimeMillis().toString()
                ), apiConfig.secretKey
            )}"
        )
    }

    companion object {
        val PLACE_ORDER = "/sapi/v1/margin/order"
    }
}

data class PlaceOrderResponse(
    val orderId: Long
)