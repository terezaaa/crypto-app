import org.koin.core.KoinComponent
import org.koin.core.inject
import trading.Currency
import trading.Pair
import trading.exchange.MarginAssetTable
import trading.exchange.MarginBalance
import trading.margin.MarginTradingService
import java.math.BigDecimal

class MarginTradingComponent : KoinComponent {
    private val marginAssetTable by inject<MarginAssetTable>()
    private val marginTradingService by inject<MarginTradingService>()


    suspend fun let(pair: Pair, expectedAsset: BigDecimal) {
        marginTradingService.let(pair, expectedAsset)
    }

    fun getAsset(currency: Currency): MarginBalance = marginAssetTable.get(currency)

    fun getAssets(): Map<Currency, MarginBalance> = marginAssetTable.getAll()
}