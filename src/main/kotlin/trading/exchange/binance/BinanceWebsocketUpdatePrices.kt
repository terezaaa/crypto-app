package trading.exchange.binance

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.wss
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import trading.Currency
import trading.Pair
import trading.exchange.PriceData
import trading.exchange.PriceTable
import java.math.BigDecimal

class BinanceWebsocketUpdatePrices(
    private val priceTable: PriceTable,
    private val apiConfig: BinanceApiConfig
) {
    private val wsClient = HttpClient {
        install(WebSockets)
    }

    init {
        val serializer = jacksonObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        val pairs = Currency.values().map { Pair(it, Currency.USDT) }
        val pairsSymbolMap = pairs.map { "${it.base}${it.counter}" to it }.toMap()

        println("/stream?streams=${pairs.map { "${it.base}${it.counter}@ticker".toLowerCase() }
            .reduce { acc, s -> "$acc/$s" }}")

        GlobalScope.launch {
            wsClient.wss(
                HttpMethod.Get,
                apiConfig.wsHost,
                apiConfig.wsPort,
                "/stream?streams=${pairs.map { "${it.base}${it.counter}@ticker".toLowerCase() }
                    .reduce { acc, s -> "$acc/$s" }}"
            ) {
                while (!incoming.isClosedForReceive) {
                    val frame = incoming.receive()
                    when (frame) {
                        is Frame.Text -> {
                            val text = frame.readText()
                            val streamPayload = serializer.readValue(text, StreamPayload::class.java)
                            val pair = pairsSymbolMap[streamPayload.data.s]!!
                            priceTable.update(pair, PriceData(streamPayload.data.c))
                        }
                    }
                }
            }
        }
    }
}

data class StreamPayload(
    val data: Price
)

data class Price(
    val s: String,
    val c: BigDecimal
)