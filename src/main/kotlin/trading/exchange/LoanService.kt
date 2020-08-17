package trading.exchange

import trading.Currency
import java.math.BigDecimal

interface LoanService {
    suspend fun borrow(amount: BigDecimal, currency: Currency)

    suspend fun repay(amount: BigDecimal, currency: Currency)
}