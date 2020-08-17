package trading.exchange.binance

data class BinanceApiConfig(
    val apiKey: String,
    val secretKey: String,
    val baseUrl: String,
    val wsHost: String,
    val wsPort: Int
)