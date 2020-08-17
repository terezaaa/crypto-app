package trading.exchange.binance

import org.apache.commons.codec.digest.HmacUtils

val currencyScale = 6

fun getQueryStringWithSignature(queryParams: Map<String, String>, secretKey: String): String {
    val paramsString = queryParams.map { "${it.key}=${it.value}" }.reduce { acc, s -> "$acc&$s" }

    val signature = HmacUtils.hmacSha256Hex(
        secretKey,
        paramsString
    )

    return "$paramsString&signature=$signature"
}