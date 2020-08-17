package trading.exchange.binance

import io.ktor.client.request.post
import trading.Currency
import trading.exchange.LoanService
import java.math.BigDecimal
import java.math.RoundingMode

class BinanceLoanService(
    binanceHttpClientFactory: BinanceHttpClientFactory,
    private val apiConfig: BinanceApiConfig
) : LoanService {
    private val httpClient = binanceHttpClientFactory.create()

    override suspend fun borrow(amount: BigDecimal, currency: Currency) {
        httpClient.post<BorrowResponse>(
            "${apiConfig.baseUrl}${BORROW_PATH}?${getQueryStringWithSignature(
                mapOf(
                    "asset" to "$currency",
                    "amount" to "${amount.setScale(currencyScale, RoundingMode.UP)}",
                    "timestamp" to System.currentTimeMillis().toString()
                ), apiConfig.secretKey
            )}"
        )
    }

    override suspend fun repay(amount: BigDecimal, currency: Currency) {
        httpClient.post<RepayResponse>(
            "${apiConfig.baseUrl}${REPLAY_PATH}?${getQueryStringWithSignature(
                mapOf(
                    "asset" to "$currency",
                    "amount" to "${amount.setScale(currencyScale, RoundingMode.UP)}",
                    "timestamp" to System.currentTimeMillis().toString()
                ), apiConfig.secretKey
            )}"
        )
    }

    companion object {
        const val BORROW_PATH = "/sapi/v1/margin/loan"
        const val REPLAY_PATH = "/sapi/v1/margin/repay"
    }
}

data class BorrowResponse(
    val tranId: Long
)

data class RepayResponse(
    val tranId: Long
)