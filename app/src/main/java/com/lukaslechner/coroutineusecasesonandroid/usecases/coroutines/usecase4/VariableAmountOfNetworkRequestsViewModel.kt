package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class VariableAmountOfNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading

        try {
            viewModelScope.launch {
                val recentAndroidVersions = mockApi.getRecentAndroidVersions()

                val featureOfAndroidVersions = recentAndroidVersions.map {androidVersion ->
                    mockApi.getAndroidVersionFeatures(androidVersion.apiLevel)
                }

                uiState.value = UiState.Success(featureOfAndroidVersions)
            }
        } catch (e: Exception){
            uiState.value = UiState.Error(
                e.localizedMessage ?: "Network request failed"
            )
        }
    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading
        try {

            viewModelScope.launch {
                val recentAndroidVersions = mockApi.getRecentAndroidVersions()

                val featureOfAndroidVersions = recentAndroidVersions.map { androidVersion ->
                    this.async {
                        mockApi.getAndroidVersionFeatures(androidVersion.apiLevel)
                    }
                }

                uiState.value = UiState.Success(
                    featureOfAndroidVersions.awaitAll()
                )
            }

        } catch (e: Exception){
            uiState.value = UiState.Error(
                e.localizedMessage ?: "Unknown error"
            )
        }
    }
}