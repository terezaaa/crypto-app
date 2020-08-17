@file:JvmName("Main")

import org.koin.core.context.startKoin
import trading.Currency
import trading.Pair
import trading.exchange.MarginBalance

suspend fun main() {
    startKoin {
        fileProperties("/properties.conf")
        modules(pricesModule, marginTradingModule)
    }

    val priceComponent = PriceComponent()
    val marginTrading = MarginTradingComponent()

    while (true) {
        print("-> ")

        readLine().let {
            when {
                it == "prices" -> catchable {
                    priceComponent.getPrices()
                        .forEach { println("${it.key.base}-${it.key.counter} => ${it.value.price}") }
                }

                it == "margin assets" -> catchable {
                    marginTrading.getAssets().forEach {
                        printAsset(it.value, it.key)
                    }
                }

                it!!.startsWith("margin asset") -> {
                    val (_, _, currencyValue) = it.split(" ")
                    val currency = Currency.valueOf(currencyValue)

                    val asset = marginTrading.getAsset(currency)
                    printAsset(asset, currency)
                }

                it.startsWith("margin let") -> {
                    val (_, _, base, counter, asset) = it.split(" ")

                    try {
                        marginTrading.let(Pair(Currency.valueOf(base), Currency.valueOf(counter)), asset.toBigDecimal())

                        println("Ok.")
                    } catch (e: Exception) {
                        println("Error: ${e.message}")
                    }
                }

                else -> {
                    println("Supported commands:")
                    println("Get prices => prices")
                    println("Get margin assets => margin assets, margin asset {coin}")
                    println("Make asset => margin let {base coin} {counter coin} {value}")
                }
            }
        }
    }
}

private fun catchable(body: () -> Any) {
    try {
        body()
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

private fun printAsset(asset: MarginBalance, currency: Currency) {
    println("${currency}")
    println("total: ${asset.total}")
    println("free: ${asset.interest}")
    println("borrowed: ${asset.borrowed}")

    println()
}