package trading.exchange.binance

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.header
import mu.KLoggable
import org.apache.http.impl.NoConnectionReuseStrategy

class BinanceHttpClientFactory(private val apiConfig: BinanceApiConfig) : KLoggable {

    override val logger = logger()

    fun create(): HttpClient = HttpClient(Apache) {
        engine {
            socketTimeout = SOCKET_TIMEOUT
            connectTimeout = CONNECT_TIMEOUT
            connectionRequestTimeout = CONNECTION_REQUEST_TIMEOUT
            customizeClient {
                setMaxConnTotal(MAX_CONN_TOTAL)
                setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
                setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE)
            }
        }
        expectSuccess = true
        defaultRequest {
            header("X-MBX-APIKEY", apiConfig.apiKey)
        }
        install(JsonFeature) {
            serializer = JacksonSerializer {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
                setDefaultPropertyInclusion(
                    JsonInclude.Value.construct(JsonInclude.Include.ALWAYS, JsonInclude.Include.NON_NULL)
                )
                setSerializationInclusion(JsonInclude.Include.NON_NULL)
            }
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    logger().trace(message)
                }
            }

            level = LogLevel.ALL
        }

    }

    companion object {
        const val SOCKET_TIMEOUT = 30_000
        const val CONNECT_TIMEOUT = 90_000
        const val CONNECTION_REQUEST_TIMEOUT = 35_000
        const val MAX_CONN_TOTAL = 550
        const val MAX_CONN_PER_ROUTE = 550
    }
}