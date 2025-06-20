package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber

class NetworkRequestWithTimeoutViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest(timeout: Long) {

        usingWithTimeoutOrNull(timeout)
    }


    private fun usingWithTimeout(timeout: Long) {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val recentAndroidVersions = withTimeout(timeout) {
                    api.getRecentAndroidVersions()
                }
                uiState.value = UiState.Success(recentAndroidVersions)
            } catch (timeoutCancellationException: TimeoutCancellationException) {
                Timber.e(timeoutCancellationException)
                uiState.value = UiState.Error("Network Request timed out!!")
            } catch (e: Exception) {
                Timber.e(e)
                uiState.value = UiState.Error("Network Error!")
            }
        }
    }
    private fun usingWithTimeoutOrNull(timeout: Long) {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val recentAndroidVersions = withTimeoutOrNull(timeout) {
                    api.getRecentAndroidVersions()
                }
                uiState.value = UiState.Success(recentAndroidVersions?: throw NullPointerException())
            } catch (nullPointerException: NullPointerException) {
                Timber.e(nullPointerException)
                uiState.value = UiState.Error("${nullPointerException.message} Network Request timed out!!")
            } catch (e: Exception) {
                Timber.e(e)
                uiState.value = UiState.Error("Network Error!")
            }
        }
    }


}

