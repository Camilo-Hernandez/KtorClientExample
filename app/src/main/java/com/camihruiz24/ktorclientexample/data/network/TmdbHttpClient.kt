package com.camihruiz24.ktorclientexample.data.network

import android.util.Log
import com.camihruiz24.ktorclientexample.data.secret.TmdbApiKeys
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject


class TmdbHttpClient @Inject constructor(
) {

    companion object {
        private const val TIME_OUT = 10_000
        private const val TAG_KTOR_LOGGER = "ktor_logger:"
        private const val TAG_HTTP_STATUS_LOGGER = "http_status:"
    }

    fun createHttpClient() = HttpClient(Android){
        engine {
            connectTimeout = TIME_OUT
            socketTimeout = TIME_OUT
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
        }

        install(Logging){
            level = LogLevel.BODY
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v(TAG_KTOR_LOGGER, message)
                }
            }
        }

        install(ResponseObserver){
            onResponse {
                Log.d(TAG_HTTP_STATUS_LOGGER, "${it.status.value}")
            }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            parameters {
                append("api_key", TmdbApiKeys.API_KEY_1)
            }
        }
    }

}