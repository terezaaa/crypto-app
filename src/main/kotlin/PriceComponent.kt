import org.koin.core.KoinComponent
import org.koin.core.inject
import trading.Pair
import trading.exchange.PriceData
import trading.exchange.PriceTable

class PriceComponent : KoinComponent {
    private val priceProvider by inject<PriceTable>()

    fun getPrices(): Map<Pair, PriceData> = priceProvider.getAll()
}