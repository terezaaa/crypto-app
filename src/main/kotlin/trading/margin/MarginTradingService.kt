package trading.margin

import trading.OrderSide
import trading.Pair
import trading.exchange.LoanService
import trading.exchange.MarginAssetTable
import trading.exchange.MarginBalance
import trading.exchange.MarginOrderService
import java.math.BigDecimal

class MarginTradingService(
    private val assetTable: MarginAssetTable,
    private val marginOrderService: MarginOrderService,
    private val loanService: LoanService
) {
    suspend fun let(pair: Pair, expectedAsset: BigDecimal) {
        val asset = assetTable.get(pair.base)

        when {
            asset.total < expectedAsset -> buy(pair, expectedAsset - asset.total, asset)
            asset.total > expectedAsset -> sell(pair, asset.total - expectedAsset, asset)
        }
    }

    private suspend fun buy(pair: Pair, amount: BigDecimal, asset: MarginBalance) {
        marginOrderService.createMarketOrder(pair, amount, OrderSide.BUY)

        if (asset.borrowed > BigDecimal.ZERO) {
            loanService.repay(amount.min(asset.borrowed), pair.base)
        }
    }

    private suspend fun sell(pair: Pair, amount: BigDecimal, asset: MarginBalance) {
        if (asset.total < amount) {
            loanService.borrow(amount - asset.total, pair.base)
        }

        marginOrderService.createMarketOrder(pair, amount, OrderSide.SELL)
    }
}