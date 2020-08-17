import org.koin.dsl.module
import trading.exchange.LoanService
import trading.exchange.MarginAssetTable
import trading.exchange.MarginOrderService
import trading.exchange.PriceTable
import trading.exchange.binance.*
import trading.margin.MarginTradingService

val pricesModule = module(createdAtStart = true) {
    single(createdAtStart = true) { BinancePriceTable() as PriceTable }
    single(createdAtStart = true) { BinanceWebsocketUpdatePrices(get(), get()) }
}

val marginTradingModule = module(createdAtStart = true) {
    single {
        BinanceApiConfig(
            apiKey = getProperty("exchanges.binance.apiKey"),
            secretKey = getProperty("exchanges.binance.secretKey"),
            baseUrl = getProperty("exchanges.binance.baseUrl"),
            wsHost = getProperty("exchanges.binance.wsHost"),
            wsPort = getProperty("exchanges.binance.wsPort").toInt()
        )
    }

    single { BinanceHttpClientFactory(get()) }
    single { BinanceMarginAssetTable(get(), get()) as MarginAssetTable }
    single { BinanceMarginOrderService(get(), get()) as MarginOrderService }
    single { BinanceLoanService(get(), get()) as LoanService }

    single { MarginTradingService(get(), get(), get()) }
}