package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import timber.log.Timber

class TimeoutAndRetryViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        val numberOfRetries = 2
        val timeout = 1000L

        try {
            viewModelScope.launch {
                val oreoFeaturesDeferred = async {
                    retryWithTimeout(
                        timeout,
                        numberOfRetries
                    ){
                        api.getAndroidVersionFeatures(27)
                    }
                }

                val pieFeaturesDeferred = async {
                    retryWithTimeout(
                        timeout,
                        numberOfRetries
                    ){
                        api.getAndroidVersionFeatures(28)
                    }
                }

                val features = awaitAll(oreoFeaturesDeferred, pieFeaturesDeferred)
                uiState.value = UiState.Success(features)
            }

        } catch (timeOutCancellation: TimeoutCancellationException){
            Timber.e(timeOutCancellation)
            uiState.value = UiState.Error("${timeOutCancellation.localizedMessage} Network request timed out")
        } catch (e: Exception){
            Timber.e(e)
            uiState.value = UiState.Error("${e.localizedMessage} Unknown Error")
        }

    }

    private suspend fun <T>retryWithTimeout(
        timeout: Long = 1000L,
        retryAttempts: Int = 2,
        initialRequestMillis: Long = 100L,
        maxDelayMillis: Long = 1000L,
        exponentialFactor: Double = 2.0,
        requestBlock: suspend ()-> T
    ): T{
        return retry(
            retryAttempts,
            initialRequestMillis,
            maxDelayMillis,
            exponentialFactor
        ) {
            withTimeout(timeMillis = timeout){
                requestBlock()
            }
        }
    }


    private suspend fun <T> retry(
        retryAttempts: Int = 2,
        initialRequestMillis: Long = 100L,
        maxDelayMillis: Long = 1000L,
        exponentialFactor: Double = 2.0,
        requestBlock: suspend ()-> T
    ): T {
        var currentDelay = initialRequestMillis
        repeat(retryAttempts){
            try {
                return requestBlock()
            } catch (e: Exception){
                Timber.e(e)
            }
            delay(currentDelay)
            currentDelay = (currentDelay*exponentialFactor).toLong()
                .coerceAtMost(maxDelayMillis)
        }
        return requestBlock()
    }

}