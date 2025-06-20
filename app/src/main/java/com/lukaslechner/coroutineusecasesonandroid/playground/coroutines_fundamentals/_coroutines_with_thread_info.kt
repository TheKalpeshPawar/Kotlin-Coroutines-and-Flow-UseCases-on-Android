package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines_fundamentals

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("main starts")
    joinAll(
        async {
            coroutineWithThreadInfo(1, 500)
        },
        async{
            coroutineWithThreadInfo(2, 300)
        }
    )

    println("Main ends")
}


suspend fun coroutineWithThreadInfo(number: Int, delay: Long = 500){
    println("Coroutine $number starts work on ${Thread.currentThread().name}.")
    delay(delay)
    println("Coroutine $number has finished ${Thread.currentThread().name}.")
}