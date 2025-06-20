package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading
        try {
            viewModelScope.launch {
                val oreoFeatures = mockApi.getAndroidVersionFeatures(27)
                val pieFeatures = mockApi.getAndroidVersionFeatures(28)
                val android10Features = mockApi.getAndroidVersionFeatures(29)

                val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)
                uiState.value = UiState.Success(versionFeatures)
            }
        }catch (e: Exception){
            uiState.value = UiState.Error("Network request failed")
        }
    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading

        val startTime = System.currentTimeMillis()

        val oreoFeaturesDeferred = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(27)
        }
        val pieFeaturesDeferred = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(28)
        }
        val android10FeaturesDeferred = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(29)
        }
        println("Time elapsed after calling all three async blocks: ${elapsedMills(startTime)}")

        viewModelScope.launch {
        try {

//            val oreoFeatures = oreoFeaturesDeferred.await()
//            println("Time elapsed after calling first await: ${elapsedMills(startTime)}")
//            val pieFeatures = pieFeaturesDeferred.await()
//            println("Time elapsed after calling second await: ${elapsedMills(startTime)}")
//            val android10Features = android10FeaturesDeferred.await()
//            println("Time elapsed after calling third await: ${elapsedMills(startTime)}")

            val versionFeatures = awaitAll(
                oreoFeaturesDeferred,
                pieFeaturesDeferred,
                android10FeaturesDeferred
            )
            uiState.value = UiState.Success(versionFeatures)

        } catch (e: Exception) {
            uiState.value = UiState.Error("Network request failed")
        }
    }
    }
}

suspend fun networkCall(number: Int): String {
    delay(500)
    return "Result $number"
}

fun elapsedMills(startTime: Long) = System.currentTimeMillis() - startTime