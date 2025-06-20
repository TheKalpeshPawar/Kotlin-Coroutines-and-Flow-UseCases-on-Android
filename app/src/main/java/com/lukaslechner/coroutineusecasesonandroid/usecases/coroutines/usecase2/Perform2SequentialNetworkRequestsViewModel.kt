package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber


class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            lateinit var recentAndroidVersion: List<AndroidVersion>
            lateinit var features: VersionFeatures
            try{
                recentAndroidVersion = mockApi.getRecentAndroidVersions()

                if(recentAndroidVersion.isNotEmpty()){
                    try {
                        features = mockApi.getAndroidVersionFeatures(recentAndroidVersion.last().apiLevel)
                    }catch (e: Exception){
                        Timber.e(e)
                        uiState.value = UiState.Error("Failed to fetch android version features ")
                    }
                }
                uiState.value = UiState.Success(features)
            }catch (e: Exception){
                Timber.e(e)
                uiState.value = UiState.Error("Failed to fetch recent android versions")
            }
        }

    }
}
