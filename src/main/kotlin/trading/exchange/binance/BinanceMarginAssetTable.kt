package trading.exchange.binance

import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking
import trading.Currency
import trading.exchange.MarginAssetTable
import trading.exchange.MarginBalance
import java.math.BigDecimal

class BinanceMarginAssetTable(
    binanceHttpClientFactory: BinanceHttpClientFactory,
    private val apiConfig: BinanceApiConfig
) : MarginAssetTable {
    private val httpClient = binanceHttpClientFactory.create()

    override fun get(currency: Currency): MarginBalance = getAll()[currency]!!

    override fun getAll(): Map<Currency, MarginBalance> {
        val response = runBlocking {
            httpClient.get<GetAssetsResponseDto>(
                "${apiConfig.baseUrl}$ASSET_PATH?${getQueryStringWithSignature(
                    mapOf(
                        "timestamp" to System.currentTimeMillis().toString()
                    ), apiConfig.secretKey
                )}"
            )
        }

        return response.userAssets.map {
            Currency.valueOf(it.asset) to MarginBalance(
                it.netAsset,
                it.free,
                it.borrowed
            )
        }.toMap()
    }

    companion object {
        private val ASSET_PATH = "/sapi/v1/margin/account"
    }
}

data class GetAssetsResponseDto(
    val userAssets: List<UserAssetsDto>
)

data class UserAssetsDto(
    val asset: String,
    val netAsset: BigDecimal,
    val free: BigDecimal,
    val borrowed: BigDecimal
)