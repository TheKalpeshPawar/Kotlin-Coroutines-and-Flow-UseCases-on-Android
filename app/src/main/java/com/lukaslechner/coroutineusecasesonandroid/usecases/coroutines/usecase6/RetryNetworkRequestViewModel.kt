package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class RetryNetworkRequestViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        viewModelScope.launch {

            val numberOfRetries = 2
            try {
                retry(numberOfRetries){
                    repeat(numberOfRetries){
                        loadRecentAndroidVersions()
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                uiState.value = UiState.Error("Network Error!")
            }
        }
    }

    private suspend fun loadRecentAndroidVersions(){
        val resentAndroidVersion = api.getRecentAndroidVersions()
        uiState.value = UiState.Success(resentAndroidVersion)
    }

    private suspend fun <T> retry(
        numberOfTimes: Int,
        initialDelayMillis: Long =100,
        maxDelayMillis: Long = 1000,
        factory: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelayMillis
        repeat(
            numberOfTimes
        ){
            try {
                return block()
            } catch (e: Exception){
                Timber.e(e)
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factory).toLong().coerceAtMost(maxDelayMillis)
        }
        return block()
    }
}